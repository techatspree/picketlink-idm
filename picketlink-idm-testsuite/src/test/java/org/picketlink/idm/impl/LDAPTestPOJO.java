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

package org.picketlink.idm.impl;

import org.picketlink.idm.opends.OpenDSService;
import org.jboss.portal.test.framework.embedded.DSConfig;

import java.net.URL;
import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.opends.server.tools.LDAPModify;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class LDAPTestPOJO extends IdentityTestPOJO
{

   private String EMBEDDED_OPEN_DS_DIRECTORY_NAME = "EmbeddedOpenDS";

   protected DSConfig directoryConfig;

   private String directories = "datasources/directories.xml";

   //By default use embedded OpenDS
   private String directoryName = EMBEDDED_OPEN_DS_DIRECTORY_NAME;

   public static Hashtable<String,String> env = new Hashtable<String,String>();

   OpenDSService openDSService = new OpenDSService(null);

   @Override
   public void start() throws Exception
   {

      overrideFromProperties();

      directoryConfig = DSConfig.obtainConfig(directories, directoryName);

      identityConfig = directoryConfig.getConfigFile();

      super.start();

      env.put(Context.INITIAL_CONTEXT_FACTORY, directoryConfig.getContextFactory());
      //Use description to store URL to be able to prefix with "ldaps://"
      env.put(Context.PROVIDER_URL, directoryConfig.getDescription());
      env.put(Context.SECURITY_AUTHENTICATION, "simple");
      env.put(Context.SECURITY_PRINCIPAL, directoryConfig.getAdminDN());
      env.put(Context.SECURITY_CREDENTIALS, directoryConfig.getAdminPassword());

      if (directoryName.equals(EMBEDDED_OPEN_DS_DIRECTORY_NAME))
      {
         openDSService.start();
      }
   }

   @Override
   public void stop() throws Exception
   {
      cleanUp(new InitialLdapContext(env, null));

      super.stop();

      if (directoryName.equals(EMBEDDED_OPEN_DS_DIRECTORY_NAME))
      {
         openDSService.stop();
      }

   }

   public void overrideFromProperties() throws Exception
   {
      super.overrideFromProperties();

      String dirName = System.getProperties().getProperty("directoryName");

      if (dirName != null && !dirName.startsWith("$"))
      {
         setDirectoryName(dirName);
      }

      String trustStorePath = System.getProperties().getProperty("trustStorePath");

      if (trustStorePath != null && !trustStorePath.startsWith("$"))
      {
         System.setProperty("javax.net.ssl.trustStore", trustStorePath);
      }

      String trustStorePassword = System.getProperties().getProperty("trustStorePassword");

      if (trustStorePassword != null && !trustStorePassword.startsWith("$"))
      {
         System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
      }

   }

   public void setDirectoryName(String directoryName)
   {
      this.directoryName = directoryName;
   }

   public String getDirectoryName()
   {
      return directoryName;
   }

//   public void populate() throws Exception
//   {
//      populateLDIF("ldap/initial-opends.ldif");
//   }

   public void populateClean() throws Exception
   {

      String ldif = directoryConfig.getPopulateLdif();
      if (ldif != null && ldif.length() != 0)
      {
         populateLDIF(directoryConfig.getPopulateLdif());
      }
   }

   public void populateLDIF(String ldif) throws Exception
   {

      URL ldifURL = Thread.currentThread().getContextClassLoader().getResource(ldif);

      System.out.println("LDIF: " + ldifURL.toURI().getPath());

      String[] cmd = new String[] {"-h", directoryConfig.getHost(),
            "-p", directoryConfig.getPort(),
            "-D", directoryConfig.getAdminDN(),
            "-w", directoryConfig.getAdminPassword(),
            "-a", "-f", ldifURL.toURI().getPath()};

      //Not sure why... but it actually does make a difference...
      if (directoryName.equals(EMBEDDED_OPEN_DS_DIRECTORY_NAME))
      {
         System.out.println("Populate success: " + (LDAPModify.mainModify(cmd, false, System.out, System.err) == 0));
      }
      else
      {
         System.out.println("Populate success: " + (LDAPModify.mainModify(cmd) == 0));
      }
   }

   protected void cleanUp(DirContext ldapCtx) throws Exception
   {
      try
      {
         String dn = directoryConfig.getCleanUpDN();

         System.out.println("Removing: " + dn);

         removeContext(ldapCtx, dn);
      }
      catch (Exception e)
      {
         //
      }
   }

   //subsequent remove of javax.naming.Context
   protected void removeContext(Context mainCtx, String name) throws Exception
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


   public LdapContext getLdapContext() throws Exception
   {
      return new InitialLdapContext(env, null);
   }


   
}
