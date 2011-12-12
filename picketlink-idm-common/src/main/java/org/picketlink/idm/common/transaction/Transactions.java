/*
* JBoss, a division of Red Hat
* Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/

package org.picketlink.idm.common.transaction;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.NotSupportedException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;

import org.apache.log4j.Logger;

/**
 * Utility class for managing transactions.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class Transactions
{

   /** . */
   private static Logger log = Logger.getLogger(Transactions.class);

   /** . */
   private static final String[] STATUS_NAMES = {
      "STATUS_ACTIVE",
      "STATUS_MARKED_ROLLBACK",
      "STATUS_PREPARED",
      "STATUS_COMMITTED",
      "STATUS_ROLLEDBACK",
      "STATUS_UNKNOWN",
      "STATUS_NO_TRANSACTION",
      "STATUS_PREPARING",
      "STATUS_COMMITTING",
      "STATUS_ROLLING_BACK"};

   /**
    * Decode the status name.
    *
    * @param status the status value
    * @return the translated status name or null if it is not valid
    */
   public static String decodeStatus(int status)
   {
      if (status >= 0 && status <= STATUS_NAMES.length)
      {
         return STATUS_NAMES[status];
      }
      else
      {
         return null;
      }
   }

   /**
    * Apply the transaction type before the unit of work.
    *
    * @param type the transaction type
    * @param tm the transaction manager
    * @return the new transaction if one has been started.
    * @throws TransactionException
    * @throws IllegalArgumentException if the type or the transaction manager is null
    */
   public static Transaction applyBefore(Type type, TransactionManager tm) throws TransactionException, IllegalArgumentException
   {
      if (tm == null)
      {
         throw new IllegalArgumentException("No transaction manager provided");
      }
      if (type == null)
      {
         throw new IllegalArgumentException("No type");
      }

      // Suspend the incoming transaction
      Transaction oldTx = suspend(tm);

      if (oldTx != null)
      {
         type.txBefore(tm, oldTx);
      }
      else
      {
         type.noTxBefore(tm);
      }

      return oldTx;
   }

   /**
    * Apply the transaction type after the unit of work has been done.
    *
    * @param type the transaction type
    * @param tm the transaction manager
    * @param oldTx the old transaction if it is not null
    * @throws TransactionException
    * @throws IllegalArgumentException if the type of the transaction manager is null
    */
   public static void applyAfter(Type type, TransactionManager tm, Transaction oldTx) throws TransactionException, IllegalArgumentException
   {
      if (tm == null)
      {
         throw new IllegalArgumentException("No transaction manager provided");
      }
      if (type == null)
      {
         throw new IllegalArgumentException("No type");
      }

      try
      {
         if (oldTx != null)
         {
            type.txAfter(tm, oldTx);
         }
         else
         {
            type.noTxAfter(tm);
         }
      }
      finally
      {
         if (oldTx != null)
         {
            try
            {
               resume(tm, oldTx);
            }
            catch (TransactionException ignore)
            {
               log.error("Was not capable to resume the incoming transaction", ignore);
            }
         }
      }
   }

   /**
    * Apply the transaction type around the unit of work.
    *
    * @param type the transaction type
    * @param tm the transaction manager
    * @param runnable the unit of work
    * @return the object returned by the runnable object
    * @throws NestedException wraps any exception throws by the runnable object
    * @throws TransactionException
    * @throws IllegalArgumentException if any method argument is null
    */
   public static Object apply(Type type, TransactionManager tm, final Runnable runnable) throws NestedException, TransactionException, IllegalArgumentException
   {
      if (tm == null)
      {
         throw new IllegalArgumentException("No transaction manager provided");
      }
      if (runnable == null)
      {
         throw new IllegalArgumentException("No code to execute");
      }
      if (type == null)
      {
         throw new IllegalArgumentException("No type");
      }

      // Any throwable thrown by the wrapped code
      Throwable throwable = null;

      // Any object returned by the wrapped code
      Object ret = null;

      // Suspend the incoming transaction
      Transaction oldTx = suspend(tm);

      try
      {
         if (oldTx != null)
         {
            type.txBefore(tm, oldTx);
            try
            {
               ret = runnable.run();
            }
            catch (Throwable t)
            {
               throwable = t;
            }
            finally
            {
               type.txAfter(tm, oldTx);
            }
         }
         else
         {
            type.noTxBefore(tm);
            try
            {
               ret = runnable.run();
            }
            catch (Throwable t)
            {
               throwable = t;
            }
            finally
            {
               type.noTxAfter(tm);
            }
         }
      }
      finally
      {
         if (oldTx != null)
         {
            try
            {
               resume(tm, oldTx);
            }
            catch (TransactionException ignore)
            {
               log.error("Was not capable to resume the incoming transaction", ignore);
            }
         }
      }
      if (throwable != null)
      {
         if (throwable instanceof Error)
         {
            throw (Error)throwable;
         }
         else
         {
            throw new NestedException(throwable);
         }
      }
      else
      {
         return ret;
      }
   }

   public static Object notSupported(TransactionManager tm, Runnable runnable)
      throws NestedException, TransactionException
   {
      return apply(TYPE_NOT_SUPPORTED, tm, runnable);
   }

   public static Object never(TransactionManager tm, Runnable runnable)
      throws NestedException, TransactionException
   {
      return apply(TYPE_NEVER, tm, runnable);
   }

   public static Object mandatory(TransactionManager tm, Runnable runnable)
      throws NestedException, TransactionException
   {
      return apply(TYPE_MANDATORY, tm, runnable);
   }

   public static Object supports(TransactionManager tm, Runnable runnable)
      throws NestedException, TransactionException
   {
      return apply(TYPE_SUPPORTS, tm, runnable);
   }

   public static Object required(TransactionManager tm, Runnable runnable)
      throws NestedException, TransactionException
   {
      return apply(TYPE_REQUIRED, tm, runnable);
   }

   public static Object requiresNew(TransactionManager tm, Runnable runnable)
      throws NestedException, TransactionException
   {
      return apply(TYPE_REQUIRES_NEW, tm, runnable);
   }

   /**
    * Begin a new transaction.
    *
    * @param tm the transaction manager
    * @throws IllegalArgumentException if the tm is null
    */
   public static void begin(TransactionManager tm) throws IllegalArgumentException, TransactionException
   {
      try
      {
         if (tm == null)
         {
            throw new IllegalArgumentException("No transaction manager");
         }
         tm.begin();
      }
      catch (SystemException e)
      {
         log.error("Problem when beginning transaction", e);
         throw new TransactionException(e);
      }
      catch (NotSupportedException e)
      {
         log.error("Problem when beginning transaction", e);
         throw new TransactionException(e);
      }
   }

   /**
    * Mark the transaction as rollback only.
    *
    * @param tx the transaction to mark as rollback only
    * @throws IllegalArgumentException if the transaction is null
    * @throws TransactionException
    */
   private static void setRollbackOnly(Transaction tx) throws IllegalArgumentException, TransactionException
   {
      try
      {
         if (tx == null)
         {
            throw new IllegalArgumentException("No transaction to set rollback only");
         }
         tx.setRollbackOnly();
      }
      catch (SystemException e)
      {
         log.error("Problem when setting transaction as rollback only", e);
         throw new TransactionException(e);
      }
   }

   /**
    * Mark the active transaction for this thread as rollback only
    *
    * @see #setRollbackOnly(javax.transaction.Transaction)
    * @param tm the transaction manager
    * @throws IllegalArgumentException if the tm is null
    */
   public static void setRollbackOnly(TransactionManager tm) throws IllegalArgumentException, TransactionException
   {
      try
      {
         if (tm == null)
         {
            throw new IllegalArgumentException("No transaction manager");
         }
         Transaction tx = tm.getTransaction();
         if (tx == null)
         {
            throw new TransactionException("No active transaction to set rollback only");
         }
         setRollbackOnly(tx);
      }
      catch (SystemException e)
      {
         log.error("Problem when setting transaction as rollback only", e);
         throw new TransactionException(e);
      }
   }

   public void safeSetRollbackOnly(TransactionManager tm)
   {
      try
      {
         setRollbackOnly(tm);
      }
      catch (IllegalArgumentException e)
      {
         log.error("", e);
      }
      catch (TransactionException e)
      {
         log.error("", e);
      }
   }

   public static void safeEnd(TransactionManager tm)
   {
      try
      {
         end(tm);
      }
      catch (IllegalArgumentException e)
      {
         log.error("", e);
      }
      catch (TransactionException e)
      {
         log.error("", e);
      }
   }

   /**
    * Terminate the active transaction for this thread. If the transaction is marked for rollback
    * then it is rollbacked otherwise it is commited.
    *
    * @param tm the transaction manager
    * @return true if commit happened, false otherwise
    * @throws IllegalArgumentException if the tm is null
    */
   public static boolean end(TransactionManager tm) throws IllegalArgumentException, TransactionException
   {
      try
      {
         if (tm == null)
         {
            throw new IllegalArgumentException("No transaction manager");
         }
         int status = tm.getStatus();
         switch (status)
         {
         case Status.STATUS_MARKED_ROLLBACK:
            tm.rollback();
            return false;
         case Status.STATUS_ACTIVE:
            tm.commit();
            return true;
         default:
            throw new TransactionException("Abnormal status for ending a tx " + STATUS_NAMES[status]);
         }
      }
      catch (SystemException e)
      {
         log.error("Problem when ending transaction", e);
         throw new TransactionException(e);
      }
      catch (HeuristicMixedException e)
      {
         log.error("Problem when ending transaction", e);
         throw new TransactionException(e);
      }
      catch (HeuristicRollbackException e)
      {
         log.error("Problem when ending transaction", e);
         throw new TransactionException(e);
      }
      catch (RollbackException e)
      {
         log.error("Problem when ending transaction", e);
         throw new TransactionException(e);
      }
   }

   /**
    * Associate the thread with a transaction
    *
    * @param tm the transaction manager
    * @param tx the transaction to associate with the this thread
    * @throws IllegalArgumentException if any argument is null
    * @throws TransactionException
    */
   public static void resume(TransactionManager tm, Transaction tx) throws IllegalArgumentException, TransactionException
   {
      try
      {
         if (tm == null)
         {
            throw new IllegalArgumentException("No transaction manager");
         }
         if (tx == null)
         {
            throw new IllegalArgumentException("No transaction to resume");
         }
         tm.resume(tx);
      }
      catch (Exception e)
      {
         log.error("Problem when resuming transaction", e);
         throw new TransactionException(e);
      }
   }

   /**
    * Disassociate the current thread with the active transaction.
    *
    * @param tm the transaction manager
    * @return the transaction previously associated with this thread
    * @throws IllegalArgumentException if the transaction manager is null
    * @throws TransactionException
    */
   public static Transaction suspend(TransactionManager tm) throws IllegalArgumentException, TransactionException
   {
      try
      {
         if (tm == null)
         {
            throw new IllegalArgumentException("No transaction manager");
         }
         return tm.suspend();
      }
      catch (SystemException e)
      {
         log.error("Problem when suspending transaction", e);
         throw new TransactionException(e);
      }
   }

   public interface Runnable
   {
      Object run() throws Exception;
   }

   public abstract static class Type
   {
      private final String name;

      private Type(String name)
      {
         this.name = name;
      }

      public Transaction before(TransactionManager tm)
      {
         return applyBefore(this, tm);
      }

      public void after(TransactionManager tm, Transaction oldTx)
      {
         applyAfter(this, tm, oldTx);
      }

      abstract void txBefore(TransactionManager tm, Transaction oldTx) throws TransactionException;

      abstract void txAfter(TransactionManager tm, Transaction oldTx);

      abstract void noTxBefore(TransactionManager tm) throws TransactionException;

      abstract void noTxAfter(TransactionManager tm);

      public String getName()
      {
         return name;
      }

      public String toString()
      {
         return name;
      }
   }

   public static final Type TYPE_NOT_SUPPORTED = new Type("NOT_SUPPORTED")
   {
      void txBefore(TransactionManager tm, Transaction oldTx)
      {
      }
      void txAfter(TransactionManager tm, Transaction oldTx)
      {
      }
      void noTxBefore(TransactionManager tm)
      {
      }
      void noTxAfter(TransactionManager tm)
      {
      }
   };

   public static final Type TYPE_SUPPORTS = new Type("SUPPORTS")
   {
      void txBefore(TransactionManager tm, Transaction oldTx) throws TransactionException
      {
         resume(tm, oldTx);
      }
      void txAfter(TransactionManager tm, Transaction oldTx)
      {
         try
         {
            suspend(tm);
         }
         catch (IllegalStateException ignore)
         {
            log.error("Problem when suspending transaction", ignore);
         }
      }
      void noTxBefore(TransactionManager tm)
      {
      }
      void noTxAfter(TransactionManager tm)
      {
      }
   };

   public static final Type TYPE_REQUIRED = new Type("REQUIRED")
   {
      void txBefore(TransactionManager tm, Transaction oldTx)
      {
         resume(tm, oldTx);
      }
      void txAfter(TransactionManager tm, Transaction oldTx)
      {
         try
         {
            suspend(tm);
         }
         catch (TransactionException ignore)
         {
            log.error("Problem when suspending transaction", ignore);
         }
      }
      void noTxBefore(TransactionManager tm) throws TransactionException
      {
         begin(tm);
      }
      void noTxAfter(TransactionManager tm)
      {
         try
         {
            end(tm);
         }
         catch (IllegalStateException ignore)
         {
            log.error("Problem when ending transaction", ignore);
         }
      }
   };

   public static final Type TYPE_REQUIRES_NEW = new Type("REQUIRES_NEW")
   {
      void txBefore(TransactionManager tm, Transaction oldTx) throws TransactionException
      {
         begin(tm);
      }
      void txAfter(TransactionManager tm, Transaction oldTx)
      {
         try
         {
            end(tm);
         }
         catch (IllegalStateException ignore)
         {
            log.error("Problem when ending transaction", ignore);
         }
      }
      void noTxBefore(TransactionManager tm) throws TransactionException
      {
         begin(tm);
      }
      void noTxAfter(TransactionManager tm)
      {
         try
         {
            end(tm);
         }
         catch (IllegalStateException ignore)
         {
            log.error("Problem when ending transaction", ignore);
         }
      }
   };

   public static final Type TYPE_MANDATORY = new Type("MANDATORY")
   {
      void txBefore(TransactionManager tm, Transaction oldTx) throws TransactionException
      {
         resume(tm, oldTx);
      }
      void txAfter(TransactionManager tm, Transaction oldTx)
      {
      }
      void noTxBefore(TransactionManager tm) throws TransactionException
      {
         throw new TransactionException("No incoming transaction");
      }
      void noTxAfter(TransactionManager tm)
      {
         throw new UnsupportedOperationException("Should never ne called");
      }
   };

   public static final Type TYPE_NEVER = new Type("NEVER")
   {
      void txBefore(TransactionManager tm, Transaction oldTx) throws TransactionException
      {
         throw new TransactionException("Need no incoming transaction");
      }
      void txAfter(TransactionManager tm, Transaction oldTx)
      {
         throw new UnsupportedOperationException("Should never ne called");
      }
      void noTxBefore(TransactionManager tm)
      {
      }
      void noTxAfter(TransactionManager tm)
      {
      }
   };
}
