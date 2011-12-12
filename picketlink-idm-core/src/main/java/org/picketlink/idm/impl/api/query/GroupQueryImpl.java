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

package org.picketlink.idm.impl.api.query;

import org.picketlink.idm.api.query.GroupQuery;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.User;
import org.picketlink.idm.impl.api.model.GroupKey;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;

import java.util.Set;
import java.util.Collections;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class GroupQueryImpl extends AbstractQuery implements GroupQuery
{
   public final GroupKey groupKey;

   public final String groupName;

   public final String groupType;

   public final Set<Group> associatedParentGroups;

   public final Set<Group> associatedChildGroups;

   public final Set<User> usersAssociated;

   public final Set<User> usersRelated;

   public final Set<User> usersConnectedByRole;

   public GroupQueryImpl(IdentitySearchCriteriaImpl searchCriteria, GroupKey groupKey, String groupName, String groupType, Set<Group> associatedParentGroups, Set<Group> associatedChildGroups, Set<User> usersAssociated, Set<User> usersRelated, Set<User> usersConnectedByRole)
   {
      super(searchCriteria);
      this.groupKey = groupKey;
      this.groupName = groupName;
      this.groupType = groupType;

      if (associatedParentGroups != null)
      {
         this.associatedParentGroups = Collections.unmodifiableSet(associatedParentGroups);
      }
      else
      {
         this.associatedParentGroups = Collections.emptySet();
      }

      if (associatedChildGroups != null)
      {
         this.associatedChildGroups = Collections.unmodifiableSet(associatedChildGroups);
      }
      else
      {
         this.associatedChildGroups = Collections.emptySet();
      }

      if (usersAssociated != null)
      {
         this.usersAssociated = Collections.unmodifiableSet(usersAssociated);
      }
      else
      {
         this.usersAssociated = Collections.emptySet();
      }

      if (usersRelated != null)
      {
         this.usersRelated = Collections.unmodifiableSet(usersRelated);
      }
      else
      {
         this.usersRelated = Collections.emptySet();
      }

      if (usersConnectedByRole != null)
      {
         this.usersConnectedByRole = Collections.unmodifiableSet(usersConnectedByRole);
      }
      else
      {
         this.usersConnectedByRole = Collections.emptySet();
      }
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof GroupQueryImpl))
      {
         return false;
      }
      if (!super.equals(o))
      {
         return false;
      }

      GroupQueryImpl that = (GroupQueryImpl)o;

      if (!associatedChildGroups.equals(that.associatedChildGroups))
      {
         return false;
      }
      if (!associatedParentGroups.equals(that.associatedParentGroups))
      {
         return false;
      }
      if (groupKey != null ? !groupKey.equals(that.groupKey) : that.groupKey != null)
      {
         return false;
      }
      if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null)
      {
         return false;
      }
      if (groupType != null ? !groupType.equals(that.groupType) : that.groupType != null)
      {
         return false;
      }
      if (!usersAssociated.equals(that.usersAssociated))
      {
         return false;
      }
      if (!usersConnectedByRole.equals(that.usersConnectedByRole))
      {
         return false;
      }
      if (!usersRelated.equals(that.usersRelated))
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (groupKey != null ? groupKey.hashCode() : 0);
      result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
      result = 31 * result + (groupType != null ? groupType.hashCode() : 0);
      result = 31 * result + associatedParentGroups.hashCode();
      result = 31 * result + associatedChildGroups.hashCode();
      result = 31 * result + usersAssociated.hashCode();
      result = 31 * result + usersRelated.hashCode();
      result = 31 * result + usersConnectedByRole.hashCode();
      return result;
   }
}
