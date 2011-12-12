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

package org.picketlink.idm.impl.store.ldap;

import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.spi.store.IdentityStoreSession;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * Session around LDAP store. Exposes LDAP connection (LdapContext) and does nothing for transaction related methods
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class LDAPIdentityStoreSessionImpl implements IdentityStoreSession
{
   private static Logger log = Logger.getLogger(LDAPIdentityStoreSessionImpl.class.getName());

   private final LDAPIdentityStoreConfiguration storeConfig;

   public LDAPIdentityStoreSessionImpl(LDAPIdentityStoreConfiguration storeConfig)
   {
      this.storeConfig = storeConfig;

   }


   public LdapContext getLdapContext() throws Exception
   {

      if (storeConfig.getExternalJNDIContext() != null)
      {
         InitialContext iniCtx = new InitialContext();
         return (LdapContext)iniCtx.lookup(storeConfig.getExternalJNDIContext());
      }

      if (storeConfig.getCustomSystemProperties() != null &&
         storeConfig.getCustomSystemProperties().size() > 0)
      {

         Map<String, String> props = storeConfig.getCustomSystemProperties();

         for (String name : props.keySet())
         {
            System.setProperty(name, props.get(name));
         }
      }

      Hashtable<String,String> env = new Hashtable<String,String>();


      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

      if (storeConfig.getProviderURL() != null)
      {
         env.put(Context.PROVIDER_URL, storeConfig.getProviderURL());

      }
      if (storeConfig.getAdminDN() != null)
      {
         env.put(Context.SECURITY_PRINCIPAL, storeConfig.getAdminDN());

      }
      if (storeConfig.getAdminPassword() != null)
      {
         env.put(Context.SECURITY_CREDENTIALS, storeConfig.getAdminPassword());

      }

      if (storeConfig.getAuthenticationMethod() != null)
      {
         env.put(Context.SECURITY_AUTHENTICATION, storeConfig.getAuthenticationMethod());
      }
      else
      {
         env.put(Context.SECURITY_AUTHENTICATION, "simple");
      }

      if (storeConfig.getCustomJNDIConnectionParameters() != null &&
         storeConfig.getCustomJNDIConnectionParameters().size() > 0)
      {

         Map<String, String> params = storeConfig.getCustomJNDIConnectionParameters();

         for (String name : params.keySet())
         {
            env.put(name, params.get(name));
         }
      }


      return new InitialLdapContext(env, null);
   }

   public Object getSessionContext() throws IdentityException
   {
      try
      {
         return getLdapContext();
      }
      catch (Exception e)
      {

         if (log.isLoggable(Level.FINER))
         {
            log.log(Level.FINER, "Exception occured: ", e);
         }

         throw new IdentityException("Could not create LdapContext", e);
      }
   }

   public void close() throws IdentityException
   {

   }

   public void save() throws IdentityException
   {

   }

   public void clear() throws IdentityException
   {

   }

   public boolean isOpen()
   {
      return false;
   }

   public boolean isTransactionSupported()
   {
      return false;
   }

   public void startTransaction()
   {

   }

   public void commitTransaction()
   {

   }

   public void rollbackTransaction()
   {

   }

   public boolean isTransactionActive()
   {
      return false;
   }
}
