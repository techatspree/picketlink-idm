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

package org.picketlink.idm.impl.configuration.metadata;

import org.picketlink.idm.spi.configuration.metadata.IdentityRepositoryConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityStoreMappingMetaData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class IdentityRepositoryConfigurationMetaDataImpl implements IdentityRepositoryConfigurationMetaData, Serializable
{

   private String id;

   private String className;

   private String externalConfig;

   private String defaultIdentityStoreId;

   private String defaultAttributeStroeId;

   private List<IdentityStoreMappingMetaData> identityStoreToIdentityObjectTypeMappings;

   private Map<String, List<String>> options;


   public IdentityRepositoryConfigurationMetaDataImpl()
   {
   }

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public String getClassName()
   {
      return className;
   }

   public void setClassName(String className)
   {
      this.className = className;
   }

   public String getExternalConfig()
   {
      return externalConfig;
   }

   public void setExternalConfig(String externalConfig)
   {
      this.externalConfig = externalConfig;
   }

   public String getDefaultIdentityStoreId()
   {
      return defaultIdentityStoreId;
   }

   public void setDefaultIdentityStoreId(String defaultIdentityStoreId)
   {
      this.defaultIdentityStoreId = defaultIdentityStoreId;
   }

   public String getDefaultAttributeStoreId()
   {
      return defaultAttributeStroeId;
   }

   public void setDefaultAttributeStroeId(String defaultAttributeStroeId)
   {
      this.defaultAttributeStroeId = defaultAttributeStroeId;
   }

   public List<IdentityStoreMappingMetaData> getIdentityStoreToIdentityObjectTypeMappings()
   {
      return identityStoreToIdentityObjectTypeMappings;
   }

   public void setIdentityStoreToIdentityObjectTypeMappings(List<IdentityStoreMappingMetaData> identityStoreToIdentityObjectTypeMappings)
   {
      this.identityStoreToIdentityObjectTypeMappings = identityStoreToIdentityObjectTypeMappings;
   }

   public Map<String, List<String>> getOptions()
   {
      return options;
   }

   public void setOptions(Map<String, List<String>> options)
   {
      this.options = options;
   }

   public List<String> getOption(String optionName)
   {
      if (options != null )
      {
         return options.get(optionName);
      }

      return null;
   }

   public String getOptionSingleValue(String optionName)
   {
      List<String> values = getOption(optionName);

      if (values != null && values.size() > 0)
      {
         return values.get(0);
      }

      return null;
   }

}
