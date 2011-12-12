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

package org.picketlink.idm.impl.store.ldap;

import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.spi.configuration.metadata.IdentityStoreConfigurationMetaData;

import java.util.Map;

/**
 * Helper interface to expose LDAP IdentityStore configuration in a different way
 * 
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface LDAPIdentityStoreConfiguration
{
   
   IdentityStoreConfigurationMetaData getConfigurationMetaData();

   String getProviderURL();

   String getAdminDN();

   String getAdminPassword();

   String getAuthenticationMethod();

   int getSearchTimeLimit();

   LDAPIdentityObjectTypeConfiguration getTypeConfiguration(String typeName);

   IdentityObjectType[] getConfiguredTypes();

   Map<String, LDAPIdentityObjectTypeConfiguration> getTypesConfiguration();

   Map<String, String> getCustomJNDIConnectionParameters();

   Map<String, String> getCustomSystemProperties();

   String getExternalJNDIContext();

   String getMembershipToRelationshipTypeMapping();

   boolean isSupportNamedRelationships();

   String[] getRelationshipNamesCtxDNs();

   String getRelationshipNameSearchFilter();

   String getRelationshipNameSearchScope();

   Map<String, String[]> getRelationshipNameCreateEntryAttributeValues();

   String getRelationshipNameAttributeName();

   String getNamedRelationshipSearchFilter();

   Map<String, String[]> getNamedRelationshipCreateEntryAttributeValues();

   String getNamedRelationshipNameAttributeName();

   String getNamedRelationshipMemberAttributeName();

   boolean isSortExtensionSupported();

   boolean isCreateMissingContexts();

}
