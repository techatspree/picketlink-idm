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

import org.picketlink.idm.cache.UserSearch;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class UserSearchImpl extends AbstractSearch implements UserSearch, Serializable
{

   private String userId;

   private Collection<String> associatedUserIds = new HashSet<String>();

   private Collection<String> relatedUserIds = new HashSet<String>();

   private Collection<String> associatedGroupTypeNames = new HashSet<String>();

   private Collection<String> relatedGroupTypeNames = new HashSet<String>();

   private Boolean cascade;

   public String getUserId()
   {
      return userId;
   }

   public void setUserId(String userId)
   {
      this.userId = userId;
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

   public Collection<String> getAssociatedGroupTypeNames()
   {
      return associatedGroupTypeNames;
   }

   public void setAssociatedGroupTypeNames(Collection<String> associatedGroupTypeNames)
   {
      this.associatedGroupTypeNames = associatedGroupTypeNames;
   }

   public void addAssociatedGroupId(String id)
   {
      this.associatedGroupTypeNames.add(id);
   }

   public Collection<String> getRelatedGroupTypeNames()
   {
      return relatedGroupTypeNames;
   }

   public void setRelatedGroupTypeNames(Collection<String> relatedGroupTypeNames)
   {
      this.relatedGroupTypeNames = relatedGroupTypeNames;
   }

   public void addRelatedGroupId(String id)
   {
      this.relatedGroupTypeNames.add(id);
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

      UserSearchImpl that = (UserSearchImpl)o;

      if (associatedGroupTypeNames != null ? !associatedGroupTypeNames.equals(that.associatedGroupTypeNames) : that.associatedGroupTypeNames != null)
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
      if (relatedGroupTypeNames != null ? !relatedGroupTypeNames.equals(that.relatedGroupTypeNames) : that.relatedGroupTypeNames != null)
      {
         return false;
      }
      if (relatedUserIds != null ? !relatedUserIds.equals(that.relatedUserIds) : that.relatedUserIds != null)
      {
         return false;
      }
      if (userId != null ? !userId.equals(that.userId) : that.userId != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (userId != null ? userId.hashCode() : 0);
      result = 31 * result + (associatedUserIds != null ? associatedUserIds.hashCode() : 0);
      result = 31 * result + (relatedUserIds != null ? relatedUserIds.hashCode() : 0);
      result = 31 * result + (associatedGroupTypeNames != null ? associatedGroupTypeNames.hashCode() : 0);
      result = 31 * result + (relatedGroupTypeNames != null ? relatedGroupTypeNames.hashCode() : 0);
      result = 31 * result + (cascade != null ? cascade.hashCode() : 0);
      return result;
   }
}
