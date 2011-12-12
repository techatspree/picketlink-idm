/*
* JBoss, a division of Red Hat
* Copyright 2009, Red Hat Middleware, LLC, and individual contributors as indicated
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


package org.picketlink.idm.impl.cache;

import java.io.Serializable;

import org.picketlink.idm.cache.RoleSearch;
import org.picketlink.idm.api.RoleType;

public class RoleSearchImpl extends AbstractSearch implements RoleSearch, Serializable
{

   private String identityTypeId;

   private RoleType roleType;

   public String getIdentityTypeId()
   {
      return identityTypeId;
   }

   public void setIdentityTypeId(String identityTypeId)
   {
      this.identityTypeId = identityTypeId;
   }

   public RoleType getRoleType()
   {
      return roleType;
   }

   public void setRoleType(RoleType roleType)
   {
      this.roleType = roleType;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o == null || getClass() != o.getClass())
      {
         return false;
      }

      RoleSearchImpl that = (RoleSearchImpl)o;

      if (identityTypeId != null ? !identityTypeId.equals(that.identityTypeId) : that.identityTypeId != null)
      {
         return false;
      }
      if (roleType != null ? !roleType.equals(that.roleType) : that.roleType != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (identityTypeId != null ? identityTypeId.hashCode() : 0);
      result = 31 * result + (roleType != null ? roleType.hashCode() : 0);
      return result;
   }
}
