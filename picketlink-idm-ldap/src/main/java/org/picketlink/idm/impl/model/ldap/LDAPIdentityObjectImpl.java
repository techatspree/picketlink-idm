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

package org.picketlink.idm.impl.model.ldap;

import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.common.exception.PolicyValidationException;

import java.util.Set;
import java.util.Map;
import java.io.Serializable;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class LDAPIdentityObjectImpl implements IdentityObject, Serializable
{
   private String dn;

   private String id;

   private IdentityObjectType type;

   public LDAPIdentityObjectImpl(String dn, String id, IdentityObjectType type)
   {
      this.dn = dn;
      this.id = id;
      this.type = type;
   }

   public String getId()
   {
      return dn;
   }

   public String getDn()
   {
      return dn;
   }

   public String getName()
   {
      return id;
   }

   public IdentityObjectType getIdentityType()
   {
      return type;
   }

   public Map<String, Set<String>> getAttributes()
   {
      return null;
   }

   public void validatePolicy() throws PolicyValidationException
   {

   }

   @Override
   public String toString()
   {
      return "IdentityObject[id=" + getId() + "; name="  + getName() + "; type=" + getIdentityType().getName() + "]";
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof IdentityObject))
      {
         return false;
      }

      IdentityObject that = (IdentityObject)o;

      if (id != null ? !id.equals(that.getName()) : that.getName() != null)
      {
         return false;
      }
      if (type != null ? !type.equals(that.getIdentityType()) : that.getIdentityType() != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = id != null ? id.hashCode() : 0;
      result = 31 * result + (type != null ? type.hashCode() : 0);
      return result;
   }
}
