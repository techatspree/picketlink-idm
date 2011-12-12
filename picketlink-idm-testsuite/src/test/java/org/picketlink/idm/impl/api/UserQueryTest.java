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
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.RoleType;
import org.picketlink.idm.api.Role;
import org.picketlink.idm.api.SortOrder;
import org.picketlink.idm.api.query.UserQuery;
import org.picketlink.idm.api.query.UserQueryBuilder;
import junit.framework.Assert;

import java.util.List;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class UserQueryTest extends Assert
{
   private APITestContext ctx;

   private String ORGANIZATION = "ORGANIZATION";

   private String ORGANIZATION_UNIT = "ORGANIZATION_UNIT";

   private String DEPARTMENT = "DEPARTMENT";

   public UserQueryTest(APITestContext testContext)
   {
      this.ctx = testContext;
   }

   public void testQuery(String realmName) throws Exception
   {
      ctx.begin();

      IdentitySession ids = ctx.getIdentitySessionFactory().getCurrentIdentitySession(realmName);

      // Create stuff

      User user1 = ids.getPersistenceManager().createUser("user1");
      User user2 = ids.getPersistenceManager().createUser("user2");
      User user3 = ids.getPersistenceManager().createUser("user3");
      User user4 = ids.getPersistenceManager().createUser("user4");
      User testUser1 = ids.getPersistenceManager().createUser("testUser1");
      User testUser2 = ids.getPersistenceManager().createUser("testUser2");
      User testUser3 = ids.getPersistenceManager().createUser("testUser3");
      User testUser4 = ids.getPersistenceManager().createUser("testUser4");

      Group group1 = ids.getPersistenceManager().createGroup("group1", ORGANIZATION);
      Group group2 = ids.getPersistenceManager().createGroup("group2", ORGANIZATION_UNIT);
      Group group3 = ids.getPersistenceManager().createGroup("group3", ORGANIZATION_UNIT);
      Group group4 = ids.getPersistenceManager().createGroup("group4", DEPARTMENT);
      Group group5 = ids.getPersistenceManager().createGroup("group5", DEPARTMENT);

      ids.getRelationshipManager().associateUser(group1, user1);
      ids.getRelationshipManager().associateUser(group1, user2);

      RoleType rt1 = ids.getRoleManager().createRoleType("rt1");
      RoleType rt2 = ids.getRoleManager().createRoleType("rt2");
      RoleType rt3 = ids.getRoleManager().createRoleType("rt3");
      RoleType rt4 = ids.getRoleManager().createRoleType("rt4");

      Role role1 = ids.getRoleManager().createRole(rt1, user1, group1);
      Role role2 = ids.getRoleManager().createRole(rt1, user1, group2);
      Role role3 = ids.getRoleManager().createRole("rt2", user1.getKey(), group1.getKey());

      // Asserts

      //
      UserQueryBuilder qb = ids.createUserQueryBuilder();

      qb.withUserId("user1");
      UserQuery q = qb.createQuery();

      assertEquals(1, ids.execute(q).size());
      assertEquals(1, ids.list(q).size());
      assertEquals("user1", ids.uniqueResult(q).getKey());

      //
      qb.reset();

      qb.addAssociatedGroup(group2);

      assertEquals(0, ids.execute(qb.createQuery()).size());

      //
      qb.reset();

      qb.addAssociatedGroup(group1);

      assertEquals(2, ids.execute(qb.createQuery()).size());

      //
      qb.reset();

      qb.addAssociatedGroup(group1);
      qb.withUserId(user1.getKey());

      assertEquals(1, ids.execute(qb.createQuery()).size());

      //
      qb.reset();

      qb.addAssociatedGroup(group2);
      qb.withUserId(user1.getKey());

      assertEquals(0, ids.execute(qb.createQuery()).size());

      //
      qb.reset();

      qb.addAssociatedGroup(group1);
      qb.addGroupConnectedWithRole(group2);

      assertEquals(1, ids.execute(qb.createQuery()).size());
      assertEquals(user1, ids.uniqueResult(qb.createQuery()));

      //
      qb.reset();

      qb.addRelatedGroup(group1);

      assertEquals(2, ids.execute(qb.createQuery()).size());

      //
      qb.reset();

      qb.addRelatedGroup(group2);
      qb.addAssociatedGroup(group1);

      assertEquals(1, ids.execute(qb.createQuery()).size());
      assertEquals(user1, ids.uniqueResult(qb.createQuery()));

      //
      qb.reset();

      assertEquals(8, ids.execute(qb.createQuery()).size());


      // Criteria

      // All users
      qb.reset();

      assertEquals(8, ids.execute(qb.createQuery()).size());

      // All users sorted
      qb.reset();
      qb.sort(SortOrder.ASCENDING);

      List<User> results = ids.list(qb.createQuery());

      assertEquals(testUser1, results.get(0));
      assertEquals(user4, results.get(7));

      // All users sorted desc
      qb.reset();
      qb.sort(SortOrder.DESCENDING);

      results = ids.list(qb.createQuery());

      assertEquals(testUser1, results.get(7));
      assertEquals(user4, results.get(0));

      //
      qb.reset();
      qb.sort(SortOrder.DESCENDING);
      qb.idFilter("test*");

      results = ids.list(qb.createQuery());

      assertEquals(4, results.size());
      assertEquals(testUser1, results.get(3));
      assertEquals(testUser4, results.get(0));

      //
      qb.reset();
      qb.sort(SortOrder.ASCENDING);
      qb.idFilter("user*");
      qb.page(1, 2);
      results = ids.list(qb.createQuery());

      assertEquals(2, results.size());
      assertEquals(user2, results.get(0));
      assertEquals(user3, results.get(1));

      ids.getAttributesManager().addAttribute(user4, "lastName", "gtn");
      ids.getAttributesManager().addAttribute(user3, "email", "user3@localhost");

      //
      qb.reset();
      qb.attributeValuesFilter("lastName", new String[] {"*gtn*"});
      results = ids.list(qb.createQuery());
      assertEquals(1, results.size());
      assertEquals(user4, results.get(0));

      //
      qb.reset();
      qb.attributeValuesFilter("email", new String[] {"*user3*"});
      results = ids.list(qb.createQuery());
      assertEquals(1, results.size());
      assertEquals(user3, results.get(0));



      ctx.commit();
   }



}
