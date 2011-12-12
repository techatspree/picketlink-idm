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

import org.picketlink.idm.api.query.RoleQuery;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.RoleType;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class RoleQueryImpl extends AbstractQuery implements RoleQuery
{

   public final User user;

   public final Group group;

   public final RoleType roleType;

   public RoleQueryImpl(IdentitySearchCriteriaImpl searchCriteria, User user, Group group, RoleType roleType)
   {
      super(searchCriteria);
      this.user = user;
      this.group = group;
      this.roleType = roleType;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof RoleQueryImpl))
      {
         return false;
      }
      if (!super.equals(o))
      {
         return false;
      }

      RoleQueryImpl roleQuery = (RoleQueryImpl)o;

      if (group != null ? !group.equals(roleQuery.group) : roleQuery.group != null)
      {
         return false;
      }
      if (roleType != null ? !roleType.equals(roleQuery.roleType) : roleQuery.roleType != null)
      {
         return false;
      }
      if (user != null ? !user.equals(roleQuery.user) : roleQuery.user != null)
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
      result = 31 * result + (roleType != null ? roleType.hashCode() : 0);
      return result;
   }
}
