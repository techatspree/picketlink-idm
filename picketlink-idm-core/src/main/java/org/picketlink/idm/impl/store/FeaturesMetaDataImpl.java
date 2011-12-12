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

package org.picketlink.idm.impl.store;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.spi.configuration.metadata.IdentityObjectTypeMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityStoreConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.RelationshipMetaData;
import org.picketlink.idm.spi.model.IdentityObjectCredentialType;
import org.picketlink.idm.spi.model.IdentityObjectRelationshipType;
import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.spi.store.FeaturesMetaData;
import org.picketlink.idm.spi.store.IdentityObjectSearchCriteriaType;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class FeaturesMetaDataImpl implements FeaturesMetaData, Serializable
{
   private Set<String> supportedTypeNames = new HashSet<String>();

   private final Set<IdentityObjectSearchCriteriaType> supportedSearchConstraintTypes;

   private final Map<String, Set<String>> supportedCredentials;

   private final boolean namedRelationshipsSupport;

   private final Set<String> readOnlyObjectTypes;

   private final boolean relationshipPropertiesSupport;

   // <Relationship Type, <From IdentityType, To IdentityType>>
   private final Map<String, Map<String, Set<String>>> supportedRelationshipMappings = new HashMap<String, Map<String, Set<String>>>();


   public FeaturesMetaDataImpl(IdentityStoreConfigurationMetaData configurationMD,
                               Set<IdentityObjectSearchCriteriaType> supportedCriteria,
                               boolean relationshipPropertiesSupport,
                               boolean namedRelationshipsSupport,
                               Set<String> readOnlyObjectTypes)
   {

      this.relationshipPropertiesSupport = relationshipPropertiesSupport;
      this.namedRelationshipsSupport = namedRelationshipsSupport;
      this.readOnlyObjectTypes = readOnlyObjectTypes;

      Map<String, Set<String>> supportedCredentials = new HashMap<String, Set<String>>();

      for (IdentityObjectTypeMetaData typeMetaData : configurationMD.getSupportedIdentityTypes())
      {
         supportedTypeNames.add(typeMetaData.getName());

         if (typeMetaData.getCredentials() != null)
         {
            Set<String> credentials = new HashSet<String>(typeMetaData.getCredentials());
            supportedCredentials.put(typeMetaData.getName(), credentials);
         }

      }

      supportedTypeNames = Collections.unmodifiableSet(supportedTypeNames);

      this.supportedCredentials = Collections.unmodifiableMap(supportedCredentials);

      this.supportedSearchConstraintTypes = Collections.unmodifiableSet(supportedCriteria);




      // Supported relationships

      for (IdentityObjectTypeMetaData identityObjectTypeMetaData : configurationMD.getSupportedIdentityTypes())
      {
         String fromTypeName = identityObjectTypeMetaData.getName();

         if (identityObjectTypeMetaData.getRelationships() == null)
         {
            continue;
         }

         for (RelationshipMetaData relationshipMetaData : identityObjectTypeMetaData.getRelationships())
         {
            String relationshipName = relationshipMetaData.getRelationshipTypeRef();
            String toTypeName = relationshipMetaData.getIdentityObjectTypeRef();

            // Populate

            Map<String, Set<String>> mappings = null;

            if (supportedRelationshipMappings.containsKey(relationshipName))
            {
               mappings = supportedRelationshipMappings.get(relationshipName);

            }
            else
            {
               mappings = new HashMap<String, Set<String>>();
               supportedRelationshipMappings.put(relationshipName, mappings);
            }

            Set<String> toTypes = null;
            if (mappings.containsKey(fromTypeName))
            {
               toTypes = mappings.get(fromTypeName);
            }
            else
            {
               toTypes = new HashSet<String>();
               mappings.put(fromTypeName, toTypes);
            }

            toTypes.add(toTypeName);

            
         }


      }

   }

   public boolean isIdentityObjectAddRemoveSupported(IdentityObjectType objectType)
   {
      if (readOnlyObjectTypes.contains(objectType.getName()))
      {
         return false;
      }

      return true;
   }

   public boolean isRelationshipNameAddRemoveSupported()
   {
      // For now just assume that if store supports named relationships, names can be altered
      return namedRelationshipsSupport;
   }

   public boolean isNamedRelationshipsSupported()
   {
      return namedRelationshipsSupport;
   }

   public boolean isRelationshipPropertiesSupported()
   {
      return relationshipPropertiesSupport;
   }

   public boolean isSearchCriteriaTypeSupported(IdentityObjectType identityObjectType, IdentityObjectSearchCriteriaType storeSearchConstraint)
   {
      if (supportedSearchConstraintTypes.contains(storeSearchConstraint))
      {
         return true;
      }
      return false;
   }

   public Set<String> getSupportedIdentityObjectTypes()
   {
      return supportedTypeNames;
   }

   public boolean isIdentityObjectTypeSupported(IdentityObjectType identityObjectType)
   {
      if (supportedTypeNames.contains(identityObjectType.getName()))
      {
         return true;
      }
      return false;
   }

   public boolean isRelationshipTypeSupported(IdentityObjectType fromType, IdentityObjectType toType, IdentityObjectRelationshipType relationshipType) throws IdentityException
   {
      Map<String, Set<String>> mappings = supportedRelationshipMappings.get(relationshipType.getName());
      if (mappings != null && mappings.containsKey(fromType.getName()))
      {
         if (mappings.get(fromType.getName()).contains(toType.getName()))
         {
            return true;
         }
      }
      return false;
   }

   public Set<String> getSupportedRelationshipTypes()
   {
      return supportedRelationshipMappings.keySet();
   }

   public boolean isCredentialSupported(IdentityObjectType identityObjectType, IdentityObjectCredentialType credentialType)
   {
      Set<String> types = supportedCredentials.get(identityObjectType.getName());
      if (types != null && types.contains(credentialType.getName()))
      {
         return true;
      }
      return false;
   }

   public boolean isRoleNameSearchCriteriaTypeSupported(IdentityObjectSearchCriteriaType constraint)
   {
      // For now simple - use the same allowed criteria list for everything
      return isNamedRelationshipsSupported() && isSearchCriteriaTypeSupported(null, constraint);
   }

}
