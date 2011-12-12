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

import org.picketlink.idm.cache.RoleTypeSearch;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.User;

public class RoleTypeSearchImpl extends AbstractSearch implements RoleTypeSearch, Serializable
{
   private User user;

   private Group group;

   public User getUser()
   {
      return user;
   }

   public void setUser(User user)
   {
      this.user = user;
   }

   public Group getGroup()
   {
      return group;
   }

   public void setGroup(Group group)
   {
      this.group = group;
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

      RoleTypeSearchImpl that = (RoleTypeSearchImpl)o;

      if (group != null ? !group.equals(that.group) : that.group != null)
      {
         return false;
      }
      if (user != null ? !user.equals(that.user) : that.user != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (user != null ? user.hashCode() : 0);
      result = 31 * result + (group != null ? group.hashCode() : 0);
      return result;
   }
}
