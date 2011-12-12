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

import java.util.logging.Logger;
import java.util.Random;
import java.util.Map;
import java.util.Arrays;
import java.util.Collection;
import java.io.File;

import org.picketlink.idm.api.IdentitySearchCriteria;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.RoleType;
import org.picketlink.idm.api.AttributeDescription;
import org.picketlink.idm.api.Attribute;
import org.picketlink.idm.api.User;
import org.picketlink.idm.impl.api.IdentitySessionFactoryImpl;
import org.picketlink.idm.impl.api.SimpleAttribute;
import org.picketlink.idm.impl.configuration.IdentityConfigurationImpl;


/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class DBTestCase extends TestBase
{

   private static Logger logger = Logger.getLogger(DBTestCase.class.getName());

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();

      startDatabase();
   }

   @Override
   protected void tearDown() throws Exception
   {
      super.tearDown();

      stopDatabase();
   }


   public void testJBossIdentity() throws Exception
   {
      IdentitySessionFactory identitySessionFactory = new IdentityConfigurationImpl().
         configure(new File("src/test/resources/example-db-config.xml")).buildIdentitySessionFactory();

      IdentitySession identitySession = identitySessionFactory.createIdentitySession("realm://JBossIdentityExample/SampleRealm");
      identitySession.beginTransaction();

      Collection<User> users = identitySession
         .getPersistenceManager().findUser((IdentitySearchCriteria)null);

      String ORGANIZATION = "ORGANIZATION";
      String GROUP = "GROUP";

      User johnDoe = identitySession.getPersistenceManager().createUser("John Doe");
      User alice = identitySession.getPersistenceManager().createUser("Alice");
      User eva = identitySession.getPersistenceManager().createUser("Eva");
      
      identitySession.getAttributesManager().updatePassword(eva, "oldPassword");
      identitySession.getAttributesManager().updatePassword(eva, "newPassword");

      Group acmeOrg = identitySession.getPersistenceManager().createGroup("ACME", ORGANIZATION);

      Group itGroup = identitySession.getPersistenceManager().createGroup("IT", GROUP);
      Group hrGroup = identitySession.getPersistenceManager().createGroup("HR", GROUP);

      identitySession.getRelationshipManager().associateGroups(acmeOrg, itGroup);
      identitySession.getRelationshipManager().associateGroups(acmeOrg, hrGroup);

      identitySession.getRelationshipManager().associateUser(itGroup, johnDoe);
      identitySession.getRelationshipManager().associateUser(itGroup, alice);

      identitySession.getRelationshipManager().associateUser(hrGroup, eva);

      RoleType managerRT = identitySession.getRoleManager().createRoleType("manager");

      identitySession.getRoleManager().createRole(managerRT, johnDoe, itGroup);

      // John belongs to IT and not HR
      assertTrue(identitySession.getRelationshipManager().isAssociated(itGroup, johnDoe));
      assertFalse(identitySession.getRelationshipManager().isAssociated(hrGroup, johnDoe));

      // John is manager of IT and not HR
      assertTrue(identitySession.getRoleManager().hasRole(johnDoe, itGroup, managerRT));
      assertFalse(identitySession.getRoleManager().hasRole(johnDoe, hrGroup, managerRT));

      // Check that binary attribute picture is mapped

      AttributeDescription attributeDescription = identitySession.getAttributesManager().getAttributeDescription(johnDoe, "picture");
      assertNotNull(attributeDescription);
      assertEquals("binary", attributeDescription.getType());


      // Generate random binary data for binary attribute
      Random random = new Random();

      byte[] picture = new byte[5120];
      random.nextBytes(picture);

      identitySession.getAttributesManager().addAttributes(johnDoe, new Attribute[] {new SimpleAttribute("picture", new byte[][]{picture})});
      identitySession.getAttributesManager().addAttributes(johnDoe, new Attribute[] {new SimpleAttribute("emplyer", new String[]{"ACME1", "ACME2"})});
      identitySession.getAttributesManager().addAttributes(johnDoe, new Attribute[] {new SimpleAttribute("hobby", new String[]{"BASE Jumping"})});

      // Assert picture

      Map<String, Attribute> attributes = identitySession.getAttributesManager().getAttributes(johnDoe);
      assertEquals(3, attributes.keySet().size());
      assertTrue(Arrays.equals((byte[])attributes.get("picture").getValue(), picture));


      User xUser = identitySession.getPersistenceManager().createUser("x");
      Group someGroup = identitySession.getPersistenceManager().createGroup("someGroup", GROUP);

      identitySession.getRoleManager().createRole(managerRT, xUser, someGroup);

      assertEquals(0, identitySession.getRelationshipManager().findAssociatedGroups(xUser, GROUP).size());
      assertEquals(1, identitySession.getRoleManager().findGroupsWithRelatedRole(xUser, GROUP, null).size());
      assertEquals(1, identitySession.getRoleManager().findGroupsWithRelatedRole(xUser, null).size());

      Group otherGroup = identitySession.getPersistenceManager().createGroup("otherGroup", GROUP);

      identitySession.getRelationshipManager().associateUser(otherGroup, xUser);

      assertEquals(1, identitySession.getRelationshipManager().findAssociatedGroups(xUser, GROUP).size());
      assertEquals(1, identitySession.getRoleManager().findGroupsWithRelatedRole(xUser, GROUP, null).size());
      assertEquals(1, identitySession.getRoleManager().findGroupsWithRelatedRole(xUser, null).size());


      identitySession.getTransaction().commit();
      identitySession.close();


   }

}
