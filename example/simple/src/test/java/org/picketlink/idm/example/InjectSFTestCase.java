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

import org.picketlink.idm.api.IdentitySearchCriteria;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Attribute;
import org.picketlink.idm.api.AttributeDescription;
import org.picketlink.idm.api.cfg.IdentityConfiguration;
import org.picketlink.idm.impl.configuration.IdentityConfigurationImpl;
import org.picketlink.idm.impl.api.SimpleAttribute;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Logger;
import java.util.Collection;
import java.util.Random;
import java.util.Map;
import java.util.Arrays;
import java.io.File;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class InjectSFTestCase extends TestBase
{
   private static Logger logger = Logger.getLogger(InjectSFTestCase.class.getName());

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


      public void testInjectSessionFactory() throws Exception
      {
         IdentityConfiguration identityConfiguration = new IdentityConfigurationImpl().
            configure(new File("src/test/resources/example-db-inject-sf-config.xml"));

         SessionFactory hibernateSessionFactory = new Configuration().configure("hibernate-jboss-identity-classes.cfg.xml")
            .buildSessionFactory();

         identityConfiguration.getIdentityConfigurationRegistry().register(hibernateSessionFactory, "registeredSessionFactory1");

         IdentitySessionFactory identitySessionFactory = identityConfiguration.buildIdentitySessionFactory();

         IdentitySession identitySession = identitySessionFactory.createIdentitySession("realm://JBossIdentityExample/SampleRealm");
         identitySession.beginTransaction();

         Collection<User> users = identitySession
            .getPersistenceManager().findUser((IdentitySearchCriteria)null);

         String ORGANIZATION = "ORGANIZATION";
         String GROUP = "GROUP";

         // Use username nad group ids instead of objects

         String johnDoeUser = identitySession.getPersistenceManager().createUser("John Doe").getKey();
         String aliceUser = identitySession.getPersistenceManager().createUser("Alice").getKey();
         String evaUser = identitySession.getPersistenceManager().createUser("Eva").getKey();

         String acmeOrgId = identitySession.getPersistenceManager().createGroup("ACME", ORGANIZATION).getKey();

         String itGroupId = identitySession.getPersistenceManager().createGroup("IT", GROUP).getKey();
         String hrGroupId = identitySession.getPersistenceManager().createGroup("HR", GROUP).getKey();

         identitySession.getRelationshipManager().associateGroupsByKeys(acmeOrgId, itGroupId);
         identitySession.getRelationshipManager().associateGroupsByKeys(acmeOrgId, hrGroupId);

         identitySession.getRelationshipManager().associateUserByKeys(itGroupId, johnDoeUser);
         identitySession.getRelationshipManager().associateUserByKeys(itGroupId, aliceUser);

         identitySession.getRelationshipManager().associateUserByKeys(hrGroupId, evaUser);

         identitySession.getRoleManager().createRoleType("manager");

         identitySession.getRoleManager().createRole("manager", johnDoeUser, itGroupId);

         // John belongs to IT and not HR
         assertTrue(identitySession.getRelationshipManager().isAssociatedByKeys(itGroupId, johnDoeUser));
         assertFalse(identitySession.getRelationshipManager().isAssociatedByKeys(hrGroupId, johnDoeUser));

         // John is manager of IT and not HR
         assertTrue(identitySession.getRoleManager().hasRole(johnDoeUser, itGroupId, "manager"));
         assertFalse(identitySession.getRoleManager().hasRole(johnDoeUser, hrGroupId, "manager"));

//          Check that binary attribute picture is mapped
//
         AttributeDescription attributeDescription = identitySession.getAttributesManager().getAttributeDescription(johnDoeUser, "picture");
         assertNotNull(attributeDescription);
         assertEquals("binary", attributeDescription.getType());


         // Generate random binary data for binary attribute
         Random random = new Random();

         byte[] picture = new byte[5120];
         random.nextBytes(picture);

         identitySession.getAttributesManager().addAttributes(johnDoeUser, new Attribute[] {new SimpleAttribute("picture", new byte[][]{picture})});

         // Assert picture

         Map<String, Attribute> attributes = identitySession.getAttributesManager().getAttributes(johnDoeUser);
         assertEquals(1, attributes.keySet().size());
         assertTrue(Arrays.equals((byte[])attributes.get("picture").getValue(), picture));


         String xUser = identitySession.getPersistenceManager().createUser("x").getKey();
         String someGroupId = identitySession.getPersistenceManager().createGroup("someGroup", GROUP).getKey();

         identitySession.getRoleManager().createRole("manager", xUser, someGroupId);

         assertEquals(0, identitySession.getRelationshipManager().findAssociatedGroups(xUser, GROUP, null).size());
         assertEquals(1, identitySession.getRoleManager().findGroupsWithRelatedRole(xUser, GROUP, null).size());
         assertEquals(1, identitySession.getRoleManager().findGroupsWithRelatedRole(xUser, null).size());

         String otherGroupId = identitySession.getPersistenceManager().createGroup("otherGroup", GROUP).getKey();

         identitySession.getRelationshipManager().associateUserByKeys(otherGroupId, xUser);

         assertEquals(1, identitySession.getRelationshipManager().findAssociatedGroups(xUser, GROUP, null).size());
         assertEquals(1, identitySession.getRoleManager().findGroupsWithRelatedRole(xUser, GROUP, null).size());
         assertEquals(1, identitySession.getRoleManager().findGroupsWithRelatedRole(xUser, null).size());





         identitySession.getTransaction().commit();
         identitySession.close();
         
         
         System.out.println("Done");

      }

}
