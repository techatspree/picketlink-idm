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

import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;

/**
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class RelationshipManagerTest extends Assert
{
   //TODO:
   //TODO: - inheritence in find methods
   //TODO: - creating forbidden associations
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

   public RelationshipManagerTest(APITestContext ctx)
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

      // Create stuff
      User user1 = session.getPersistenceManager().createUser("user1");
      User user2 = session.getPersistenceManager().createUser("user2");
      User user3 = session.getPersistenceManager().createUser("user3");
      User user4 = session.getPersistenceManager().createUser("user4");

      Group group1 = session.getPersistenceManager().createGroup("group1", ORGANIZATION);
      Group group2 = session.getPersistenceManager().createGroup("group2", ORGANIZATION_UNIT);
      Group group3 = session.getPersistenceManager().createGroup("group3", ORGANIZATION_UNIT);
      Group group4 = session.getPersistenceManager().createGroup("group4", DEPARTMENT);
      Group group5 = session.getPersistenceManager().createGroup("group5", DEPARTMENT);


      // Associate groups

      session.getRelationshipManager().associateGroups(group1, group2);
      session.getRelationshipManager().associateGroups(group1, group3);

      // Assert

      // isAssociated

      assertTrue(session.getRelationshipManager().isAssociated(group1, group2));
      assertTrue(session.getRelationshipManager().isAssociated(group1, group3));
      assertFalse(session.getRelationshipManager().isAssociated(group1, group4));
      assertFalse(session.getRelationshipManager().isAssociated(group2, group1));
      assertFalse(session.getRelationshipManager().isAssociated(group2, group3));

      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group1), Arrays.asList(group2, group3)));
      assertFalse(session.getRelationshipManager().isAssociated(Arrays.asList(group1, group2), Arrays.asList(group2, group3)));
      assertFalse(session.getRelationshipManager().isAssociated(Arrays.asList(group2, group3), Arrays.asList(group1)));

      assertTrue(session.getRelationshipManager().isAssociatedByKeys(group1.getKey(), group2.getKey()));
      assertTrue(session.getRelationshipManager().isAssociatedByKeys(group1.getKey(), group3.getKey()));
      assertFalse(session.getRelationshipManager().isAssociatedByKeys(group1.getKey(), group4.getKey()));
      assertFalse(session.getRelationshipManager().isAssociatedByKeys(group2.getKey(), group1.getKey()));
      assertFalse(session.getRelationshipManager().isAssociatedByKeys(group2.getKey(), group3.getKey()));
      
      assertTrue(session.getRelationshipManager().
         isAssociatedByKeys(Arrays.asList(group1.getKey()), Arrays.asList(group2.getKey(), group3.getKey())));
      assertFalse(session.getRelationshipManager().
         isAssociatedByKeys(Arrays.asList(group1.getKey(), group2.getKey()), Arrays.asList(group2.getKey(), group3.getKey())));
      assertFalse(session.getRelationshipManager().
         isAssociatedByKeys(Arrays.asList(group2.getKey(), group3.getKey()), Arrays.asList(group1.getKey())));


      // Associate groups - other methods

      // First disassociate and assert
      session.getRelationshipManager().disassociateGroups(Arrays.asList(group1), Arrays.asList(group2, group3));

      assertFalse(session.getRelationshipManager().isAssociated(group1, group2));
      assertFalse(session.getRelationshipManager().isAssociated(group1, group3));

      // reassociate #1
      session.getRelationshipManager().associateGroups(Arrays.asList(group1), Arrays.asList(group2, group3));
      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group1), Arrays.asList(group2, group3)));

      session.getRelationshipManager().disassociateGroupsByKeys(Arrays.asList(group1.getKey()), Arrays.asList(group2.getKey(), group3.getKey()));

      // reassociate #2
      session.getRelationshipManager().associateGroupsByKeys(Arrays.asList(group1.getKey()), Arrays.asList(group2.getKey(), group3.getKey()));
      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group1), Arrays.asList(group2, group3)));

      session.getRelationshipManager().disassociateGroups(Arrays.asList(group1), Arrays.asList(group2, group3));

      // reassociate #3
      session.getRelationshipManager().associateGroupsByKeys(group1.getKey(), group2.getKey());
      session.getRelationshipManager().associateGroupsByKeys(group1.getKey(), group3.getKey());
      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group1), Arrays.asList(group2, group3)));

      session.getRelationshipManager().disassociateGroups(Arrays.asList(group1), Arrays.asList(group2, group3));


      // Associate Group and Users

      session.getRelationshipManager().associateUser(group1, user1);
      session.getRelationshipManager().associateUser(group1, user2);

      // isAssociated

      assertTrue(session.getRelationshipManager().isAssociated(group1, user1));
      assertTrue(session.getRelationshipManager().isAssociated(group1, user2));
      assertFalse(session.getRelationshipManager().isAssociated(group1, user3));
      assertFalse(session.getRelationshipManager().isAssociated(group2, user1));
      assertFalse(session.getRelationshipManager().isAssociated(group2, user3));

      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group1), Arrays.asList(user1, user2)));
      assertFalse(session.getRelationshipManager().isAssociated(Arrays.asList(group1, group2), Arrays.asList(user1, user2)));
      assertFalse(session.getRelationshipManager().isAssociated(Arrays.asList(group2), Arrays.asList(user3)));

      assertTrue(session.getRelationshipManager().isAssociatedByKeys(group1.getKey(), user1.getKey()));
      assertTrue(session.getRelationshipManager().isAssociatedByKeys(group1.getKey(), user2.getKey()));
      assertFalse(session.getRelationshipManager().isAssociatedByKeys(group1.getKey(), user3.getKey()));
      assertFalse(session.getRelationshipManager().isAssociatedByKeys(group2.getKey(), user1.getKey()));
      assertFalse(session.getRelationshipManager().isAssociatedByKeys(group2.getKey(), user3.getKey()));

      assertTrue(session.getRelationshipManager().
         isAssociatedByKeys(Arrays.asList(group1.getKey()), Arrays.asList(user1.getKey(), user2.getKey())));
      assertFalse(session.getRelationshipManager().
         isAssociatedByKeys(Arrays.asList(group1.getKey(), group2.getKey()), Arrays.asList(user1.getKey(), user2.getKey())));
      assertFalse(session.getRelationshipManager().
         isAssociatedByKeys(Arrays.asList(group2.getKey(), group3.getKey()), Arrays.asList(user1.getKey())));


      // Associate groups / users - other methods

      // First disassociate and assert
      session.getRelationshipManager().disassociateUsers(Arrays.asList(group1), Arrays.asList(user1, user2));

      assertFalse(session.getRelationshipManager().isAssociated(group1, user1));
      assertFalse(session.getRelationshipManager().isAssociated(group1, user2));

      // reassociate #1
      session.getRelationshipManager().associateUsers(Arrays.asList(group1), Arrays.asList(user1, user2));
      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group1), Arrays.asList(user1, user2)));

      session.getRelationshipManager().disassociateUsers(Arrays.asList(group1), Arrays.asList(user1, user2));

      // reassociate #2
      session.getRelationshipManager().associateUsersByKeys(Arrays.asList(group1.getKey()), Arrays.asList(user1.getKey(), user2.getKey()));
      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group1), Arrays.asList(user1, user2)));

      session.getRelationshipManager().disassociateUsersByKeys(Arrays.asList(group1.getKey()), Arrays.asList(user1.getKey(), user2.getKey()));

      // reassociate #3
      session.getRelationshipManager().associateUserByKeys(group1.getKey(), user1.getKey());
      session.getRelationshipManager().associateUserByKeys(group1.getKey(), user2.getKey());

      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group1), Arrays.asList(user1, user2)));

      session.getRelationshipManager().disassociateUsers(Arrays.asList(group1), Arrays.asList(user1, user2));

      // reassociate #4
      session.getRelationshipManager().associateUserByKeys(group1.getKey(), user1.getKey());
      session.getRelationshipManager().associateUserByKeys(group2.getKey(), user1.getKey());
      session.getRelationshipManager().associateUserByKeys(group3.getKey(), user1.getKey());

      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group1, group2, group3), Arrays.asList(user1)));

      session.getRelationshipManager().disassociateGroups(user1);

      assertFalse(session.getRelationshipManager().isAssociated(group1, user1));
      assertFalse(session.getRelationshipManager().isAssociated(group2, user1));
      assertFalse(session.getRelationshipManager().isAssociated(group3, user1));
      assertFalse(session.getRelationshipManager().isAssociated(Arrays.asList(group1, group2, group3), Arrays.asList(user1)));

      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(user1).size());



      ctx.commit();

      ctx.begin();


      // Find methods - prepopulate

      session.getRelationshipManager().associateUsers(Arrays.asList(group1), Arrays.asList(user1, user2, user3));
      session.getRelationshipManager().associateUsers(Arrays.asList(group3), Arrays.asList(user2, user3));
      session.getRelationshipManager().associateGroups(Arrays.asList(group2), Arrays.asList(group3, group4, group5));

      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group1), Arrays.asList(user1, user2, user3)));
      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group2), Arrays.asList(group3, group4, group5)));
      assertTrue(session.getRelationshipManager().isAssociated(Arrays.asList(group3), Arrays.asList(user2, user3)));

      ctx.commit();
      ctx.begin();

      // Find users
      //TODO: check if collections contain correct objects (not only size)

      assertEquals(3, session.getRelationshipManager().findAssociatedUsers(group1, false).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedUsers(group2, false).size());

      assertEquals(3, session.getRelationshipManager().findAssociatedUsers(group1, false, null).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedUsers(group2, false, null).size());

      assertEquals(3, session.getRelationshipManager().findAssociatedUsers(group1.getKey(), false, null).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedUsers(group2.getKey(), false, null).size());

      // Find groups

      // #1
      assertEquals(3, session.getRelationshipManager().findAssociatedGroups(group2, null, true, false).size());
      assertEquals(2, session.getRelationshipManager().findAssociatedGroups(group2, DEPARTMENT, true, false).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(group2, ORGANIZATION_UNIT, true, false).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(group2, ORGANIZATION, true, false).size());

      // #1
      assertEquals(2, session.getRelationshipManager().findAssociatedGroups(group2.getKey(), DEPARTMENT, true, false, null).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(group2.getKey(), ORGANIZATION_UNIT, true, false, null).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(group2.getKey(), ORGANIZATION, true, false, null).size());

      // #3
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(group3, ORGANIZATION_UNIT, false, false).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(group4, ORGANIZATION_UNIT, false, false).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(group2, ORGANIZATION, false, false).size());
      assertEquals(3, session.getRelationshipManager().findAssociatedGroups(group2, null, true, false).size());

      // #4
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(group3.getKey(), ORGANIZATION_UNIT, false, false, null).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(group4.getKey(), ORGANIZATION_UNIT, false, false, null).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(group2.getKey(), ORGANIZATION, false, false, null).size());

      // #5
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user1).size());
      assertEquals(2, session.getRelationshipManager().findAssociatedGroups(user2).size());
      assertEquals(2, session.getRelationshipManager().findAssociatedGroups(user3).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(user4).size());

      // #6
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user1.getKey(), null).size());
      assertEquals(2, session.getRelationshipManager().findAssociatedGroups(user2.getKey(), null).size());
      assertEquals(2, session.getRelationshipManager().findAssociatedGroups(user3.getKey(), null).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(user4.getKey(), null).size());

      // #7
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user1, ORGANIZATION).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(user1, ORGANIZATION_UNIT).size());
      assertEquals(2, session.getRelationshipManager().findAssociatedGroups(user2, (String)null).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user2, ORGANIZATION).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user2, ORGANIZATION_UNIT).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user3, ORGANIZATION).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user3, ORGANIZATION_UNIT).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(user4, ORGANIZATION).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(user4, ORGANIZATION_UNIT).size());

      // #7
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user1.getKey(), ORGANIZATION, null).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(user1.getKey(), ORGANIZATION_UNIT, null).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user2.getKey(), ORGANIZATION, null).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user2.getKey(), ORGANIZATION_UNIT, null).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user3.getKey(), ORGANIZATION, null).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user3.getKey(), ORGANIZATION_UNIT, null).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(user4.getKey(), ORGANIZATION, null).size());
      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(user4.getKey(), ORGANIZATION_UNIT, null).size());

      // Cleanup and check
      session.getRelationshipManager().disassociateUsers(Arrays.asList(group1), Arrays.asList(user1, user2, user3));
      session.getRelationshipManager().disassociateUsers(Arrays.asList(group3), Arrays.asList(user2, user3));
      session.getRelationshipManager().disassociateGroups(Arrays.asList(group2), Arrays.asList(group3, group4));

      assertFalse(session.getRelationshipManager().isAssociated(Arrays.asList(group1), Arrays.asList(user1, user2, user3)));
      assertFalse(session.getRelationshipManager().isAssociated(Arrays.asList(group2), Arrays.asList(group3, group4, group5)));
      assertFalse(session.getRelationshipManager().isAssociated(Arrays.asList(group3), Arrays.asList(user2, user3)));

      ctx.commit();

   }

   public void testCascade(String realmName) throws Exception
   {

      IdentitySessionFactory factory = ctx.getIdentitySessionFactory();

      IdentitySession session = factory.createIdentitySession(realmName);

      ctx.begin();

      // Create stuff
      User user1 = session.getPersistenceManager().createUser("user1");
      User user2 = session.getPersistenceManager().createUser("user2");
      User user3 = session.getPersistenceManager().createUser("user3");
      User user4 = session.getPersistenceManager().createUser("user4");
      User user5 = session.getPersistenceManager().createUser("user5");
      User user6 = session.getPersistenceManager().createUser("user6");
      User user7 = session.getPersistenceManager().createUser("user7");

      Group group1 = session.getPersistenceManager().createGroup("group1", ORGANIZATION);
      Group group2 = session.getPersistenceManager().createGroup("group2", ORGANIZATION);
      Group group3 = session.getPersistenceManager().createGroup("group3", ORGANIZATION);
      Group group4 = session.getPersistenceManager().createGroup("group4", ORGANIZATION);
      Group group5 = session.getPersistenceManager().createGroup("group5", ORGANIZATION);


      // Create looped associations
      session.getRelationshipManager().associateGroups(group1, group2);
      session.getRelationshipManager().associateGroups(group1, group3);
      session.getRelationshipManager().associateGroups(group2, group4);
      session.getRelationshipManager().associateGroups(group2, group5);
      session.getRelationshipManager().associateGroups(group3, group1);

      // Assign users
      session.getRelationshipManager().associateUser(group1, user1);
      session.getRelationshipManager().associateUser(group2, user2);
      session.getRelationshipManager().associateUser(group3, user3);
      session.getRelationshipManager().associateUser(group5, user4);
      session.getRelationshipManager().associateUser(group5, user5);


      // Make sure that algorithm doesn't go in a loop
      Collection<Group> results = session.getRelationshipManager().
         findAssociatedGroups(group1, ORGANIZATION, true, true);

      assertEquals(4, results.size());
      assertFalse(results.contains(group1));
      assertTrue(results.contains(group2));
      assertTrue(results.contains(group3));
      assertTrue(results.contains(group4));
      assertTrue(results.contains(group5));


      // And get all users associated in a tree
      Collection<User> results2 = session.getRelationshipManager().
         findAssociatedUsers(group1, true);

      assertEquals(5, results2.size());
      assertFalse(results2.contains(user6));
      assertFalse(results2.contains(user7));
      assertTrue(results2.contains(user1));
      assertTrue(results2.contains(user2));
      assertTrue(results2.contains(user3));
      assertTrue(results2.contains(user4));
      assertTrue(results2.contains(user5));

   }

   public void testMergedRoleAssociations(String realmName) throws Exception
   {
      IdentitySessionFactory factory = ctx.getIdentitySessionFactory();

      IdentitySession session = factory.createIdentitySession(realmName);

      ctx.begin();

      // Create stuff
      User user1 = session.getPersistenceManager().createUser("user1");
      User user2 = session.getPersistenceManager().createUser("user2");
      User user3 = session.getPersistenceManager().createUser("user3");
      User user4 = session.getPersistenceManager().createUser("user4");

      Group group1 = session.getPersistenceManager().createGroup("group1", ORGANIZATION);
      Group group2 = session.getPersistenceManager().createGroup("group2", ORGANIZATION_UNIT);
      Group group3 = session.getPersistenceManager().createGroup("group3", ORGANIZATION_UNIT);
      Group group4 = session.getPersistenceManager().createGroup("group4", DEPARTMENT);
      Group group5 = session.getPersistenceManager().createGroup("group5", DEPARTMENT);

      // Associate

      session.getRelationshipManager().associateGroups(group1, group2);
      session.getRelationshipManager().associateGroups(group1, group3);
      session.getRelationshipManager().associateUser(group1, user1);
      session.getRelationshipManager().associateUser(group1, user2);

      session.getRoleManager().createRoleType("rt1");
      session.getRoleManager().createRoleType("rt2");

      session.getRoleManager().createRole("rt1", user1.getKey(), group1.getKey());
      session.getRoleManager().createRole("rt1", user3.getKey(), group1.getKey());
      session.getRoleManager().createRole("rt1", user4.getKey(), group1.getKey());
      session.getRoleManager().createRole("rt2", user3.getKey(), group1.getKey());

      session.getRoleManager().createRole("rt1", user3.getKey(), group2.getKey());
      session.getRoleManager().createRole("rt2", user2.getKey(), group3.getKey());

      // Assert

      assertEquals(2, session.getRelationshipManager().findRelatedGroups(user2, null, null).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user2, (IdentitySearchCriteria)null).size());

      assertEquals(2, session.getRelationshipManager().findAssociatedUsers(group1, false, null).size());
      assertEquals(4, session.getRelationshipManager().findRelatedUsers(group1, null).size());

      assertEquals(0, session.getRelationshipManager().findAssociatedUsers(group2, false, null).size());
      assertEquals(1, session.getRelationshipManager().findRelatedUsers(group2, null).size());

      assertEquals(0, session.getRelationshipManager().findAssociatedGroups(user3, (IdentitySearchCriteria)null).size());
      assertEquals(2, session.getRelationshipManager().findRelatedGroups(user3, null, null).size());

      assertEquals(1, session.getRelationshipManager().findAssociatedGroups(user2, (IdentitySearchCriteria)null).size());
      assertEquals(2, session.getRelationshipManager().findRelatedGroups(user2, null, null).size());

      ctx.commit();

   }
}