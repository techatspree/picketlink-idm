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

package org.picketlink.idm.impl.api;

import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.impl.IdentityTestPOJO;
import org.picketlink.idm.impl.configuration.IdentityConfigurationImpl;

import java.util.logging.Logger;

/**
 * Some iteration to help find possible n+1 selects in logs.
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class SimpleTestCase extends IdentityTestPOJO implements APITestContext
{
   private static Logger log = Logger.getLogger(SimpleTestCase.class.getName());


   IdentitySessionFactory identitySessionFactory;

   public void setUp() throws Exception
   {
      super.start();

      setRealmName("realm://RedHat/DB");


      identitySessionFactory = new IdentityConfigurationImpl().
         configure(getIdentityConfig()).buildIdentitySessionFactory();

      log.fine("Statistics after IdentitySessionFactory creation:");
      hibernateSupport.getSessionFactory().getStatistics().logSummary();
      
   }

   public void tearDown() throws Exception
   {
      super.stop();
   }

   public IdentitySessionFactory getIdentitySessionFactory()
   {
      return identitySessionFactory;
   }

   public void testPersistenceManager() throws Exception
   {
      IdentitySession session = identitySessionFactory.createIdentitySession(getRealmName());


      String ORGANIZATION = "ORGANIZATION";



      for (int i = 0; i < 50; i++)
      {
         log.fine("\n\n\n### Create Group: " + i + "\n");

         begin();
         session.getPersistenceManager().createGroup("test" + i, ORGANIZATION);
         commit();

      }

      for (int i = 0; i < 50; i++)
      {
         log.fine("\n\n\n### Create User: " + i + "\n");

         begin();
         session.getPersistenceManager().createUser("test" + i);
         commit();

      }

      String groupKey = session.getPersistenceManager().createGroupKey("test0", ORGANIZATION);

      for (int i = 0; i < 50; i++)
      {
         log.fine("\n\n\n### Associate User[" + i + "] to a Group"  + "\n");

         begin();
         session.getRelationshipManager().associateUserByKeys(groupKey, "test" + i);
         commit();

      }

      begin();

      groupKey = session.getPersistenceManager().createGroup("master", ORGANIZATION).getKey();

      commit();

      for (int i = 0; i < 50; i++)
      {
         log.fine("\n\n\n### Associate Group[" + i + "] of type DEPARTMENT to a Group" + groupKey + "\n");



         begin();
         String groupKey2 = session.getPersistenceManager().createGroupKey("test" + i, ORGANIZATION);
         session.getRelationshipManager().associateGroupsByKeys(groupKey, groupKey2);
         commit();

      }


      
   }

}