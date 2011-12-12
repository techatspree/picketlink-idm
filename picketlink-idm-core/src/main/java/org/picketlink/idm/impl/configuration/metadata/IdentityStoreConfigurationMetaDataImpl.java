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

import org.picketlink.idm.spi.configuration.metadata.IdentityStoreConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityObjectTypeMetaData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class IdentityStoreConfigurationMetaDataImpl implements IdentityStoreConfigurationMetaData, Serializable
{

   private String id;

   private String className;

   private String externalConfig;

   private List<String> supportedRelationshipTypes = new LinkedList<String>();

   private List<IdentityObjectTypeMetaData> supportedIdentityTypes = new LinkedList<IdentityObjectTypeMetaData>();

   private Map<String, List<String>> options = new HashMap<String, List<String>>();


   public IdentityStoreConfigurationMetaDataImpl()
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

   public List<String> getSupportedRelationshipTypes()
   {
      return supportedRelationshipTypes;
   }

   public void setSupportedRelationshipTypes(List<String> supportedRelationshipTypes)
   {
      this.supportedRelationshipTypes = supportedRelationshipTypes;
   }

   public List<IdentityObjectTypeMetaData> getSupportedIdentityTypes()
   {
      return supportedIdentityTypes;
   }

   public void setSupportedIdentityTypes(List<IdentityObjectTypeMetaData> supportedIdentityTypes)
   {
      this.supportedIdentityTypes = supportedIdentityTypes;
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
