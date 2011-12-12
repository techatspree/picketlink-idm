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

import org.picketlink.idm.cache.RelationshipSearch;
import org.picketlink.idm.api.IdentityType;
import org.picketlink.idm.impl.api.model.GroupKey;
import org.picketlink.idm.impl.api.model.SimpleGroup;
import org.picketlink.idm.impl.api.model.SimpleUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class RelationshipSearchImpl extends AbstractSearch implements RelationshipSearch, Serializable
{
   private Collection<IdentityType> parents = new HashSet<IdentityType>();

   private Collection<IdentityType> members = new HashSet<IdentityType>();

   public Collection<IdentityType> getParents()
   {
      return parents;
   }

   public Collection<IdentityType> getMembers()
   {
      return members;
   }

   public void setParents(Collection<IdentityType> parents)
   {
      this.parents = parents;
   }

   public void setMembers(Collection<IdentityType> members)
   {
      this.members = members;
   }

   public void addParent(IdentityType it)
   {
      parents.add(it);
   }

   public void addMember(IdentityType it)
   {
      members.add(it);
   }

   public void addParent(String id)
   {
      if (GroupKey.validateKey(id))
      {
         parents.add(new SimpleGroup(new GroupKey(id)));
         return;
      }
      parents.add(new SimpleUser(id));
   }

   public void addMember(String id)
   {
      if (GroupKey.validateKey(id))
      {
         members.add(new SimpleGroup(new GroupKey(id)));
         return;
      }
      members.add(new SimpleUser(id));
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

      RelationshipSearchImpl that = (RelationshipSearchImpl)o;

      if (members != null ? !members.equals(that.members) : that.members != null)
      {
         return false;
      }
      if (parents != null ? !parents.equals(that.parents) : that.parents != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (parents != null ? parents.hashCode() : 0);
      result = 31 * result + (members != null ? members.hashCode() : 0);
      return result;
   }
}
