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

package org.picketlink.idm.impl.configuration.jaxb2;

import org.picketlink.idm.impl.configuration.metadata.IdentityConfigurationMetaDataImpl;
import org.picketlink.idm.impl.configuration.metadata.IdentityObjectTypeMetaDataImpl;
import org.picketlink.idm.spi.configuration.metadata.IdentityStoreConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityRepositoryConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.RealmConfigurationMetaData;
import org.picketlink.idm.impl.configuration.metadata.RealmConfigurationMetaDataImpl;
import org.picketlink.idm.impl.configuration.metadata.IdentityRepositoryConfigurationMetaDataImpl;
import org.picketlink.idm.impl.configuration.metadata.IdentityStoreConfigurationMetaDataImpl;
import org.picketlink.idm.impl.configuration.metadata.IdentityStoreMappingMetaDataImpl;
import org.picketlink.idm.impl.configuration.metadata.RelationshipMetaDataImpl;
import org.picketlink.idm.spi.configuration.metadata.IdentityStoreMappingMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.RelationshipMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityObjectAttributeMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityObjectTypeMetaData;
import org.picketlink.idm.common.exception.IdentityConfigurationException;
import org.picketlink.idm.impl.api.attribute.IdentityObjectAttributeMetaDataImpl;
import org.picketlink.idm.impl.configuration.jaxb2.generated.JbossIdentityType;
import org.picketlink.idm.impl.configuration.jaxb2.generated.IdentityStoreType;
import org.picketlink.idm.impl.configuration.jaxb2.generated.RepositoryType;
import org.picketlink.idm.impl.configuration.jaxb2.generated.RealmType;
import org.picketlink.idm.impl.configuration.jaxb2.generated.GroupTypeMappingType;
import org.picketlink.idm.impl.configuration.jaxb2.generated.OptionType;
import org.picketlink.idm.impl.configuration.jaxb2.generated.OptionsType;
import org.picketlink.idm.impl.configuration.jaxb2.generated.IdentityStoreMappingType;
import org.picketlink.idm.impl.configuration.jaxb2.generated.IdentityObjectTypeType;
import org.picketlink.idm.impl.configuration.jaxb2.generated.AttributeType;
import org.picketlink.idm.impl.configuration.jaxb2.generated.RelationshipType;
import org.picketlink.idm.impl.helper.SecurityActions;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @author Tom Baeyens
 * @version : 0.1 $
 */
public abstract class JAXB2IdentityConfiguration
{

   //TODO: check if configuration is consistent. Part of constraints should be checked by schema. Test cases needed :)
   static Set<String> attributeTypes = new HashSet<String>();

   static
   {
      attributeTypes.add("text");
      attributeTypes.add("binary");
   }

   public static IdentityConfigurationMetaData createConfigurationMetaData(File configFile) throws IdentityConfigurationException
   {
      if (configFile == null)
      {
         throw new IllegalArgumentException("Identity config file is null");
      }
      
      InputStream inputStream;
      try
      {
         inputStream = new FileInputStream(configFile);
      } catch (FileNotFoundException e)
      {
         throw new IllegalArgumentException("Identity config file "+configFile.getAbsolutePath()+" does not exist");
      }
      return createConfigurationMetaData(inputStream);
   }

   public static IdentityConfigurationMetaData createConfigurationMetaData(String configResource) throws IdentityConfigurationException
   {

      ClassLoader classLoader = SecurityActions.getContextClassLoader();
      InputStream inputStream = classLoader.getResourceAsStream(configResource);
      if (inputStream == null)
      {
         throw new IllegalArgumentException("Resource "+configResource+" does not exist");
      }
      return createConfigurationMetaData(inputStream);
   }

