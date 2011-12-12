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

import org.picketlink.idm.api.query.UserQuery;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;

import java.util.Set;
import java.util.Collections;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class UserQueryImpl extends AbstractQuery implements UserQuery  
{

   public final String userId;

   public final Set<Group> groupsAssociatedWith;

   public final Set<Group> groupsConnectedWithRole;

   public final Set<Group> groupsRelated;

   public UserQueryImpl(IdentitySearchCriteriaImpl searchCriteria, String userId, Set<Group> groupsAssociatedWith, Set<Group> groupsConnectedWithRole, Set<Group> groupsRelated)
   {
      super(searchCriteria);
      this.userId = userId;

      if (groupsAssociatedWith != null)
      {
         this.groupsAssociatedWith = Collections.unmodifiableSet(groupsAssociatedWith);
      }
      else
      {
         this.groupsAssociatedWith = Collections.emptySet();
      }

      if (groupsConnectedWithRole != null)
      {
         this.groupsConnectedWithRole = Collections.unmodifiableSet(groupsConnectedWithRole);
      }
      else
      {
         this.groupsConnectedWithRole = Collections.emptySet();
      }

      if (groupsRelated != null)
      {
         this.groupsRelated = Collections.unmodifiableSet(groupsRelated);
      }
      else
      {
         this.groupsRelated = Collections.emptySet();
      }
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof UserQueryImpl))
      {
         return false;
      }
      if (!super.equals(o))
      {
         return false;
      }

      UserQueryImpl userQuery = (UserQueryImpl)o;

      if (!groupsAssociatedWith.equals(userQuery.groupsAssociatedWith))
      {
         return false;
      }
      if (!groupsConnectedWithRole.equals(userQuery.groupsConnectedWithRole))
      {
         return false;
      }
      if (!groupsRelated.equals(userQuery.groupsRelated))
      {
         return false;
      }
      if (userId != null ? !userId.equals(userQuery.userId) : userQuery.userId != null)
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
      result = 31 * result + groupsAssociatedWith.hashCode();
      result = 31 * result + groupsConnectedWithRole.hashCode();
      result = 31 * result + groupsRelated.hashCode();
      return result;
   }
}
