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
import org.picketlink.idm.api.RoleType;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.Attribute;
import org.picketlink.idm.api.AttributeDescription;
import org.picketlink.idm.api.Credential;
import org.picketlink.idm.common.p3p.P3PConstants;
import org.picketlink.idm.common.exception.IdentityException;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;

import junit.framework.Assert;

/**
 * Abstract test mapping real life structures using the API
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class OrganizationTest extends Assert
{
   APITestContext ctx;

   public OrganizationTest(APITestContext ctx)
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

   public void testRedHatOrganization(String realmName) throws Exception
   {
      // GroupType

      String ORGANIZATION = "ORGANIZATION";
      String ORGANIZATION_UNIT = "ORGANIZATION_UNIT";
      String DIVISION = "DIVISION";
      String DEPARTMENT = "DEPARTMENT";
      String PROJECT = "PROJECT";
      String PEOPLE = "PEOPLE";

      IdentitySessionFactory factory = ctx.getIdentitySessionFactory();

      IdentitySession session = factory.createIdentitySession(realmName);

      //ctx.begin();

      ctx.begin();

      // Organization structure

      Group rhOrg = session.getPersistenceManager().createGroup("RedHat", ORGANIZATION);

      Group jbossDivision = session.getPersistenceManager().createGroup("JBoss", DIVISION);
      Group rhelDivision = session.getPersistenceManager().createGroup("RHEL", DIVISION);

      session.getRelationshipManager().associateGroups(rhOrg, jbossDivision);
      session.getRelationshipManager().associateGroups(rhOrg, rhelDivision);

      Group itDepartment = session.getPersistenceManager().createGroup("IT", DEPARTMENT);
      Group hrDepartment = session.getPersistenceManager().createGroup("HR", DEPARTMENT);

      session.getRelationshipManager().associateGroups(jbossDivision, itDepartment);
      session.getRelationshipManager().associateGroups(jbossDivision, hrDepartment);

      Group rndDepartment = session.getPersistenceManager().createGroup("RnD", DEPARTMENT); 

      session.getRelationshipManager().associateGroups(itDepartment, rndDepartment);

      Group projectsOU = session.getPersistenceManager().createGroup("Projects", ORGANIZATION_UNIT);
      Group commonFrameworksOU = session.getPersistenceManager().createGroup("Common Frameworks", ORGANIZATION_UNIT);

      session.getRelationshipManager().associateGroups(rndDepartment, projectsOU);

      // Projects

      Group portalProject = session.getPersistenceManager().createGroup("Portal", PROJECT);
      Group soaProject = session.getPersistenceManager().createGroup("SOA", PROJECT);
      Group jbpmProject = session.getPersistenceManager().createGroup("jBPM", PROJECT);
      Group seamProject = session.getPersistenceManager().createGroup("Seam", PROJECT);
      Group asProject = session.getPersistenceManager().createGroup("AS", PROJECT);
      Group securityProject = session.getPersistenceManager().createGroup("Security", PROJECT);

      session.getRelationshipManager().associateGroups(projectsOU, portalProject);
      session.getRelationshipManager().associateGroups(projectsOU, soaProject);
      session.getRelationshipManager().associateGroups(projectsOU, jbpmProject);
      session.getRelationshipManager().associateGroups(projectsOU, asProject);
      session.getRelationshipManager().associateGroups(projectsOU, seamProject);

      // Check...
      assertTrue(session.getRelationshipManager().isAssociated(projectsOU, portalProject));
      assertTrue(session.getRelationshipManager().isAssociated(projectsOU, soaProject));
      assertTrue(session.getRelationshipManager().isAssociated(projectsOU, jbpmProject));
      assertTrue(session.getRelationshipManager().isAssociated(projectsOU, asProject));
      assertTrue(session.getRelationshipManager().isAssociated(projectsOU, seamProject));

      // Portal is part of common frameworks
      session.getRelationshipManager().associateGroups(commonFrameworksOU, portalProject);

      // People

      Group employeesGroup = session.getPersistenceManager().createGroup("Employees", PEOPLE);

      // Management

      User theuteUser = session.getPersistenceManager().createUser("theute");
      User mlittleUser = session.getPersistenceManager().createUser("mlittle");
      User bgeorgesUser = session.getPersistenceManager().createUser("bgeorges");
      User asaldhanaUser = session.getPersistenceManager().createUser("asaldhana");
      User janderseUser = session.getPersistenceManager().createUser("janderse");

       // Portal Team

      User bdawidowUser = session.getPersistenceManager().createUser("bdawidow");
      User claprunUser = session.getPersistenceManager().createUser("claprun");
      User whalesUser = session.getPersistenceManager().createUser("whales");
      User sshahUser = session.getPersistenceManager().createUser("sshah");
      User mwringeUser = session.getPersistenceManager().createUser("mwringe");

      // Store as employees

      session.getRelationshipManager().associateUser(employeesGroup, theuteUser);
      session.getRelationshipManager().associateUser(employeesGroup, mlittleUser);
      session.getRelationshipManager().associateUser(employeesGroup, asaldhanaUser);
      session.getRelationshipManager().associateUser(employeesGroup, bdawidowUser);
      session.getRelationshipManager().associateUser(employeesGroup, claprunUser);
      session.getRelationshipManager().associateUser(employeesGroup, whalesUser);
      session.getRelationshipManager().associateUser(employeesGroup, sshahUser);
      session.getRelationshipManager().associateUser(employeesGroup, mwringeUser);

      // Portal team for management purposes

      Group portalTeamGroup = session.getPersistenceManager().createGroup("Portal Team", PEOPLE);
      session.getRelationshipManager().associateUser(portalTeamGroup, bdawidowUser);
      session.getRelationshipManager().associateUser(portalTeamGroup, claprunUser);
      session.getRelationshipManager().associateUser(portalTeamGroup, whalesUser);
      session.getRelationshipManager().associateUser(portalTeamGroup, sshahUser);
      session.getRelationshipManager().associateUser(portalTeamGroup, mwringeUser);

      // Portal team is under common frameworks

      session.getRelationshipManager().associateGroups(commonFrameworksOU, portalTeamGroup);

      // Role Types

      RoleType developerRT = session.getRoleManager().createRoleType("Developer");
      RoleType managerRT = session.getRoleManager().createRoleType("Manager");
      RoleType leadDeveloperRT = session.getRoleManager().createRoleType("Lead Developer");
      RoleType productManagerRT = session.getRoleManager().createRoleType("Product Manager");

      // Assign roles

      // Common frameworks manager

      session.getRoleManager().createRole(managerRT, bgeorgesUser, commonFrameworksOU);

      // Portal developers

      session.getRoleManager().createRole(developerRT, theuteUser, portalProject);
      session.getRoleManager().createRole(developerRT, bdawidowUser, portalProject);
      session.getRoleManager().createRole(developerRT, claprunUser, portalProject);
      session.getRoleManager().createRole(developerRT, whalesUser, portalProject);
      session.getRoleManager().createRole(developerRT, sshahUser, portalProject);
      session.getRoleManager().createRole(developerRT, mwringeUser, portalProject);

      // Portal management
      session.getRoleManager().createRole(leadDeveloperRT, theuteUser, portalProject);
      session.getRoleManager().createRole(managerRT, theuteUser, portalTeamGroup);
      session.getRoleManager().createRole(productManagerRT, janderseUser, portalProject);

      // SOA

      session.getRoleManager().createRole(developerRT, mlittleUser, portalProject);
      session.getRoleManager().createRole(productManagerRT, mlittleUser, portalProject);

      // AS & Security

      session.getRoleManager().createRole(developerRT, asaldhanaUser, asProject);
      session.getRoleManager().createRole(developerRT, asaldhanaUser, securityProject);
      session.getRoleManager().createRole(leadDeveloperRT, asaldhanaUser, securityProject);


      // Check what RoleTypes has user theute
      Collection<RoleType> roleTypes = session.getRoleManager().findUserRoleTypes(theuteUser);
      assertEquals(3, roleTypes.size());
      assertTrue(roleTypes.contains(developerRT));
      assertTrue(roleTypes.contains(leadDeveloperRT));
      assertTrue(roleTypes.contains(managerRT));
      assertFalse(roleTypes.contains(productManagerRT));

      assertTrue(session.getRoleManager().hasRole(theuteUser, portalProject, developerRT));
      assertTrue(session.getRoleManager().hasRole(theuteUser, portalProject, leadDeveloperRT));
      assertTrue(session.getRoleManager().hasRole(theuteUser, portalTeamGroup, managerRT));

      // Check where anil is Lead Developer and where Developer

      roleTypes = session.getRoleManager().findUserRoleTypes(asaldhanaUser);
      assertEquals(2, roleTypes.size());
      assertTrue(roleTypes.contains(developerRT));
      assertTrue(roleTypes.contains(leadDeveloperRT));

      roleTypes = session.getRoleManager().findRoleTypes(asaldhanaUser, securityProject);
      assertEquals(2, roleTypes.size());
      assertTrue(roleTypes.contains(leadDeveloperRT));

      roleTypes = session.getRoleManager().findRoleTypes(asaldhanaUser, asProject);
      assertEquals(1, roleTypes.size());
      assertTrue(roleTypes.contains(developerRT));

      // and simpler...
      assertTrue(session.getRoleManager().hasRole(asaldhanaUser, asProject, developerRT));

      // Assert relationships

      Collection<User> identities = session.getRelationshipManager().findAssociatedUsers(portalTeamGroup, false);
      assertEquals(5, identities.size());
      assertTrue(identities.contains(claprunUser));
      assertTrue(identities.contains(mwringeUser));
      assertTrue(identities.contains(sshahUser));
      assertTrue(identities.contains(whalesUser));
      assertTrue(identities.contains(bdawidowUser));

      Collection<Group> groups = session.getRelationshipManager().findAssociatedGroups(rndDepartment, PROJECT, true, false);
      assertEquals(0, groups.size());

      // Check to which group Anil belongs
      groups = session.getRelationshipManager().findAssociatedGroups(asaldhanaUser, PEOPLE);
      assertEquals(1, groups.size());
      assertTrue(groups.contains(employeesGroup));

      // Now check sshah
      groups = session.getRelationshipManager().findAssociatedGroups(sshahUser, PEOPLE);
      assertEquals(2, groups.size());
      assertTrue(groups.contains(employeesGroup));
      assertTrue(groups.contains(portalTeamGroup));



      
      // User attributes
      Attribute[] userInfo = new Attribute[]
         {
            new SimpleAttribute(P3PConstants.INFO_USER_NAME_GIVEN, new String[]{"Boleslaw"}),
            new SimpleAttribute(P3PConstants.INFO_USER_NAME_FAMILY, new String[]{"Dawidowicz"}),
            //new SimpleAttribute("picture", new byte[][]{picture}),
            new SimpleAttribute("email", new String[]{"bd@example.com"})
         };

      session.getAttributesManager().addAttributes(bdawidowUser, userInfo);

      Map<String, Attribute> attributes = session.getAttributesManager().getAttributes(bdawidowUser);
      assertEquals(3, attributes.keySet().size());
      assertEquals("Dawidowicz", (attributes.get(P3PConstants.INFO_USER_NAME_FAMILY)).getValue());
      

      // Generate random binary data for binary attribute
      Random random = new Random();

      // Check that binary attribute picture is mapped
      AttributeDescription attributeDescription = session.getAttributesManager().getAttributeDescription(bdawidowUser, "picture");

      if (attributeDescription != null && attributeDescription.getType().equals("binary"))
      {

         // 900 kilobytes
         byte[] picture = new byte[921600];
         random.nextBytes(picture);

         userInfo = new Attribute[]
         {
            new SimpleAttribute("picture", new byte[][]{picture}),
         };


         session.getAttributesManager().addAttributes(bdawidowUser, userInfo);

         attributes = session.getAttributesManager().getAttributes(bdawidowUser);
         assertEquals(4, attributes.keySet().size());
         assertEquals("Dawidowicz", (attributes.get(P3PConstants.INFO_USER_NAME_FAMILY)).getValue());
         assertTrue(Arrays.equals((byte[])attributes.get("picture").getValue(), picture));
      }


      // Find user by email
      assertNull(session.getAttributesManager().findUserByUniqueAttribute("email", "toto"));
      User user = session.getAttributesManager().findUserByUniqueAttribute("email", "bd@example.com");
      assertEquals(bdawidowUser, user);


      // If email is configured as unique it should not be possible to set same value for different user
      
      attributeDescription = session.getAttributesManager().getAttributeDescription(bdawidowUser, "email");

      if (attributeDescription != null && attributeDescription.isUnique())
      {


         // check if same unique email can be used for other user
         try
         {
            userInfo = new Attribute[]
               {
                  new SimpleAttribute("email", new String[]{"bd@example.com"})
               };

            session.getAttributesManager().addAttributes(theuteUser, userInfo);
            fail();
         }
         catch (IdentityException e)
         {
            // expected
         }
      }



      // Credential
      User anotherOne = bdawidowUser; //session.getPersistenceManager().createUser("blah1");

      if (session.getAttributesManager().isCredentialTypeSupported(PasswordCredential.TYPE))
      {

         // There is a known issue that on some LDAP servers (MSAD at least) old password can
         // still be used for some time together with the new one. Because of this testsuite cannot
         // assert previously set password values

         // #1
         session.getAttributesManager().updatePassword(anotherOne, "Password2000");
         assertTrue(session.getAttributesManager().validatePassword(anotherOne, "Password2000"));
         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "Password2001"));
         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "Password2002"));

         // #1
         session.getAttributesManager().updatePassword(anotherOne, "Password2002");
         assertTrue(session.getAttributesManager().validatePassword(anotherOne, "Password2002"));
         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "Password2001"));
         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "wirdPasswordValue"));
//         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "Password2000"));
         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "Password2003"));


         // #1
         session.getAttributesManager().updatePassword(anotherOne, "Password2003");
         assertTrue(session.getAttributesManager().validatePassword(anotherOne, "Password2003"));
//         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "Password2000"));
//         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "Password2002"));
         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "Password2005"));
         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "Password2006"));
         assertFalse(session.getAttributesManager().validatePassword(anotherOne, "Password2007"));


         // #2
         Credential password = new PasswordCredential("SuperPassword2345");
         session.getAttributesManager().updateCredential(anotherOne, password);
         assertTrue(session.getAttributesManager().validateCredentials(anotherOne, new Credential[]{password}));

         // #3
      }

      if (session.getAttributesManager().isCredentialTypeSupported(BinaryCredential.TYPE))
      {
         // 500 kilobytes
         byte[] cert = new byte[512000];
         random.nextBytes(cert);
         Credential binaryCredential = new BinaryCredential(cert);
         session.getAttributesManager().updateCredential(anotherOne, binaryCredential);
         assertTrue(session.getAttributesManager().validateCredentials(anotherOne, new Credential[]{binaryCredential}));
      }


      ctx.commit();

   }

   public void testSamplePortal(String realmName) throws Exception
   {
      String SYSTEM = "SYSTEM";
      String ADMINISTRATION = "ADMINISTRATION";
      String COMMUNITY = "COMMUNITY";
      String ORGANIZATION = "ORGANIZATION";
      String ORGANIZATION_UNIT = "ORGANIZATION_UNIT";
      String OFFICE = "OFFICE";
      String DIVISION = "DIVISION";
      String DEPARTMENT = "DEPARTMENT";
      String SECURITY = "SECURITY";
      String PEOPLE = "PEOPLE";

      IdentitySessionFactory factory = ctx.getIdentitySessionFactory();

      IdentitySession session = factory.createIdentitySession(realmName);

      ctx.begin();



      // Create all role types
      RoleType adminRT = session.getRoleManager().createRoleType("Admin");
      RoleType accountAdminRT = session.getRoleManager().createRoleType("Account Admin");
      RoleType managerRT = session.getRoleManager().createRoleType("Manager");
      RoleType officeManagerRT = session.getRoleManager().createRoleType("Office Manager");
      RoleType contributorRT = session.getRoleManager().createRoleType("Contributor");
      RoleType communityOwnerRT = session.getRoleManager().createRoleType("Community Owner");
      RoleType communityMemberRT = session.getRoleManager().createRoleType("Community Member");
      RoleType communityForumModeratorRT = session.getRoleManager().createRoleType("Community Forum Moderator");
      RoleType communityCMSAdminRT = session.getRoleManager().createRoleType("Community CMS Admin");


      // Create system root groups - groups containing all communities, global security groups and organization

      Group communityRootGroup = session.getPersistenceManager().createGroup("COMMUNITY_ROOT", SYSTEM);
      Group securityRootGroup = session.getPersistenceManager().createGroup("SECURITY_ROOT", SYSTEM);
      Group organizationRootGroup = session.getPersistenceManager().createGroup("ORGANIZATION_ROOT",SYSTEM);
      Group usersROOTGroup = session.getPersistenceManager().createGroup("USERS_ROOT",SYSTEM);

      ctx.commit();

      ctx.begin();

      // Communities

      Group portalLoversCommunity = session.getPersistenceManager().createGroup("Portal Lovers", COMMUNITY);
      Group baseJumpingCommunity = session.getPersistenceManager().createGroup("BASE Jumping", COMMUNITY);
      Group geeksCommunity = session.getPersistenceManager().createGroup("Geeks", COMMUNITY);

      session.getRelationshipManager().associateGroups(communityRootGroup, portalLoversCommunity);
      session.getRelationshipManager().associateGroups(communityRootGroup, baseJumpingCommunity);
      session.getRelationshipManager().associateGroups(communityRootGroup, geeksCommunity);

      // Security groups - act like global portal roles

      Group portalAdminGroup = session.getPersistenceManager().createGroup("Poral Admin", SECURITY);
      Group cmsAdminGroup = session.getPersistenceManager().createGroup("CMS Admin", SECURITY);
      Group userAdminGroup = session.getPersistenceManager().createGroup("User Admin", SECURITY);
      Group cmsEditorGroup = session.getPersistenceManager().createGroup("CMS Editor", SECURITY);

      session.getRelationshipManager().associateGroups(securityRootGroup, portalAdminGroup);
      session.getRelationshipManager().associateGroups(securityRootGroup, cmsAdminGroup);
      session.getRelationshipManager().associateGroups(securityRootGroup, userAdminGroup);
      session.getRelationshipManager().associateGroups(securityRootGroup, cmsEditorGroup);

      // Organization structure

      Group acmeOrg = session.getPersistenceManager().createGroup("ACME", ORGANIZATION);

      session.getRelationshipManager().associateGroups(organizationRootGroup, acmeOrg);

      Group departmentsOU = session.getPersistenceManager().createGroup("Departments", ORGANIZATION_UNIT);
      Group officesOU = session.getPersistenceManager().createGroup("Offices", ORGANIZATION_UNIT);
      Group employeesOU = session.getPersistenceManager().createGroup("Employees", ORGANIZATION_UNIT);

      session.getRelationshipManager().associateGroups(acmeOrg, departmentsOU);
      session.getRelationshipManager().associateGroups(acmeOrg, officesOU);
      session.getRelationshipManager().associateGroups(acmeOrg, employeesOU);

      // Departments

      Group hrDepart = session.getPersistenceManager().createGroup("HR", DEPARTMENT);
      Group financeDepart = session.getPersistenceManager().createGroup("Finance", DEPARTMENT);
      Group rndDepart = session.getPersistenceManager().createGroup("R&D", DEPARTMENT);

      session.getRelationshipManager().associateGroups(departmentsOU, hrDepart);
      session.getRelationshipManager().associateGroups(departmentsOU, financeDepart);
      session.getRelationshipManager().associateGroups(departmentsOU, rndDepart);

      // Offices

      Group parisOffice =session.getPersistenceManager().createGroup("Paris", OFFICE);
      Group londonOffice =session.getPersistenceManager().createGroup("London", OFFICE);
      Group nyOffice =session.getPersistenceManager().createGroup("New York", OFFICE);

      session.getRelationshipManager().associateGroups(officesOU, parisOffice);
      session.getRelationshipManager().associateGroups(officesOU, londonOffice);
      session.getRelationshipManager().associateGroups(officesOU, nyOffice);

      // People

      User anneUser = session.getPersistenceManager().createUser("anne");
      User marieUser = session.getPersistenceManager().createUser("marie");
      User eveUser = session.getPersistenceManager().createUser("eve");
      User angelinaUser = session.getPersistenceManager().createUser("angelina");
      User joannaUser = session.getPersistenceManager().createUser("joanna");
      User merilUser = session.getPersistenceManager().createUser("meril");
      User johnUser = session.getPersistenceManager().createUser("john");
      User stanUser = session.getPersistenceManager().createUser("stan");
      User chrisUser = session.getPersistenceManager().createUser("chris");
      User billUser = session.getPersistenceManager().createUser("bill");
      User jackUser = session.getPersistenceManager().createUser("jack");

      ctx.commit();

      ctx.begin();

      // All users are under people root

      session.getRelationshipManager().associateUser(usersROOTGroup, anneUser);
      session.getRelationshipManager().associateUser(usersROOTGroup, marieUser);
      session.getRelationshipManager().associateUser(usersROOTGroup, eveUser);
      session.getRelationshipManager().associateUser(usersROOTGroup, angelinaUser);
      session.getRelationshipManager().associateUser(usersROOTGroup, joannaUser);
      session.getRelationshipManager().associateUser(usersROOTGroup, merilUser);
      session.getRelationshipManager().associateUser(usersROOTGroup, johnUser);
      session.getRelationshipManager().associateUser(usersROOTGroup, stanUser);
      session.getRelationshipManager().associateUser(usersROOTGroup, chrisUser);
      session.getRelationshipManager().associateUser(usersROOTGroup, billUser);
      session.getRelationshipManager().associateUser(usersROOTGroup, jackUser);

      ctx.commit();

      ctx.begin();

      //


      // Anna is...

      // Anna Smith...

      
      session.getAttributesManager().addAttribute(anneUser, P3PConstants.INFO_USER_NAME_GIVEN, "Anne");
      session.getAttributesManager().addAttribute(anneUser, P3PConstants.INFO_USER_NAME_FAMILY, "Smith");
      session.getAttributesManager().addAttribute(anneUser, P3PConstants.INFO_USER_JOB_TITLE, "Senior Software Developer");
      session.getAttributesManager().addAttribute(anneUser, P3PConstants.INFO_USER_BUSINESS_INFO_ONLINE_EMAIL, "anne.smith@acme.com");
      session.getAttributesManager().addAttribute(anneUser, P3PConstants.INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_NUMBER, "777 777 777 7 77");

      // Anna works in Paris office and participates in BASE jumping community as a forum moderator and CMS admin

      session.getRelationshipManager().associateUser(parisOffice, anneUser);

      // Anne could be just associated with BASE jumping community group using RelationshipManager but insted Role mechanism
      // is used to have more fine grained control of access rights inside of a community


      session.save();

      ctx.commit();

      ctx.begin();

      session.getRoleManager().createRole(communityMemberRT, anneUser, baseJumpingCommunity);
      session.getRoleManager().createRole(communityForumModeratorRT, anneUser, baseJumpingCommunity);
      session.getRoleManager().createRole(communityCMSAdminRT, anneUser, baseJumpingCommunity);

      // Anne belongs to CMS portal admins security group

      session.getRelationshipManager().associateUser(cmsAdminGroup, anneUser);

      // Ann is also an owner of geeks community

      session.getRoleManager().createRole(communityOwnerRT, anneUser, geeksCommunity);


      //


      // Angelina ...

      session.getAttributesManager().addAttribute(angelinaUser, P3PConstants.INFO_USER_NAME_GIVEN, "Angelina");
      session.getAttributesManager().addAttribute(angelinaUser, P3PConstants.INFO_USER_NAME_FAMILY, "Blond");
      session.getAttributesManager().addAttribute(angelinaUser, P3PConstants.INFO_USER_JOB_TITLE, "Very important looking job title");
      session.getAttributesManager().addAttribute(angelinaUser, P3PConstants.INFO_USER_BUSINESS_INFO_ONLINE_EMAIL, "angelina1979@acme.com");
      session.getAttributesManager().addAttribute(angelinaUser, P3PConstants.INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_NUMBER, "888 88 88 8888");

      // She works in London and is office manager there - se both RelationshipManager and RoleManager used against
      // the same pair of identity objects

      session.getRelationshipManager().associateUser(londonOffice, angelinaUser);

      session.getRoleManager().createRole(officeManagerRT, angelinaUser, londonOffice);

      // Angelina is member of geeks community and manages cms content there

      session.getRoleManager().createRole(communityMemberRT, angelinaUser, geeksCommunity);
      session.getRoleManager().createRole(communityCMSAdminRT, angelinaUser, geeksCommunity);

      // As office manager she also edits portal cms content

      session.getRelationshipManager().associateUser(cmsEditorGroup, angelinaUser);


      //


      // Some assertions

      // How many users do we have in whole acme org and how many people in paris and london offices
      assertEquals(11, session.getRelationshipManager().findAssociatedUsers(usersROOTGroup, false).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedUsers(parisOffice, false).size());
      assertEquals(1, session.getRelationshipManager().findAssociatedUsers(londonOffice, false).size());


      // Is anne geeks community owner?
      assertTrue(session.getRoleManager().hasRole(anneUser, geeksCommunity, communityOwnerRT));

      // Does she belong to cms admins
      assertTrue(session.getRelationshipManager().isAssociated(cmsAdminGroup, anneUser));

      // Check all groups that anna belongs to
      Collection<Group> groups = session.getRelationshipManager().findAssociatedGroups(anneUser);
      assertEquals(3, groups.size());
      assertTrue(groups.contains(usersROOTGroup));
      assertTrue(groups.contains(parisOffice));
      assertTrue(groups.contains(cmsAdminGroup));



      ctx.commit();

   }
   





   
}