   public static IdentityConfigurationMetaData createConfigurationMetaData(InputStream configInputStream) throws IdentityConfigurationException
   {
      if (configInputStream == null)
      {
         throw new IllegalArgumentException("no config resource");
      }

      JAXBElement<JbossIdentityType> jiElement = null;

      Object o = null;
      try
      {
         JAXBContext jaxbContext = JAXBContext.newInstance("org.picketlink.idm.impl.configuration.jaxb2.generated");
         Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

         jiElement = (JAXBElement<JbossIdentityType>)unMarshaller.unmarshal(configInputStream);
      }
      catch (JAXBException e)
      {
         throw new IdentityConfigurationException("Cannot unmarshal xml configuration: ", e);
      }

      JbossIdentityType identityConfig = jiElement.getValue();

      IdentityConfigurationMetaDataImpl configurationMD = new IdentityConfigurationMetaDataImpl();

      if (identityConfig.getStores() != null &&
         identityConfig.getStores().getIdentityStores() != null &&
         identityConfig.getStores().getIdentityStores().getIdentityStore() != null)
      {

         for (IdentityStoreType identityStoreType : identityConfig.getStores().getIdentityStores().getIdentityStore())
         {
            configurationMD.getIdentityStores().add(createIdentityStoreConfigurationMetaData(identityStoreType));
         }
      }

      if (identityConfig.getRepositories() != null &&
         identityConfig.getRepositories().getRepository() != null)
      {

         for (RepositoryType repositoryType : identityConfig.getRepositories().getRepository())
         {
            configurationMD.getRepositories().add(createIdentityRepositoryConfigurationMetaData(repositoryType));
         }
      }

      if (identityConfig.getRealms() != null &&
         identityConfig.getRealms().getRealm() != null)
      {
         for (RealmType realmType : identityConfig.getRealms().getRealm())
         {
            configurationMD.getRealms().add(createRealmConfigurationMetaData(realmType));
         }
      }

      checkDataConsistency(configurationMD);
      
      return configurationMD;

   }

   private static RealmConfigurationMetaData createRealmConfigurationMetaData(RealmType realmType)
   {
      RealmConfigurationMetaDataImpl realmMD = new RealmConfigurationMetaDataImpl();

      realmMD.setId(realmType.getId());
      if (realmType.getIdentityTypeMappings() != null)
      {
         realmMD.setIdentityMapping(realmType.getIdentityTypeMappings().getIdentityMapping());
      }
      realmMD.setIdentityRepositoryIdRef(realmType.getRepositoryIdRef());

      Map<String, String> groupMappings = new HashMap<String, String>();

      if (realmType.getIdentityTypeMappings() != null
         && realmType.getIdentityTypeMappings().getGroupTypeMapping() != null)
      {
         for (GroupTypeMappingType groupTypeMappingType : realmType.getIdentityTypeMappings().getGroupTypeMapping())
         {
            groupMappings.put(groupTypeMappingType.getGroupTypeName(), groupTypeMappingType.getIdentityObjectTypeName());
         }
      }

      realmMD.setGroupTypeMappings(groupMappings);

      realmMD.setOptions(createOptions(realmType.getOptions()));


      return realmMD;
   }


   private static IdentityRepositoryConfigurationMetaData createIdentityRepositoryConfigurationMetaData(RepositoryType repositoryType)
   {
      IdentityRepositoryConfigurationMetaDataImpl repoMD = new IdentityRepositoryConfigurationMetaDataImpl();

      repoMD.setId(repositoryType.getId());
      repoMD.setClassName(repositoryType.getClazz());
      if (repositoryType.getExternalConfig() != null)
      {
         repoMD.setExternalConfig(repositoryType.getExternalConfig().getValue());
      }
      repoMD.setDefaultAttributeStroeId(repositoryType.getDefaultAttributeStoreId());
      repoMD.setDefaultIdentityStoreId(repositoryType.getDefaultIdentityStoreId());

      List<IdentityStoreMappingMetaData> storeMappings = new LinkedList<IdentityStoreMappingMetaData>();

      if (repositoryType.getIdentityStoreMappings() != null &&
         repositoryType.getIdentityStoreMappings().getIdentityStoreMapping() != null)
      {

         for (IdentityStoreMappingType identityStoreMappingType : repositoryType.getIdentityStoreMappings().getIdentityStoreMapping())
         {
            IdentityStoreMappingMetaDataImpl mapping = new IdentityStoreMappingMetaDataImpl();

            mapping.setIdentityStoreId(identityStoreMappingType.getIdentityStoreId());
            mapping.setIdentityObjectTypeMappings(identityStoreMappingType.getIdentityObjectTypes().getIdentityObjectType());
            mapping.setOptions(createOptions(identityStoreMappingType.getOptions()));

            storeMappings.add(mapping);
         }
      }

      repoMD.setIdentityStoreToIdentityObjectTypeMappings(storeMappings);

      repoMD.setOptions(createOptions(repositoryType.getOptions()));


      return repoMD;
   }

