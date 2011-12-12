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

import org.picketlink.idm.impl.IdentityTestPOJO;
import org.picketlink.idm.impl.configuration.IdentityConfigurationImpl;
import org.picketlink.idm.api.IdentitySessionFactory;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class OrganizationTestCase extends IdentityTestPOJO implements APITestContext
{
   private OrganizationTest orgTest;

   private IdentitySessionFactory identitySessionFactory;

   private String samplePortalRealmName = "realm://portal/SamplePortal/DB";

   private String samplePortalRealmCloneName = "realm://portal/SamplePortal/DB-clone";

   private String sampleOrganizationRealmName = "realm://RedHat/DB";

   private String sampleOrganizationRealmCloneName = "realm://RedHat/DB-clone";


   public void setUp() throws Exception
   {
      super.start();

      orgTest = new OrganizationTest(this);

      identitySessionFactory = new IdentityConfigurationImpl().
         configure(getIdentityConfig()).buildIdentitySessionFactory();
   }

   public void tearDown() throws Exception
   {
      super.stop();
   }

   public IdentitySessionFactory getIdentitySessionFactory()
   {
      return identitySessionFactory;
   }

   public void testOrganization() throws Exception
   {

      orgTest.testRedHatOrganization(getSampleOrganizationRealmName());
      orgTest.testRedHatOrganization(getSampleOrganizationRealmCloneName());
   }

   public void testSamplePortal() throws Exception
   {

      orgTest.testSamplePortal(getSamplePortalRealmName());
      orgTest.testSamplePortal(getSamplePortalRealmCloneName());

   }

   public String getSamplePortalRealmName()
   {
      return samplePortalRealmName;
   }

   public void setSamplePortalRealmName(String samplePortalRealmName)
   {
      this.samplePortalRealmName = samplePortalRealmName;
   }

   public String getSampleOrganizationRealmName()
   {
      return sampleOrganizationRealmName;
   }

   public void setSampleOrganizationRealmName(String sampleOrganizationRealmName)
   {
      this.sampleOrganizationRealmName = sampleOrganizationRealmName;
   }

   public String getSamplePortalRealmCloneName()
   {
      return samplePortalRealmCloneName;
   }

   public void setSamplePortalRealmCloneName(String samplePortalRealmCloneName)
   {
      this.samplePortalRealmCloneName = samplePortalRealmCloneName;
   }

   public String getSampleOrganizationRealmCloneName()
   {
      return sampleOrganizationRealmCloneName;
   }

   public void setSampleOrganizationRealmCloneName(String sampleOrganizationRealmCloneName)
   {
      this.sampleOrganizationRealmCloneName = sampleOrganizationRealmCloneName;
   }
}