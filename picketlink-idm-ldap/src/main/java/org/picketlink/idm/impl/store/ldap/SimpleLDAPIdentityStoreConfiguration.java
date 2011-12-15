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
import org.picketlink.idm.spi.configuration.metadata.IdentityObjectTypeMetaData;
import org.picketlink.idm.impl.types.SimpleIdentityObjectType;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class SimpleLDAPIdentityStoreConfiguration
   implements LDAPIdentityStoreConfiguration, Serializable
{
   private final IdentityStoreConfigurationMetaData configurationMetaData;

   private final String providerURL;

   private final String adminDN;

   private final String adminPassword;

   private final String authenticationMethod;

   private final int searchTimeLimit;

   private final Map<String, LDAPIdentityObjectTypeConfiguration> typesConfiguration;

   private final Map<String, String> customJNDIConnectionParameters;

   private final Map<String, String> customSystemProperties;

   private final String externalJNDIContext;

   private final String membershipToRelationshipTypeMapping;

   private final boolean supportNamedRelationships;

   private final String[] relationshipNamesCtxDNs;

   private final String relationshipNameSearchFilter;

   private final String relationshipNameSearchScope;

   private final Map<String, String[]> relationshipNameCreateEntryAttributeValues;

   private final String relationshipNameAttributeName;

   private final String namedRelationshipSearchFilter;

   private final Map<String, String[]> namedRelationshipCreateEntryAttributeValues;

   private final String namedRelationshipNameAttributeName;

   private final String namedRelationshipMemberAttributeName;

   private final boolean sortExtensionSupported;

   private final boolean createMissingContexts;


   // Consts

   public static final String PROVIDER_URL = "providerURL";

   public static final String ADMIN_DN = "adminDN";

   public static final String AUTHENTICATION_METHOD = "authenticationMethod";

   public static final String ADMIN_PASSWORD = "adminPassword";

   public static final String SEARCH_TIME_LIMIT = "searchTimeLimit";

   public static final int SEARCH_TIME_LIMIT_DEFAULT = 10000;

   public static final String MAX_SEARCH_RESULTS = "maxSearchResults";

   public static final int MAX_SEARCH_RESULTS_DEFAULT = 250;

   public static final String CUSTOM_JNDI_CONNECTION_PARAMETERS = "customJNDIConnectionParameters";

   public static final String CUSTOM_SYSTEM_PROPERTIES = "customSystemProperties";

   public static final String EXTERNAL_JNDI_CONTEXT = "externalJNDIContext";

   public static final String MEMBERSHIP_TO_RELATIONSHIP_TYPE_MAPPING = "membershipToRelationshipTypeMapping";

   public static final String SUPPORT_NAMED_RELATIONSHIPS = "supportNamedRelationships";

   public static final String RELATIONSHIP_NAMES_CTX_DNS = "relationshipNamesCtxDNs";

   public static final String RELATIONSHIP_NAME_SEARCH_FILTER = "relationshipNameSearchFilter";

   public static final String RELATIONSHIP_NAME_SEARCH_SCOPE = "relationshipNameSearchScope";

   public static final String RELATIONSHOP_NAME_CREATE_ENTRY_ATTRIBUTE_VALUES = "relationshipNameCreateEntryAttributeValues";

   public static final String RELATIONSHIP_NAME_ATTRIBUTE_NAME = "relationshipNameAttributeName";

   public static final String NAMED_RELATIONSHIP_SEARCH_FILTER = "namedRelationshipSearchFilter";

   public static final String NAMED_RELATIONSHIP_CREATE_ENTRY_ATTRIBUTE_VALUES = "namedRelationshipCreateEntryAttributeValues";

   public static final String NAMED_RELATIONSHIP_NAME_ATTRIBUTE_NAME = "namedRelationshipNameAttributeName";

   public static final String NAMED_RELATIONSHIP_MEMBER_ATTRIBUTE_NAME = "namedRelationshipMemberAttributeName";

   public static final String SORT_EXTENSION_SUPPORTED = "sortExtensionSupported";

   public static final String CREATE_MISSING_CONTEXTS = "createMissingContexts";

   public SimpleLDAPIdentityStoreConfiguration(IdentityStoreConfigurationMetaData storeMD)
   {
      if (storeMD == null)
      {
         throw new IllegalArgumentException();
      }


      this.configurationMetaData = storeMD;
      this.providerURL = storeMD.getOptionSingleValue(PROVIDER_URL);
      this.adminDN = storeMD.getOptionSingleValue(ADMIN_DN);
      this.authenticationMethod = storeMD.getOptionSingleValue(AUTHENTICATION_METHOD);
      this.adminPassword = storeMD.getOptionSingleValue(ADMIN_PASSWORD);
      this.externalJNDIContext = storeMD.getOptionSingleValue(EXTERNAL_JNDI_CONTEXT);
      this.membershipToRelationshipTypeMapping = storeMD.getOptionSingleValue(MEMBERSHIP_TO_RELATIONSHIP_TYPE_MAPPING);
      this.relationshipNameSearchFilter = storeMD.getOptionSingleValue(RELATIONSHIP_NAME_SEARCH_FILTER);
      this.relationshipNameSearchScope = storeMD.getOptionSingleValue(RELATIONSHIP_NAME_SEARCH_SCOPE);
      this.relationshipNameAttributeName = storeMD.getOptionSingleValue(RELATIONSHIP_NAME_ATTRIBUTE_NAME);
      this.namedRelationshipSearchFilter = storeMD.getOptionSingleValue(NAMED_RELATIONSHIP_SEARCH_FILTER);
      this.namedRelationshipNameAttributeName = storeMD.getOptionSingleValue(NAMED_RELATIONSHIP_NAME_ATTRIBUTE_NAME);
      this.namedRelationshipMemberAttributeName = storeMD.getOptionSingleValue(NAMED_RELATIONSHIP_MEMBER_ATTRIBUTE_NAME);
      String searchTL = storeMD.getOptionSingleValue(SEARCH_TIME_LIMIT);

      if (searchTL != null)
      {
         this.searchTimeLimit = Integer.valueOf(searchTL);
      }
      else
      {
         this.searchTimeLimit = SEARCH_TIME_LIMIT_DEFAULT;
      }

      String supportNamedRelationships = storeMD.getOptionSingleValue(SUPPORT_NAMED_RELATIONSHIPS);
      if (supportNamedRelationships != null && supportNamedRelationships.equalsIgnoreCase("true"))
      {
         this.supportNamedRelationships = true;
      }
      else
      {
         this.supportNamedRelationships = false;
      }

      String sortExtension = storeMD.getOptionSingleValue(SORT_EXTENSION_SUPPORTED);
      if (sortExtension != null && sortExtension.equalsIgnoreCase("false"))
      {
         this.sortExtensionSupported = false;
      }
      else
      {
         this.sortExtensionSupported = true;
      }

      String createMissingContexts = storeMD.getOptionSingleValue(CREATE_MISSING_CONTEXTS);
      if (createMissingContexts != null && createMissingContexts.equalsIgnoreCase("true"))
      {
         this.createMissingContexts = true;
      }
      else
      {
         this.createMissingContexts = false;
      }

      Map<String, LDAPIdentityObjectTypeConfiguration> types = new HashMap<String, LDAPIdentityObjectTypeConfiguration>();

      for (IdentityObjectTypeMetaData identityObjectTypeMetaData : storeMD.getSupportedIdentityTypes())
      {
         LDAPIdentityObjectTypeConfiguration typeConfig = new SimpleLDAPIdentityObjectTypeConfiguration(identityObjectTypeMetaData);
         types.put(identityObjectTypeMetaData.getName(), typeConfig);

      }

      List<String> dns = storeMD.getOption(RELATIONSHIP_NAMES_CTX_DNS);
      if (dns != null)
      {
         this.relationshipNamesCtxDNs = dns.toArray(new String[dns.size()]);
      }
      else
      {
         this.relationshipNamesCtxDNs = null;
      }

      this.typesConfiguration = types;


      Map<String, List<String>> createNamedRelationshipEntryAttributesMap = new HashMap<String, List<String>>();

      List<String> createNamedRelationshipAttributes = storeMD.getOption(NAMED_RELATIONSHIP_CREATE_ENTRY_ATTRIBUTE_VALUES);

      if (createNamedRelationshipAttributes != null && createNamedRelationshipAttributes.size() > 0)
      {
         for (String attribute : createNamedRelationshipAttributes)
         {
            String[] parts = attribute.split("=", 2);
            if (parts.length != 2)
            {
               continue;
            }

            String name = parts[0];
            String value = parts[1];

            if (!createNamedRelationshipEntryAttributesMap.containsKey(name))
            {
               List<String> list = new LinkedList<String>();
               list.add(value);
               createNamedRelationshipEntryAttributesMap.put(name, list);
            }
            else
            {
               createNamedRelationshipEntryAttributesMap.get(name).add(value);
            }
         }

         Map<String, String[]> createEntryAttributesArray = new HashMap<String, String[]>();

         for (Map.Entry<String, List<String>> entry : createNamedRelationshipEntryAttributesMap.entrySet())
         {
            createEntryAttributesArray.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
         }

         this.namedRelationshipCreateEntryAttributeValues = Collections.unmodifiableMap(createEntryAttributesArray);
      }
      else
      {
         this.namedRelationshipCreateEntryAttributeValues = Collections.unmodifiableMap(new HashMap<String, String[]>());
      }

      Map<String, List<String>> createRelationshipNameEntryAttributesMap = new HashMap<String, List<String>>();

      List<String> createRelationshipNameAttributes = storeMD.getOption(RELATIONSHOP_NAME_CREATE_ENTRY_ATTRIBUTE_VALUES);

      if (createRelationshipNameAttributes != null && createRelationshipNameAttributes.size() > 0 )
      {
         for (String attribute : createRelationshipNameAttributes)
         {
            String[] parts = attribute.split("=", 2);
            if (parts.length != 2)
            {
               continue;
            }

            String name = parts[0];
            String value = parts[1];

            if (!createRelationshipNameEntryAttributesMap.containsKey(name))
            {
               List<String> list = new LinkedList<String>();
               list.add(value);
               createRelationshipNameEntryAttributesMap.put(name, list);
            }
            else
            {
               createRelationshipNameEntryAttributesMap.get(name).add(value);
            }
         }

         Map<String, String[]> createEntryAttributesArray = new HashMap<String, String[]>();

         for (Map.Entry<String, List<String>> entry : createRelationshipNameEntryAttributesMap.entrySet())
         {
            createEntryAttributesArray.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
         }

         this.relationshipNameCreateEntryAttributeValues = Collections.unmodifiableMap(createEntryAttributesArray);
      }
      else
      {
         this.relationshipNameCreateEntryAttributeValues = Collections.unmodifiableMap(new HashMap<String, String[]>());
      }

      List<String> customJndiParams = storeMD.getOption(CUSTOM_JNDI_CONNECTION_PARAMETERS);

      Map<String, String> customJndiParamsMap = new HashMap<String, String>();

      if (customJndiParams != null && customJndiParams.size() > 0 )
      {
         for (String param : customJndiParams)
         {
            String[] parts = param.split("=", 2);
            if (parts.length != 2)
            {
               continue;
            }

            String name = parts[0];
            String value = parts[1];

            customJndiParamsMap.put(name, value);

         }

         this.customJNDIConnectionParameters = Collections.unmodifiableMap(customJndiParamsMap);
      }
      else
      {
         this.customJNDIConnectionParameters = Collections.unmodifiableMap(new HashMap<String, String>());
      }

      List<String> customSystemProps = storeMD.getOption(CUSTOM_SYSTEM_PROPERTIES);

      Map<String, String> customSystemProperties = new HashMap<String, String>();

      if (customSystemProps != null && customSystemProps.size() > 0 )
      {
         for (String param : customSystemProps)
         {
            String[] parts = param.split("=", 2);
            if (parts.length != 2)
            {
               continue;
            }

            String name = parts[0];
            String value = parts[1];

            customSystemProperties.put(name, value);

         }

         this.customSystemProperties = Collections.unmodifiableMap(customSystemProperties);
      }
      else
      {
         this.customSystemProperties = Collections.unmodifiableMap(new HashMap<String, String>());
      }

      //TODO: validate if critical values are present


   }

   public String getProviderURL()
   {
      return providerURL;
   }

   public String getAdminDN()
   {
      return adminDN;
   }

   public String getAdminPassword()
   {
      return adminPassword;
   }

   public int getSearchTimeLimit()
   {
      return searchTimeLimit;
   }

   public LDAPIdentityObjectTypeConfiguration getTypeConfiguration(String typeName)
   {
      return typesConfiguration.get(typeName);
   }

   public IdentityObjectType[] getConfiguredTypes()
   {
      IdentityObjectType[] types = new IdentityObjectType[typesConfiguration.size()];
      Object[] names = typesConfiguration.keySet().toArray();

      for (int i = 0; i < names.length; i++)
      {
         String name = names[i].toString();
         types[i] = new SimpleIdentityObjectType(name);
      }

      return types;
   }

   public IdentityStoreConfigurationMetaData getConfigurationMetaData()
   {
      return configurationMetaData;
   }

   public Map<String, LDAPIdentityObjectTypeConfiguration> getTypesConfiguration()
   {
      return typesConfiguration;
   }

   public Map<String, String> getCustomJNDIConnectionParameters()
   {
      return customJNDIConnectionParameters;
   }

   public Map<String, String> getCustomSystemProperties()
   {
      return customSystemProperties;
   }

   public String getExternalJNDIContext()
   {
      return externalJNDIContext;
   }

   public String getMembershipToRelationshipTypeMapping()
   {
      return membershipToRelationshipTypeMapping;
   }

   public boolean isSupportNamedRelationships()
   {
      return supportNamedRelationships;
   }

   public String[] getRelationshipNamesCtxDNs()
   {
      return relationshipNamesCtxDNs;
   }

   public String getRelationshipNameSearchFilter()
   {
      return relationshipNameSearchFilter;
   }

   public String getRelationshipNameSearchScope()
   {
      return relationshipNameSearchScope;
   }

   public Map<String, String[]> getRelationshipNameCreateEntryAttributeValues()
   {
      return relationshipNameCreateEntryAttributeValues;
   }

   public String getRelationshipNameAttributeName()
   {
      return relationshipNameAttributeName;
   }

   public String getNamedRelationshipSearchFilter()
   {
      return namedRelationshipSearchFilter;
   }

   public Map<String, String[]> getNamedRelationshipCreateEntryAttributeValues()
   {
      return namedRelationshipCreateEntryAttributeValues;
   }

   public String getNamedRelationshipNameAttributeName()
   {
      return namedRelationshipNameAttributeName;
   }

   public String getNamedRelationshipMemberAttributeName()
   {
      return namedRelationshipMemberAttributeName;
   }

   public String getAuthenticationMethod()
   {
      return authenticationMethod;
   }

   public boolean isSortExtensionSupported()
   {
      return sortExtensionSupported;
   }

   public boolean isCreateMissingContexts()
   {
      return createMissingContexts;
   }
}