   private static IdentityStoreConfigurationMetaData createIdentityStoreConfigurationMetaData(IdentityStoreType identityStoreType)
   {
      IdentityStoreConfigurationMetaDataImpl storeMD = new IdentityStoreConfigurationMetaDataImpl();

      storeMD.setId(identityStoreType.getId());
      storeMD.setClassName(identityStoreType.getClazz());

      if (identityStoreType.getExternalConfig() != null)
      {
         storeMD.setExternalConfig(identityStoreType.getExternalConfig().getValue());
      }

      storeMD.setSupportedRelationshipTypes(identityStoreType.getSupportedRelationshipTypes().getRelationshipType());

      if (identityStoreType.getSupportedIdentityObjectTypes() != null &&
         identityStoreType.getSupportedIdentityObjectTypes().getIdentityObjectType() != null)

      {

         for (IdentityObjectTypeType identityObjectTypeType : identityStoreType.getSupportedIdentityObjectTypes().getIdentityObjectType())
         {
            IdentityObjectTypeMetaDataImpl identityObjectTypeMD = new IdentityObjectTypeMetaDataImpl();

            identityObjectTypeMD.setName(identityObjectTypeType.getName());


            // Attributes

            List<IdentityObjectAttributeMetaData> attributes = new LinkedList<IdentityObjectAttributeMetaData>();

            if (identityObjectTypeType.getAttributes() != null &&
               identityObjectTypeType.getAttributes().getAttribute() != null)
            {

               for (AttributeType attributeType : identityObjectTypeType.getAttributes().getAttribute())
               {

                  String readOnly = attributeType.getIsReadOnly();
                  String multivalued = attributeType.getIsMultivalued();
                  String required = attributeType.getIsRequired();
                  String unique = attributeType.getIsUnique();

                  IdentityObjectAttributeMetaDataImpl attributeMD = new IdentityObjectAttributeMetaDataImpl(
                     attributeType.getName(),
                     attributeType.getMapping(),
                     attributeType.getType(),
                     (readOnly != null && readOnly.equalsIgnoreCase("true")),
                     (multivalued != null && multivalued.equalsIgnoreCase("true")),
                     (required != null && required.equalsIgnoreCase("true")),
                     (unique != null && unique.equalsIgnoreCase("true"))
                  );

                  attributes.add(attributeMD);
               }
            }

            identityObjectTypeMD.setAttributes(attributes);

            // Credentials

            List<String> credentials = new LinkedList<String>();

            if (identityObjectTypeType.getCredentials() != null &&
               identityObjectTypeType.getCredentials().getCredentialType() != null)
            {
               for (String credentialType : identityObjectTypeType.getCredentials().getCredentialType())
               {
                  credentials.add(credentialType);
               }
            }

            identityObjectTypeMD.setCredentials(credentials);

            // Relationships

            List<RelationshipMetaData> relationships = new LinkedList<RelationshipMetaData>();

            if (identityObjectTypeType.getRelationships() != null &&
               identityObjectTypeType.getRelationships().getRelationship() != null)
            {
               for (RelationshipType relationshipType : identityObjectTypeType.getRelationships().getRelationship())
               {
                  RelationshipMetaDataImpl relMD = new RelationshipMetaDataImpl();
                  relMD.setIdentityObjectTypeRef(relationshipType.getIdentityObjectTypeRef());
                  relMD.setRelationshipTypeRef(relationshipType.getRelationshipTypeRef());
                  relationships.add(relMD);
               }
            }

            identityObjectTypeMD.setRelationships(relationships);

            // Options

            Map<String, List<String>> options = new HashMap<String, List<String>>();

            if (identityObjectTypeType.getOptions() != null &&
               identityObjectTypeType.getOptions().getOption() != null)
            {

               for (OptionType optionType : identityObjectTypeType.getOptions().getOption())
               {
                  options.put(optionType.getName(), optionType.getValue());
               }
            }
            
            identityObjectTypeMD.setOptions(options);

            storeMD.getSupportedIdentityTypes().add(identityObjectTypeMD);
         }
      }


      Map<String, List<String>> options = new HashMap<String, List<String>>();

      if (identityStoreType.getOptions() != null &&
         identityStoreType.getOptions().getOption() != null)
      {

         for (OptionType optionType : identityStoreType.getOptions().getOption())
         {
            options.put(optionType.getName(), optionType.getValue());
         }
      }
      
      storeMD.setOptions(options);

      return storeMD;
   }

