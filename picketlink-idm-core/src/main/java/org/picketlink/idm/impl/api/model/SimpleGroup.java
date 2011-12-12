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

package org.picketlink.idm.impl.api.model;

import java.io.Serializable;

import org.picketlink.idm.api.Group;


/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class SimpleGroup implements Group, Serializable
{
   private final String name;

   private final String id;

   private final String groupType;

   public SimpleGroup(String name, String groupType)
   {
      this.name = name;
      this.groupType = groupType;
      this.id = new GroupKey(name, groupType).getKey();

   }

   public SimpleGroup(GroupKey groupKey)
   {
      this.name = groupKey.getName();
      this.groupType = groupKey.getType();
      this.id = groupKey.getKey();
   }

   public String getName()
   {
      return name;
   }

   public String getGroupType()
   {
      return groupType;
   }

   public String getKey()
   {
      return id;
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

      SimpleGroup that = (SimpleGroup)o;

      if (groupType != null ? !groupType.equals(that.groupType) : that.groupType != null)
      {
         return false;
      }
      if (id != null ? !id.equals(that.id) : that.id != null)
      {
         return false;
      }
      if (name != null ? !name.equals(that.name) : that.name != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = name != null ? name.hashCode() : 0;
      result = 31 * result + (id != null ? id.hashCode() : 0);
      result = 31 * result + (groupType != null ? groupType.hashCode() : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return "SimpleGroup{" +
         "name='" + name + '\'' +
         ", id='" + id + '\'' +
         ", groupType='" + groupType + '\'' +
         '}';
   }
}
