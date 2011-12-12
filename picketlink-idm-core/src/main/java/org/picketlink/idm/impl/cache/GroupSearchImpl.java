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

import org.picketlink.idm.cache.GroupSearch;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class GroupSearchImpl extends AbstractSearch implements GroupSearch, Serializable
{
   private String groupId;

   private String groupType;

   private Collection<String> associatedUserIds = new HashSet<String>();

   private Collection<String> relatedUserIds = new HashSet<String>();

   private Collection<String> associatedGroupIds = new HashSet<String>();

   private Collection<String> relatedGroupIds = new HashSet<String>();

   private Boolean parent;

   private Boolean cascade;

   public String getGroupId()
   {
      return groupId;
   }

   public void setGroupId(String groupId)
   {
      this.groupId = groupId;
   }

   public String getGroupType()
   {
      return groupType;
   }

   public void setGroupType(String groupType)
   {
      this.groupType = groupType;
   }

   public Collection<String> getAssociatedUserIds()
   {
      return associatedUserIds;
   }

   public void setAssociatedUserIds(Collection<String> associatedUserIds)
   {
      this.associatedUserIds = associatedUserIds;
   }

   public void addAssociatedUserId(String id)
   {
      this.associatedUserIds.add(id);
   }

   public Collection<String> getRelatedUserIds()
   {
      return relatedUserIds;
   }

   public void setRelatedUserIds(Collection<String> relatedUserIds)
   {
      this.relatedUserIds = relatedUserIds;
   }

   public void addRelatedUserId(String id)
   {
      this.relatedUserIds.add(id);
   }

   public Collection<String> getAssociatedGroupIds()
   {
      return associatedGroupIds;
   }

   public void setAssociatedGroupIds(Collection<String> associatedGroupIds)
   {
      this.associatedGroupIds = associatedGroupIds;
   }

   public void addAssociatedGroupId(String id)
   {
      this.associatedGroupIds.add(id);
   }

   public Collection<String> getRelatedGroupIds()
   {
      return relatedGroupIds;
   }

   public void setRelatedGroupIds(Collection<String> relatedGroupIds)
   {
      this.relatedGroupIds = relatedGroupIds;
   }

   public void addRelatedGroupId(String id)
   {
      this.relatedGroupIds.add(id);
   }

   public Boolean isParent()
   {
      return parent;
   }

   public void setParent(Boolean parent)
   {
      this.parent = parent;
   }

   public Boolean isCascade()
   {
      return cascade;
   }

   public void setCascade(Boolean cascade)
   {
      this.cascade = cascade;
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

      GroupSearchImpl that = (GroupSearchImpl)o;

      if (associatedGroupIds != null ? !associatedGroupIds.equals(that.associatedGroupIds) : that.associatedGroupIds != null)
      {
         return false;
      }
      if (associatedUserIds != null ? !associatedUserIds.equals(that.associatedUserIds) : that.associatedUserIds != null)
      {
         return false;
      }
      if (cascade != null ? !cascade.equals(that.cascade) : that.cascade != null)
      {
         return false;
      }
      if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null)
      {
         return false;
      }
      if (groupType != null ? !groupType.equals(that.groupType) : that.groupType != null)
      {
         return false;
      }
      if (parent != null ? !parent.equals(that.parent) : that.parent != null)
      {
         return false;
      }
      if (relatedGroupIds != null ? !relatedGroupIds.equals(that.relatedGroupIds) : that.relatedGroupIds != null)
      {
         return false;
      }
      if (relatedUserIds != null ? !relatedUserIds.equals(that.relatedUserIds) : that.relatedUserIds != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
      result = 31 * result + (groupType != null ? groupType.hashCode() : 0);
      result = 31 * result + (associatedUserIds != null ? associatedUserIds.hashCode() : 0);
      result = 31 * result + (relatedUserIds != null ? relatedUserIds.hashCode() : 0);
      result = 31 * result + (associatedGroupIds != null ? associatedGroupIds.hashCode() : 0);
      result = 31 * result + (relatedGroupIds != null ? relatedGroupIds.hashCode() : 0);
      result = 31 * result + (parent != null ? parent.hashCode() : 0);
      result = 31 * result + (cascade != null ? cascade.hashCode() : 0);
      return result;
   }
}
