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
import org.picketlink.idm.api.query.RoleQueryBuilder;
import org.picketlink.idm.api.query.RoleQuery;
import junit.framework.Assert;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class RoleQueryTest extends Assert
{
   private APITestContext ctx;

   private String ORGANIZATION = "ORGANIZATION";
   private String ORGANIZATION_UNIT = "ORGANIZATION_UNIT";
   private String DEPARTMENT = "DEPARTMENT";

   public RoleQueryTest(APITestContext testContext)
   {
      this.ctx = testContext;
   }

   public void testQuery(String realmName) throws Exception
   {
      ctx.begin();

      IdentitySession ids = ctx.getIdentitySessionFactory().getCurrentIdentitySession(realmName);

      // Create some stuff...

      User user1 = ids.getPersistenceManager().createUser("user1");
      User user2 = ids.getPersistenceManager().createUser("user2");
      User user3 = ids.getPersistenceManager().createUser("user3");
      User user4 = ids.getPersistenceManager().createUser("user4");

      Group group1 = ids.getPersistenceManager().createGroup("group1", ORGANIZATION);
      Group group2 = ids.getPersistenceManager().createGroup("group2", ORGANIZATION_UNIT);
      Group group3 = ids.getPersistenceManager().createGroup("group3", ORGANIZATION_UNIT);
      Group group4 = ids.getPersistenceManager().createGroup("group4", DEPARTMENT);
      Group group5 = ids.getPersistenceManager().createGroup("group5", DEPARTMENT);

      RoleType rt1 = ids.getRoleManager().createRoleType("rt1");
      RoleType rt2 = ids.getRoleManager().createRoleType("rt2");
      RoleType rt3 = ids.getRoleManager().createRoleType("rt3");
      RoleType rt4 = ids.getRoleManager().createRoleType("rt4");

      Role role1 = ids.getRoleManager().createRole(rt1, user1, group1);
      Role role2 = ids.getRoleManager().createRole(rt1, user1, group2);
      Role role3 = ids.getRoleManager().createRole("rt2", user1.getKey(), group1.getKey());

      // Test Query

      //
      RoleQueryBuilder rqb = ids.createRoleQueryBuilder();

      rqb.setUser("user1");

      RoleQuery rq = rqb.createQuery();

      assertEquals(3, ids.execute(rq).size());

      //
      rqb.setGroup(group1);
      rq = rqb.createQuery();

      assertEquals(2, ids.execute(rq).size());

      //
      rqb.setRoleType(rt2);
      rq = rqb.createQuery();

      assertEquals(1, ids.execute(rq).size());


      //
      rqb.reset();

      rqb.setGroup(group2);
      rqb.setRoleType(rt1);

      rq = rqb.createQuery();

      assertEquals(1, ids.execute(rq).size());
      assertEquals(role2, ids.uniqueResult(rq));

      ctx.commit();
   }
}
