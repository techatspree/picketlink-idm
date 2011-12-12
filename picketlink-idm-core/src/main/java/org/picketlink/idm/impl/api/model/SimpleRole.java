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

import org.picketlink.idm.api.Role;
import org.picketlink.idm.api.RoleType;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Group;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class SimpleRole implements Role, Serializable
{
   private final RoleType type;

   private final User user;

   private final Group group;

   public SimpleRole(RoleType type, User user, Group group)
   {
      this.type = type;
      this.user = user;
      this.group = group;
   }

   public User getUser()
   {
      return user;
   }

   public Group getGroup()
   {
      return group;
   }

   public RoleType getRoleType()
   {
      return type;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof SimpleRole))
      {
         return false;
      }

      SimpleRole that = (SimpleRole)o;

      if (group != null ? !group.equals(that.group) : that.group != null)
      {
         return false;
      }
      if (user != null ? !user.equals(that.user) : that.user != null)
      {
         return false;
      }
      if (type != null ? !type.equals(that.type) : that.type != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = type != null ? type.hashCode() : 0;
      result = 31 * result + (user != null ? user.hashCode() : 0);
      result = 31 * result + (group != null ? group.hashCode() : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return "SimpleRole{" +
         "type=" + type +
         ", identity=" + user +
         ", group=" + group +
         '}';
   }
}
