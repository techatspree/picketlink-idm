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

package org.picketlink.idm.opends;

import java.io.File;
import java.net.URL;
import java.net.URISyntaxException;

import org.opends.server.util.EmbeddedUtils;
import org.opends.server.types.DirectoryEnvironmentConfig;
import org.opends.server.types.InitializationException;

/**
 * Simple service class that enables to bootstrap OpenDS LDAP server and to manage its lifecycle
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class OpenDSService
{
   private String serverRoot = "";

   public OpenDSService(String serverRoot)
   {
      this.serverRoot = serverRoot;
   }

   public DirectoryEnvironmentConfig getConfig()
   {
      DirectoryEnvironmentConfig config = new DirectoryEnvironmentConfig();


      try
      {
         File root;

         if (getServerRoot() != null)
         {
            root = new File(getServerRoot());
         }
         else
         {
            
            //Find opends root based on where the config.ldif file is:

           URL rootURL = Thread.currentThread().getContextClassLoader().getResource("opends/config/config.ldif");

            if (rootURL == null)
            {
               throw new IllegalStateException("opends root doesn't exist");
            }



            try
            {
               root = new File(rootURL.toURI());
            }
            catch (URISyntaxException e)
            {
               root = new File(rootURL.getPath());
            }

            if (root != null)
            {
               root = root.getParentFile().getParentFile();
            }

         }


         if (root == null || !root.exists())
         {
            throw new IllegalStateException("opends root doesn't exist: " + getServerRoot());
         }
         if (!root.isDirectory())
         {
            throw new IllegalStateException("opends root is not a directory: " + getServerRoot());
         }

         // Server root points to the directory with opends configuration
         config.setServerRoot(root);
         config.setForceDaemonThreads(true);

      }
      catch (InitializationException e)
      {
         e.printStackTrace();
      }

      return config;
   }


   public void start()
   {
      if (!EmbeddedUtils.isRunning())
      {
         try
         {
            EmbeddedUtils.startServer(getConfig());
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }

   public void stop()
   {
      if (EmbeddedUtils.isRunning())
      {
         EmbeddedUtils.stopServer(this.getClass().getName(), null);
      }
   }

   public String getServerRoot()
   {
      return serverRoot;
   }

   public void setServerRoot(String serverRoot)
   {
      this.serverRoot = serverRoot;
   }
}
