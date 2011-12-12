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

import org.picketlink.idm.spi.configuration.metadata.RealmConfigurationMetaData;

import java.io.Serializable;
import java.util.Map;
import java.util.List;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class RealmConfigurationMetaDataImpl implements RealmConfigurationMetaData, Serializable
{

   private String id;

   private String identityRepositoryIdRef;

   private String identityMapping;

   private Map<String, String> groupTypeMappings;

   private Map<String, List<String>> options;


   public RealmConfigurationMetaDataImpl()
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

   public String getIdentityRepositoryIdRef()
   {
      return identityRepositoryIdRef;
   }

   public void setIdentityRepositoryIdRef(String identityRepositoryIdRef)
   {
      this.identityRepositoryIdRef = identityRepositoryIdRef;
   }

   public String getIdentityMapping()
   {
      return identityMapping;
   }

   public void setIdentityMapping(String identityMapping)
   {
      this.identityMapping = identityMapping;
   }

   public Map<String, String> getGroupTypeMappings()
   {
      return groupTypeMappings;
   }

   public void setGroupTypeMappings(Map<String, String> groupTypeMappings)
   {
      this.groupTypeMappings = groupTypeMappings;
   }

   public Map<String, List<String>> getOptions()
   {
      return options;
   }

   public void setOptions(Map<String, List<String>> options)
   {
      this.options = options;
   }

   List<String> getOption(String optionName)
   {
      if (options != null )
      {
         return options.get(optionName);
      }

      return null;
   }

   String getOptionSingleValue(String optionName)
   {
      List<String> values = getOption(optionName);

      if (values != null && values.size() > 0)
      {
         return values.get(0); 
      }

      return null;
   }
}
