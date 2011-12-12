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

package org.picketlink.idm.impl.api.session.mapper;

import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.impl.types.SimpleIdentityObjectType;
import org.picketlink.idm.impl.api.session.mapper.IdentityObjectTypeMapper;

import java.io.Serializable;
import java.util.Map;
import java.util.Collections;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class IdentityObjectTypeMapperImpl implements IdentityObjectTypeMapper, Serializable
{

   /**
    * IdentityObjectType --> GroupType
    */
   private final Map<String, String> groupTypeMap;

   private final String identityTypeName;

   public IdentityObjectTypeMapperImpl(Map<String, String> map, String identityTypeName)
   {
      groupTypeMap = Collections.unmodifiableMap(map);
      this.identityTypeName = identityTypeName;
   }

   public IdentityObjectType getIdentityObjectType()
   {
      return new SimpleIdentityObjectType(identityTypeName);
   }

   public IdentityObjectType getIdentityObjectType(String groupType)
   {
      if (groupType == null)
      {
         throw new IllegalArgumentException("groupType is null");
      }

      for (Map.Entry<String, String> entry : groupTypeMap.entrySet())
      {
         if (entry.getValue().equals(groupType))
         {
            return new SimpleIdentityObjectType(entry.getKey());
         }
      }

      return null;
   }

   public String getGroupType(IdentityObjectType identityObjectType)
   {
      if (identityObjectType == null)
      {
         throw new IllegalArgumentException("identityObjectType is null");
      }

      return groupTypeMap.get(identityObjectType.getName());

   }

   public boolean isGroupType(IdentityObjectType identityObjectType)
   {
      if (identityObjectType == null)
      {
         throw new IllegalArgumentException("identityObjectType is null");
      }

      if (isIdentity(identityObjectType))
      {
         return false;
      }

      if (groupTypeMap.containsKey(identityObjectType.getName()))
      {
         return true;
      }

      return false;
   }


   public boolean isIdentity(IdentityObjectType identityObjectType)
   {
      if (identityObjectType == null)
      {
         throw new IllegalArgumentException("identityObjectType is null");
      }

      if (identityObjectType.getName().equals(identityTypeName))
      {
         return true;
      }
      return false;
   }
}