   private static Map<String, List<String>> createOptions(OptionsType optionsType)
   {
      Map<String, List<String>> options = new HashMap<String, List<String>>();

      if (optionsType != null &&
         optionsType.getOption() != null)
      {
         for (OptionType optionType : optionsType.getOption())
         {
            options.put(optionType.getName(), optionType.getValue());
         }
      }

      return options;
   }

   public static void checkDataConsistency(IdentityConfigurationMetaData configurationMD) throws IdentityConfigurationException
   {

      // IdentityStore

      if (configurationMD.getRepositories() == null || configurationMD.getIdentityStores().size() == 0)
      {
         throw new IdentityConfigurationException("No identity-store configured");
      }

      // Helper structure to keep track of all IdentityObjectType mappings in stores
      Map<String, Set<String>> storeObjectTypeNameMappings = new HashMap<String, Set<String>>();

      for (IdentityStoreConfigurationMetaData storeMD : configurationMD.getIdentityStores())
      {

         // id
         if (storeMD.getId() == null || storeMD.getId().length() == 0)
         {
            throw new IdentityConfigurationException("identity-store name required");
         }

         // Helper structure containing all configured identity object type names

         Set<String> storeObjectTypeNames = new HashSet<String>();
         if (storeMD.getSupportedIdentityTypes() != null)
         {
            for (IdentityObjectTypeMetaData typeMD : storeMD.getSupportedIdentityTypes())
            {
               storeObjectTypeNames.add(typeMD.getName());
            }
         }

         storeObjectTypeNameMappings.put(storeMD.getId(), storeObjectTypeNames);

         // className
         if (storeMD.getClassName() == null || storeMD.getClassName().length() == 0)
         {
            throw new IdentityConfigurationException("identity-store \"" + storeMD.getId() + "\" class name required");
         }

         // supported relationship types are not required but we gather the names to check consistency in other parts

         Set<String> supportedRelTypes = new HashSet<String>();
         if (storeMD.getSupportedRelationshipTypes() != null)
         {
            supportedRelTypes = new HashSet<String>(storeMD.getSupportedRelationshipTypes());
         }

         // all configured identity object types
//         if (storeMD.getSupportedIdentityTypes() == null || storeMD.getSupportedIdentityTypes().size() == 0)
//         {
//            throw new IdentityConfigurationException("identity-store \"" + storeMD.getKey() + "\" doesn't have any supported " +
//               "identity-object-types configured");
//         }

         // check each configured types
         for (IdentityObjectTypeMetaData typeMD : storeMD.getSupportedIdentityTypes())
         {
            // Name
            if (typeMD.getName() == null || typeMD.getName().length() == 0)
            {
               throw new IdentityConfigurationException("identity-store \"" + storeMD.getId() + "\" identity-object-type name" +
                  "is not specified");
            }

            // Attributes

            if (typeMD.getAttributes() != null)
            {
               for (IdentityObjectAttributeMetaData attrMD : typeMD.getAttributes())
               {
                  // Name
                  if (attrMD.getName() == null || attrMD.getName().length() == 0)
                  {
                     throw new IdentityConfigurationException("Attribute name not specified in identity-store \"" + storeMD.getId() + "\"");
                  }

                  if (attrMD.getType() == null || attrMD.getType().length() == 0)
                  {
                     throw new IdentityConfigurationException("Attribute type not specified for attribute \"" + attrMD.getName()
                        + "\" in identity-store \"" + storeMD.getId() + "\"");
                  }

                  if (!attributeTypes.contains(attrMD.getType()))
                  {
                     throw new IdentityConfigurationException("Unsupported attribute type in attribute \"" + attrMD.getName()
                        + "\" in identity-store \"" + storeMD.getId() + "\"");
                  }

               }
            }

            // Relationships

            if (typeMD.getRelationships() != null)
            {
               for (RelationshipMetaData relMD : typeMD.getRelationships())
               {
                  if (relMD.getIdentityObjectTypeRef() == null)
                  {
                     throw new IdentityConfigurationException("identity-object-type-ref not specified" +
                        "in identity-object-type \"" + typeMD.getName()
                        + "\" in identity-store \"" + storeMD.getId() + "\"");
                  }

                  if (!storeObjectTypeNames.contains(relMD.getIdentityObjectTypeRef()))
                  {
                     throw new IdentityConfigurationException("identity-object-type-ref contains " +
                        "not configured name \"" + relMD.getIdentityObjectTypeRef() + "\" in " +
                        "identity-object-type \"" + typeMD.getName()
                        + "\" in identity-store \"" + storeMD.getId() + "\"");
                  }

                  if (relMD.getRelationshipTypeRef() == null)
                  {
                     throw new IdentityConfigurationException("relationship-type-ref not specified" +
                        "in identity-object-type \"" + typeMD.getName()
                        + "\" in identity-store \"" + storeMD.getId() + "\"");
                  }

                  if (!supportedRelTypes.contains(relMD.getRelationshipTypeRef()))
                  {
                     throw new IdentityConfigurationException("relationship-type-ref name is not supported" +
                        "by identity-store. Relationship name \"" + relMD.getRelationshipTypeRef() + "\" in " +
                        "identity-object-type \"" + typeMD.getName()
                        + "\" in identity-store \"" + storeMD.getId() + "\"");
                  }
               }
            }
         }
      }

      // Helper structures

      Set<String> configuredRepoNames = new HashSet<String>();

      Map<String, Set<String>> repoObjectTypeNamesMappings = new HashMap<String, Set<String>>();

      // IdentityStoreRepository

      if (configurationMD.getRepositories() == null || configurationMD.getRepositories().size() == 0)
      {
         throw new IdentityConfigurationException("No IdentityRepository configured");
      }

      for (IdentityRepositoryConfigurationMetaData repoMD : configurationMD.getRepositories())
      {
         Set<String> repoObjectNames = new HashSet<String>();

         // id
         if (repoMD.getId() == null || repoMD.getId().length() == 0)
         {
            throw new IdentityConfigurationException("repository name is required");
         }

         configuredRepoNames.add(repoMD.getId());

         // className
         if (repoMD.getClassName() == null || repoMD.getClassName().length() == 0)
         {
            throw new IdentityConfigurationException("repository \"" + repoMD.getId() + "\" class name required");
         }

         // defaultAttributeStore
         if (repoMD.getDefaultAttributeStoreId() == null || repoMD.getDefaultAttributeStoreId().length() == 0)
         {
            throw new IdentityConfigurationException("default-attribute-store in repository \"" + repoMD.getId() + "\" is required");
         }

         if (!storeObjectTypeNameMappings.containsKey(repoMD.getDefaultAttributeStoreId()))
         {
            throw new IdentityConfigurationException("default-attribute-store \"" + repoMD.getDefaultAttributeStoreId() +
               "in repository \"" + repoMD.getId() + "\" is not present in configuration");
         }

         // defaultIdentityStore
         if (repoMD.getDefaultAttributeStoreId() == null || repoMD.getDefaultAttributeStoreId().length() == 0)
         {
            throw new IdentityConfigurationException("default-identity-store in repository \"" + repoMD.getId() + "\" is required");
         }

         if (!storeObjectTypeNameMappings.containsKey(repoMD.getDefaultIdentityStoreId()))
         {
            throw new IdentityConfigurationException("default-identity-store \"" + repoMD.getDefaultIdentityStoreId() +
               "in repository \"" + repoMD.getId() + "\" is not present in configuration");
         }

         

//         if (repoMD.getIdentityStoreToIdentityObjectTypeMappings() == null ||
//            repoMD.getIdentityStoreToIdentityObjectTypeMappings().size() == 0)
//         {
//            throw new IdentityConfigurationException("No identity-store-mappings defined in repository \"" + repoMD.getKey() + "\"");
//         }

         // If there are no repo mappings then add all mappings from the default store
         if (repoMD.getIdentityStoreToIdentityObjectTypeMappings().size() == 0)
         {
            Set<String> names = storeObjectTypeNameMappings.get(repoMD.getDefaultIdentityStoreId());
            repoObjectTypeNamesMappings.put(repoMD.getId(), names);
         }

         for (IdentityStoreMappingMetaData mappingsMD : repoMD.getIdentityStoreToIdentityObjectTypeMappings())
         {
            if (mappingsMD.getIdentityStoreId() == null ||
               mappingsMD.getIdentityStoreId().length() == 0)
            {
               throw new IdentityConfigurationException("No identity-store-mappings defined in repository \"" + repoMD.getId() + "\"");
            }

            if (!storeObjectTypeNameMappings.containsKey(mappingsMD.getIdentityStoreId()))
            {
               throw new IdentityConfigurationException("Store with id from identity-store-id \"" + mappingsMD.getIdentityStoreId() +
                  "in identity-store-mapping in repository \"" + repoMD.getId() + "\" is not present in configuration");
            }

            if (mappingsMD.getIdentityObjectTypeMappings() == null ||
               mappingsMD.getIdentityObjectTypeMappings().size() == 0)
            {
               throw new IdentityConfigurationException("identity-store-mapping with \"" + mappingsMD.getIdentityStoreId() +
                  "in repository \"" + repoMD.getId() + "\" doesn't have any identity-object-types listed");
            }

            for (String identityTypeName : mappingsMD.getIdentityObjectTypeMappings())
            {
               Set<String> validNames = storeObjectTypeNameMappings.get(mappingsMD.getIdentityStoreId());

               if (!validNames.contains(identityTypeName))
               {
                  throw new IdentityConfigurationException("identity-object-type \"" + identityTypeName + "\" specified in " +
                     "identity-store-mapping in repository \"" + repoMD.getId() + "\" is not configured in specified " +
                     "identity-store");
               }

               repoObjectNames.add(identityTypeName);
            }
         }

         repoObjectTypeNamesMappings.put(repoMD.getId(), repoObjectNames);
      }


      // Realms

      if (configurationMD.getRealms() == null || configurationMD.getRealms().size() == 0)
      {
         throw new IdentityConfigurationException("No realm configured");
      }

      for (RealmConfigurationMetaData realmMD : configurationMD.getRealms())
      {
         if (realmMD.getId() == null || realmMD.getId().length() == 0)
         {
            throw new IdentityConfigurationException("realm id is missing");
         }

         if (realmMD.getIdentityRepositoryIdRef() == null || realmMD.getIdentityRepositoryIdRef().length() == 0)
         {
            throw new IdentityConfigurationException("repository-id-ref in realm \"" + realmMD.getId() + "\" is missing");
         }

         if (!configuredRepoNames.contains(realmMD.getIdentityRepositoryIdRef()))
         {
            throw new IdentityConfigurationException("repository-id-ref \"" + realmMD.getIdentityRepositoryIdRef() +
               "\" in realm \"" + realmMD.getId() + "\" doesn't reference configured repository");
         }

         if (realmMD.getIdentityMapping() == null || realmMD.getIdentityMapping().length() == 0)
         {
            throw new IdentityConfigurationException("identity-mapping in realm \"" + realmMD.getId() + "\" is missing");
         }

         // Group type mappings are not required
         if (realmMD.getGroupTypeMappings() != null )
         {
            Set<String> validNames = repoObjectTypeNamesMappings.get(realmMD.getIdentityRepositoryIdRef());

            for (String typeName : realmMD.getGroupTypeMappings().values())
            {
               if (!validNames.contains(typeName))
               {
                  throw new IdentityConfigurationException("identity-object-type-name in realm \"" + realmMD.getId() + "\" " +
                     "doesn't reference identity-object-type configured in repository \"" + realmMD.getIdentityRepositoryIdRef() +
                  "\"");

               }
            }
         }
      }



   }

}
