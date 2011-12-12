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

import org.picketlink.idm.spi.configuration.metadata.IdentityObjectTypeMetaData;
import org.picketlink.idm.spi.configuration.metadata.RelationshipMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityObjectAttributeMetaData;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class SimpleLDAPIdentityObjectTypeConfiguration implements LDAPIdentityObjectTypeConfiguration, Serializable
{
   private final String idAttributeName;

   private final String passwordAttributeName;

   private final String[] ctxDNs;

   private final String entrySearchFilter;

   private final String entrySearchScope;

   private final boolean allowCreateEntry;

   private final Map<String, String[]> createEntryAttributeValues;

   private final String[] allowedMembershipTypes;

   private final String parentMembershipAttributeName;

   private final String parentMembershipAttributePlaceholder;

   private final boolean isParentMembershipAttributeDN;

   private final boolean allowEmptyMemberships;

   private final Map<String, String> attributeNames;

   private final String childMembershipAttributeName;

   private final boolean isChildMembershipAttributeDN;

   private final boolean isChildMembershipAttributeVirtual;

   private final boolean allowEmptyPassword;

   private final String enclosePasswordWith;

   private final String passwordEncoding;

   private final Map<String, String[]> updatePasswordAttributeValues;

   private final boolean subentryMembershipLookup;


   //Consts

   public static final String ID_ATTRIBUTE_NAME = "idAttributeName";

   public static final String PASSWORD_ATTRIBUTE_NAME = "passwordAttributeName";

   public static final String CTX_DNS = "ctxDNs";

   public static final String ENTRY_SEARCH_FILTER = "entrySearchFilter";

   public static final String ENTRY_SEARCH_SCOPE = "entrySearchScope";

   public static final String ALLOW_CREATE_ENTRY = "allowCreateEntry";

   public static final String PARENT_MEMBERSHIP_ATTRIBUTE_NAME = "parentMembershipAttributeName";

   public static final String PARENT_MEMBERSHIP_ATTRIBUTE_PLACEHOLDER = "parentMembershipAttributePlaceholder";

   public static final String IS_PARENT_MEMBERSHIP_ATTRIBUTE_DN = "isParentMembershipAttributeDN";

   public static final String ALLOW_EMPTY_MEMBERSHIPS = "isAllowEmptyMemberships";

   public static final String CREATE_ENTRY_ATTRIBUTE_VALUES = "createEntryAttributeValues";

   public static final String CHILD_MEMBERSHIP_ATTRIBUTE_NAME = "childMembershipAttributeName";

   public static final String CHILD_MEMBERSHIP_ATTRIBUTE_DN = "childMembershipAttributeDN";

   public static final String CHILD_MEMBERSHIP_ATTRIBUTE_VIRTUAL = "childMembershipAttributeVirtual";

   public static final String ALLOW_EMPTY_PASSWORD = "allowEmptyPassword";

   public static final String ENCLOSE_PASSWORD_WITH = "enclosePasswordWith";

   public static final String PASSWORD_ENCODIGN = "passwordEncoding";

   public static final String SUBENTRY_MEMBERSHIP_LOOKUP = "subentryMembershipLookup";

   public static final String PASSWORD_UPDATE_ATTRIBUTE_VALUES = "passwordUpdateAttributeValues";


   public SimpleLDAPIdentityObjectTypeConfiguration(IdentityObjectTypeMetaData objectTypeMD)
   {
      this.idAttributeName = objectTypeMD.getOptionSingleValue(ID_ATTRIBUTE_NAME);
      this.passwordAttributeName = objectTypeMD.getOptionSingleValue(PASSWORD_ATTRIBUTE_NAME);
      this.entrySearchFilter = objectTypeMD.getOptionSingleValue(ENTRY_SEARCH_FILTER);
      this.entrySearchScope = objectTypeMD.getOptionSingleValue(ENTRY_SEARCH_SCOPE);
      this.parentMembershipAttributeName = objectTypeMD.getOptionSingleValue(PARENT_MEMBERSHIP_ATTRIBUTE_NAME);
      this.parentMembershipAttributePlaceholder = objectTypeMD.getOptionSingleValue(PARENT_MEMBERSHIP_ATTRIBUTE_PLACEHOLDER);
      this.childMembershipAttributeName = objectTypeMD.getOptionSingleValue(CHILD_MEMBERSHIP_ATTRIBUTE_NAME);
      this.enclosePasswordWith = objectTypeMD.getOptionSingleValue(ENCLOSE_PASSWORD_WITH);
      this.passwordEncoding = objectTypeMD.getOptionSingleValue(PASSWORD_ENCODIGN);


      String allowCreateEntry = objectTypeMD.getOptionSingleValue(ALLOW_CREATE_ENTRY);
      if (allowCreateEntry != null && allowCreateEntry.equalsIgnoreCase("true"))
      {
         this.allowCreateEntry = true;
      }
      else
      {
         this.allowCreateEntry = false;
      }

      String isMembershipAttributeDN = objectTypeMD.getOptionSingleValue(IS_PARENT_MEMBERSHIP_ATTRIBUTE_DN);
      if (isMembershipAttributeDN != null && isMembershipAttributeDN.equalsIgnoreCase("true"))
      {
         this.isParentMembershipAttributeDN = true;
      }
      else
      {
         this.isParentMembershipAttributeDN = false;
      }

      String allowEmptyMemberships = objectTypeMD.getOptionSingleValue(ALLOW_EMPTY_MEMBERSHIPS);
      if (allowEmptyMemberships != null && allowEmptyMemberships.equalsIgnoreCase("true"))
      {
         this.allowEmptyMemberships = true;
      }
      else
      {
         this.allowEmptyMemberships = false;
      }

      String isChildMembershipAttributeDN = objectTypeMD.getOptionSingleValue(CHILD_MEMBERSHIP_ATTRIBUTE_DN);
      if (isChildMembershipAttributeDN != null && isChildMembershipAttributeDN.equalsIgnoreCase("true"))
      {
         this.isChildMembershipAttributeDN = true;
      }
      else
      {
         this.isChildMembershipAttributeDN = false;
      }

      String isChildMembershipAttributeVirtual = objectTypeMD.getOptionSingleValue(CHILD_MEMBERSHIP_ATTRIBUTE_DN);
      if (isChildMembershipAttributeVirtual != null && isChildMembershipAttributeVirtual.equalsIgnoreCase("false"))
      {
         this.isChildMembershipAttributeVirtual = false;
      }
      else
      {
         this.isChildMembershipAttributeVirtual = true;
      }

      String allowEmptyPassword = objectTypeMD.getOptionSingleValue(ALLOW_EMPTY_PASSWORD);
      if (allowEmptyPassword != null && allowEmptyPassword.equalsIgnoreCase("true"))
      {
         this.allowEmptyPassword = true;
      }
      else
      {
         this.allowEmptyPassword = false;
      }

      String subentryMembershipLookup = objectTypeMD.getOptionSingleValue(SUBENTRY_MEMBERSHIP_LOOKUP);
      if (subentryMembershipLookup != null && subentryMembershipLookup.equalsIgnoreCase("true"))
      {
         this.subentryMembershipLookup = true;
      }
      else
      {
         this.subentryMembershipLookup = false;
      }

      List<String> relationships = new LinkedList<String>();

      if (objectTypeMD.getRelationships() != null)
      {
         for (RelationshipMetaData relationshipMetaData : objectTypeMD.getRelationships())
         {
            relationships.add(relationshipMetaData.getIdentityObjectTypeRef());
         }
      }

      allowedMembershipTypes = relationships.toArray(new String[relationships.size()]);
      
      Map<String, String> attrsNames = new HashMap<String, String>();

      for (IdentityObjectAttributeMetaData attributeMetaData : objectTypeMD.getAttributes())
      {
         attrsNames.put(attributeMetaData.getName(), attributeMetaData.getStoreMapping());
      }

      attributeNames = Collections.unmodifiableMap(attrsNames);

      List<String> dns = objectTypeMD.getOption(CTX_DNS);
      if (dns != null)
      {
         this.ctxDNs = dns.toArray(new String[dns.size()]);
      }
      else
      {
         this.ctxDNs = null;
      }

      Map<String, List<String>> createEntryAttributesMap = new HashMap<String, List<String>>();

      List<String> createAttributes = objectTypeMD.getOption(CREATE_ENTRY_ATTRIBUTE_VALUES);

      if (createAttributes != null && createAttributes.size() > 0 )
      {
         for (String attribute : createAttributes)
         {
            String[] parts = attribute.split("=", 2);
            if (parts.length != 2)
            {
               continue;
            }

            String name = parts[0];
            String value = parts[1];

            if (!createEntryAttributesMap.containsKey(name))
            {
               List<String> list = new LinkedList<String>();
               list.add(value);
               createEntryAttributesMap.put(name, list);
            }
            else
            {
               createEntryAttributesMap.get(name).add(value);
            }
         }

         Map<String, String[]> createEntryAttributesArray = new HashMap<String, String[]>();

         for (Map.Entry<String, List<String>> entry : createEntryAttributesMap.entrySet())
         {
            createEntryAttributesArray.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
         }

         this.createEntryAttributeValues = Collections.unmodifiableMap(createEntryAttributesArray);
      }
      else
      {
         this.createEntryAttributeValues = Collections.unmodifiableMap(new HashMap<String, String[]>());
      }



      Map<String, List<String>> updatePasswordAttributesMap = new HashMap<String, List<String>>();

      List<String> passwordUpdateAttributes = objectTypeMD.getOption(PASSWORD_UPDATE_ATTRIBUTE_VALUES);

      if (passwordUpdateAttributes != null && passwordUpdateAttributes.size() > 0 )
      {
         for (String attribute : passwordUpdateAttributes)
         {
            String[] parts = attribute.split("=", 2);
            if (parts.length != 2)
            {
               continue;
            }

            String name = parts[0];
            String value = parts[1];

            if (!updatePasswordAttributesMap.containsKey(name))
            {
               List<String> list = new LinkedList<String>();
               list.add(value);
               updatePasswordAttributesMap.put(name, list);
            }
            else
            {
               updatePasswordAttributesMap.get(name).add(value);
            }
         }

         Map<String, String[]> createEntryAttributesArray = new HashMap<String, String[]>();

         for (Map.Entry<String, List<String>> entry : updatePasswordAttributesMap.entrySet())
         {
            createEntryAttributesArray.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
         }

         this.updatePasswordAttributeValues = Collections.unmodifiableMap(createEntryAttributesArray);
      }
      else
      {
         this.updatePasswordAttributeValues = Collections.unmodifiableMap(new HashMap<String, String[]>());
      }

      //TODO: validate all required options - throw exception for missing ones and set defaults for others



   }



   public String getIdAttributeName()
   {
      return idAttributeName;
   }

   public String[] getCtxDNs()
   {
      return ctxDNs.clone();
   }

   public String getEntrySearchFilter()
   {
      return entrySearchFilter;
   }

   public boolean isAllowCreateEntry()
   {
      return allowCreateEntry;
   }

   public Map<String, String[]> getCreateEntryAttributeValues()
   {
      return createEntryAttributeValues;
   }

   public String[] getAllowedMembershipTypes()
   {
      return allowedMembershipTypes.clone();
   }

   public String getParentMembershipAttributeName()
   {
      return parentMembershipAttributeName;
   }

   public String getParentMembershipAttributePlaceholder()
   {
      return parentMembershipAttributePlaceholder;
   }

   public boolean isParentMembershipAttributeDN()
   {
      return isParentMembershipAttributeDN;
   }

   public boolean isAllowEmptyMemberships()
   {
      return allowEmptyMemberships;
   }

   public String getAttributeMapping(String name)
   {
      return attributeNames.get(name);
   }

   public String getPasswordAttributeName()
   {
      return passwordAttributeName;
   }

  
   public Set<String> getMappedAttributesNames()
   {
      return Collections.unmodifiableSet(attributeNames.keySet());
   }

   public String getEntrySearchScope()
   {
      return entrySearchScope;
   }

   public Map<String, String> getAttributeNames()
   {
      return attributeNames;
   }

   public String getChildMembershipAttributeName()
   {
      return childMembershipAttributeName;
   }

   public boolean isChildMembershipAttributeDN()
   {
      return isChildMembershipAttributeDN;
   }

   public boolean isChildMembershipAttributeVirtual()
   {
      return isChildMembershipAttributeVirtual;
   }

   public boolean isAllowEmptyPassword()
   {
      return allowEmptyPassword;
   }

   public String getEnclosePasswordWith()
   {
      return enclosePasswordWith;
   }

   public String getPasswordEncoding()
   {
      return passwordEncoding;
   }

   public Map<String, String[]> getUpdatePasswordAttributeValues()
   {
      return updatePasswordAttributeValues;
   }

   public boolean isSubentryMembershipLookup()
   {
      return subentryMembershipLookup;
   }
}
