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

package org.picketlink.idm.example;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;
import java.util.Hashtable;
import java.io.File;

import org.opends.server.tools.LDAPModify;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.Binding;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.InitialLdapContext;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class TestBase extends TestCase
{
   private static Logger logger = Logger.getLogger(TestBase.class.getName());

   private Connection connection;

   private OpenDSService openDSService;

   public static final String LDAP_HOST = "localhost";

   public static final String LDAP_PORT = "10389";

   public static final String LDAP_PROVIDER_URL = "ldap://" + LDAP_HOST + ":" + LDAP_PORT;

   public static final String LDAP_PRINCIPAL = "cn=Directory Manager";

   public static final String LDAP_CREDENTIALS = "password";


   protected void startDatabase() throws Exception
   {
      try {
         logger.info("Starting in-memory HSQL database for unit tests");
         Class.forName("org.hsqldb.jdbcDriver");
         connection = DriverManager.getConnection("jdbc:hsqldb:mem:sample-test-db", "sa", "");
      }
      catch (Exception ex)
      {                                                              
         ex.printStackTrace();
         logger.fine("Exception during HSQL database startup.");
         throw ex;
      }
   }

   protected void stopDatabase() throws Exception
   {
      logger.info("Stopping in-memory HSQL database.");
      try
      {
         connection.createStatement().execute("SHUTDOWN");
      }
      catch (Exception ex) {
         throw ex;
      }
   }

   protected void startLDAP() throws Exception
   {
      super.setUp();

      openDSService = new OpenDSService("target/test-classes/opends");
      openDSService.start();
   }

   protected void stopLDAP() throws Exception
   {
      openDSService.stop();
   }

   public void populateLDIF(String ldifRelativePath) throws Exception
   {
      File ldif = new File(ldifRelativePath);

      System.out.println("LDIF: " + ldif.getAbsolutePath());

      String[] cmd = new String[] {"-h", LDAP_HOST,
            "-p", LDAP_PORT,
            "-D", LDAP_PRINCIPAL,
            "-w", LDAP_CREDENTIALS,
            "-a", "-f", ldif.getPath()};

      logger.fine("Populate success: " + (LDAPModify.mainModify(cmd, false, System.out, System.err) == 0));

   }

   protected void cleanUpDN(String dn) throws Exception
   {
      DirContext ldapCtx = getLdapContext();

      try
      {
         logger.fine("Removing: " + dn);

         removeContext(ldapCtx, dn);
      }
      catch (Exception e)
      {
         //
      }
      finally
      {
         ldapCtx.close();
      }
   }

   //subsequent remove of javax.naming.Context
   private void removeContext(Context mainCtx, String name) throws Exception
   {
      Context deleteCtx = (Context)mainCtx.lookup(name);
      NamingEnumeration subDirs = mainCtx.listBindings(name);

      while (subDirs.hasMoreElements())
      {
         Binding binding = (Binding)subDirs.nextElement();
         String subName = binding.getName();

         removeContext(deleteCtx, subName);
      }

      mainCtx.unbind(name);
   }



   private LdapContext getLdapContext() throws Exception
   {
      Hashtable<String,String> env = new Hashtable<String,String>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      env.put(Context.PROVIDER_URL, LDAP_PROVIDER_URL);
      env.put(Context.SECURITY_AUTHENTICATION, "simple");
      env.put(Context.SECURITY_PRINCIPAL, LDAP_PRINCIPAL);
      env.put(Context.SECURITY_CREDENTIALS, LDAP_CREDENTIALS);

      return new InitialLdapContext(env, null);
   }

}
