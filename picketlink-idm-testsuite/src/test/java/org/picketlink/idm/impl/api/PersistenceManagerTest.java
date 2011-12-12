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

import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.IdentitySearchCriteria;

import junit.framework.Assert;
import org.picketlink.idm.impl.api.model.GroupKey;

/**
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class PersistenceManagerTest extends Assert
{
   //TODO:
   //TODO: - force in removeGroup (NYI)
   //TODO: - exception handling
   //TODO: - search criteria
   //TODO: - features description

   APITestContext ctx;

   private String ORGANIZATION = "ORGANIZATION";
   private String ORGANIZATION_UNIT = "ORGANIZATION_UNIT";
   private String DIVISION = "DIVISION";
   private String DEPARTMENT = "DEPARTMENT";
   private String PROJECT = "PROJECT";
   private String PEOPLE = "PEOPLE";

   public PersistenceManagerTest(APITestContext ctx)
   {
      this.ctx = ctx;
   }

   public void setCtx(APITestContext ctx)
   {
      this.ctx = ctx;
   }

   public APITestContext getCtx()
   {
      return ctx;
   }

   public void testMethods(String realmName) throws Exception
   {
      IdentitySessionFactory factory = ctx.getIdentitySessionFactory();

      IdentitySession session = factory.createIdentitySession(realmName);



      ctx.begin();

      // Bad names

      try
      {
         session.getPersistenceManager().createUser("lol" + GroupKey.SEPARATOR + "lolo");
         fail();
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }

      try
      {
         session.getPersistenceManager().createGroup(GroupKey.PREFIX + "toto", ORGANIZATION);
         fail();
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }


      // Create / Remove

      // Group
      Group group1 = session.getPersistenceManager().createGroup("groupName1", ORGANIZATION);

      assertNotNull(group1);
      assertEquals(group1.getGroupType(), ORGANIZATION);
      assertEquals(group1.getName(), "groupName1");

      String group1Id = session.getPersistenceManager().createGroupKey("groupName1", ORGANIZATION);

      assertEquals(group1Id, group1.getKey());

      group1 = session.getPersistenceManager().findGroupByKey(group1Id);

      assertNotNull(group1);
      assertEquals(group1.getGroupType(), ORGANIZATION);
      assertEquals(group1.getName(), "groupName1");

      session.getPersistenceManager().createGroup("groupName2", ORGANIZATION);
      session.getPersistenceManager().createGroup("groupName3", PROJECT);

      assertEquals(2, session.getPersistenceManager().getGroupTypeCount(ORGANIZATION));
      assertEquals(1, session.getPersistenceManager().getGroupTypeCount(PROJECT));
      assertEquals(0, session.getPersistenceManager().getGroupTypeCount(DEPARTMENT));

      assertEquals(2, session.getPersistenceManager().findGroup(ORGANIZATION).size());
      assertEquals(1, session.getPersistenceManager().findGroup(PROJECT).size());
      assertEquals(0, session.getPersistenceManager().findGroup(DEPARTMENT).size());

      session.getPersistenceManager().removeGroup(group1, false);
      String id = session.getPersistenceManager().createGroupKey("groupName2", ORGANIZATION);
      session.getPersistenceManager().removeGroup(id, false);
      id = session.getPersistenceManager().createGroupKey("groupName3", PROJECT);
      session.getPersistenceManager().removeGroup(id, false);

      assertEquals(0, session.getPersistenceManager().findGroup(ORGANIZATION, (IdentitySearchCriteria)null).size());
      assertEquals(0, session.getPersistenceManager().findGroup(PROJECT).size());
      assertEquals(0, session.getPersistenceManager().findGroup(DEPARTMENT).size());

      assertEquals(0, session.getPersistenceManager().getGroupTypeCount(ORGANIZATION));
      assertEquals(0, session.getPersistenceManager().getGroupTypeCount(PROJECT));
      assertEquals(0, session.getPersistenceManager().getGroupTypeCount(DEPARTMENT));


      //User

      User u1 = session.getPersistenceManager().createUser("olo");

      assertNotNull(u1);
      assertEquals(u1.getKey(), "olo");

      session.getPersistenceManager().createUser("anna");

      assertEquals(2, session.getPersistenceManager().getUserCount());

      assertNotNull(session.getPersistenceManager().findUser("olo"));
      assertNotNull(session.getPersistenceManager().findUser("anna"));

      //TODO: this should throw some meaningfull exception or simply return null
      //assertNull(session.getPersistenceManager().findUser("olaf"));

      assertEquals(2, session.getPersistenceManager().findUser((IdentitySearchCriteria)null).size());

      session.getPersistenceManager().removeUser(u1, false);

      assertEquals(1, session.getPersistenceManager().getUserCount());

      session.getPersistenceManager().removeUser("anna", false);

      assertEquals(0, session.getPersistenceManager().getUserCount());

      assertEquals(0, session.getPersistenceManager().findUser((IdentitySearchCriteria)null).size());


      ctx.commit();

   }



}