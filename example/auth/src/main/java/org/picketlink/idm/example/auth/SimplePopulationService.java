/*
* JBoss, a division of Red Hat
* Copyright 2009, Red Hat Middleware, LLC, and individual contributors as indicated
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


package org.picketlink.idm.example.auth;

import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Group;

import javax.naming.InitialContext;
import javax.naming.Context;
import java.util.logging.Logger;

public class SimplePopulationService
{

   private static Logger logger = Logger.getLogger(SimplePopulationService.class.getName());

   private String idmSessionFactoryJNDI = "java:/IdentitySessionFactory";

   public String getIdmSessionFactoryJNDI()
   {
      return idmSessionFactoryJNDI;
   }

   public void setIdmSessionFactoryJNDI(String idmSessionFactoryJNDI)
   {
      this.idmSessionFactoryJNDI = idmSessionFactoryJNDI;
   }

   public void start() throws Exception
   {

      logger.fine("Starting example population service");

      Context ctx = new InitialContext();
      IdentitySessionFactory ids = (IdentitySessionFactory)ctx.lookup(getIdmSessionFactoryJNDI());

      IdentitySession is = ids.getCurrentIdentitySession("realm://JBossIdentity");
      is.beginTransaction();

      if (is.getPersistenceManager().getUserCount() == 0 && is.getPersistenceManager().getGroupTypeCount("GROUP") == 0)
      {
         logger.fine("Database content not present. Populating...");


         User userUser = is.getPersistenceManager().createUser("user");
         User adminUser = is.getPersistenceManager().createUser("admin");
         Group userGroup = is.getPersistenceManager().createGroup("Users", "GROUP");
         Group adminGroup = is.getPersistenceManager().createGroup("Administrators", "GROUP"); 

         is.getAttributesManager().updatePassword(userUser, "user");
         is.getAttributesManager().updatePassword(adminUser, "admin");

         is.getRelationshipManager().associateUser(adminGroup, adminUser);
         is.getRelationshipManager().associateUser(userGroup, adminUser);
         is.getRelationshipManager().associateUser(userGroup, userUser);

      }


      is.getTransaction().commit();



   }
}
