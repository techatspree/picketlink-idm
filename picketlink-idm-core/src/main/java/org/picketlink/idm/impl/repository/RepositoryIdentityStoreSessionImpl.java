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

package org.picketlink.idm.impl.repository;

import org.picketlink.idm.spi.store.IdentityStoreSession;
import org.picketlink.idm.common.exception.IdentityException;

import java.util.Map;

/**
 * Wrapper around many IdentityStoreSession objects. Simply iterates and delegates method invocation
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class RepositoryIdentityStoreSessionImpl implements IdentityStoreSession
{

   //TODO: more sophisticated impl needed.

   protected final Map<String, IdentityStoreSession> identityStoreSessionMappings;

   public RepositoryIdentityStoreSessionImpl(Map<String, IdentityStoreSession> identityStoreSessionMappings)
   {
      if (identityStoreSessionMappings == null)
      {
         throw new IllegalArgumentException();
      }

      this.identityStoreSessionMappings = identityStoreSessionMappings;
   }

   public Object getSessionContext() throws IdentityException
   {
      return null;
   }

   public IdentityStoreSession getIdentityStoreSession(String storeId)
   {
      return identityStoreSessionMappings.get(storeId);
   }

   public void close() throws IdentityException
   {
      for (IdentityStoreSession identityStoreSession : identityStoreSessionMappings.values())
      {
         identityStoreSession.close();
      }
   }

   public void save() throws IdentityException
   {
      for (IdentityStoreSession iss : identityStoreSessionMappings.values())
      {
         iss.save();
      }

   }

   public void clear() throws IdentityException
   {
      for (IdentityStoreSession iss : identityStoreSessionMappings.values())
      {
         iss.clear();
      }

   }

   public boolean isOpen()
   {
      for (IdentityStoreSession identityStoreSession : identityStoreSessionMappings.values())
      {
         if (identityStoreSession.isOpen())
         {
            return true;
         }
      }
      return false;
   }

   public boolean isTransactionSupported()
   {
      for (IdentityStoreSession identityStoreSession : identityStoreSessionMappings.values())
      {
         if (identityStoreSession.isTransactionSupported())
         {
            return true;
         }
      }
      return false;
   }

   public void startTransaction()
   {
      for (IdentityStoreSession identityStoreSession : identityStoreSessionMappings.values())
      {
         identityStoreSession.startTransaction();
      }
   }

   public void commitTransaction()
   {
      for (IdentityStoreSession identityStoreSession : identityStoreSessionMappings.values())
      {
         identityStoreSession.commitTransaction();
      }
   }

   public void rollbackTransaction()
   {
      for (IdentityStoreSession identityStoreSession : identityStoreSessionMappings.values())
      {
         identityStoreSession.rollbackTransaction();
      }
   }

   public boolean isTransactionActive()
   {
      for (IdentityStoreSession identityStoreSession : identityStoreSessionMappings.values())
      {
         if (identityStoreSession.isTransactionActive())
         {
            return true;
         }
      }
      return false;
   }
}
