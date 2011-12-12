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

import org.picketlink.idm.api.query.RoleQueryBuilder;
import org.picketlink.idm.api.query.RoleQuery;
import org.picketlink.idm.api.query.UnsupportedQueryCriterium;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.RoleType;
import org.picketlink.idm.api.IdentityType;
import org.picketlink.idm.api.SortOrder;
import org.picketlink.idm.impl.api.model.SimpleUser;
import org.picketlink.idm.impl.api.model.SimpleGroup;
import org.picketlink.idm.impl.api.model.GroupKey;
import org.picketlink.idm.impl.api.model.SimpleRoleType;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class RoleQueryBuilderImpl extends AbstractQueryBuilder implements RoleQueryBuilder
{

   private User user;

   private Group group;

   private RoleType roleType;

   public RoleQuery createQuery()
   {
      return new RoleQueryImpl(
         searchCriteria,
         user,
         group,
         roleType
      );
   }

   public RoleQueryBuilder reset()
   {
      searchCriteria = new IdentitySearchCriteriaImpl();
      user = null;
      group = null;
      roleType = null;
      
      return this;
   }

   public RoleQueryBuilder setUser(User user)
   {
      checkNotNullArgument(user, "User");
      this.user = user;
      return this;
   }

   public RoleQueryBuilder setUser(String id)
   {
      checkNotNullArgument(id, "User id");
      this.user = new SimpleUser(id);
      return this;
   }

   public RoleQueryBuilder setGroup(Group group)
   {
      checkNotNullArgument(group, "Group");
      this.group = group;
      return this;
   }

   public RoleQueryBuilder setGroup(String id)
   {
      checkNotNullArgument(id, "Group id");
      this.group = new SimpleGroup(new GroupKey(id));
      return this;
   }

   public RoleQueryBuilder setRoleType(RoleType roleType)
   {
      checkNotNullArgument(roleType, "RoleType");
      this.roleType = roleType;
      return this;
   }

   public RoleQueryBuilder setRoleType(String roleTypeName)
   {
      checkNotNullArgument(roleTypeName, "RoleType name");
      this.roleType = new SimpleRoleType(roleTypeName);
      return this;
   }

   public RoleQueryBuilder setIdentityType(IdentityType identityType)
   {
      checkNotNullArgument(identityType, "IdentityType");
      if (identityType instanceof User)
      {
         this.user = (User)identityType;
      }
      else
      {
         this.group = (Group)identityType;
      }
      return this;
   }

   public RoleQueryBuilder setIdentityTypeKey(String id)
   {
      checkNotNullArgument(id, "IdentityType id");
      IdentityType identityType = createIdentityTypeFromId(id);
      return setIdentityType(identityType);
   }

   public RoleQueryBuilder sort(SortOrder order) throws UnsupportedQueryCriterium
   {
      return (RoleQueryBuilder)super.sort(order);
   }

   public RoleQueryBuilder sortAttributeName(String name) throws UnsupportedQueryCriterium
   {
      return (RoleQueryBuilder)super.sortAttributeName(name);
   }

   public RoleQueryBuilder page(int firstResult, int maxResults) throws UnsupportedQueryCriterium
   {
      return (RoleQueryBuilder)super.page(firstResult, maxResults);
   }

   public RoleQueryBuilder attributeValuesFilter(String attributeName, String[] attributeValue) throws UnsupportedQueryCriterium
   {
      return (RoleQueryBuilder)super.attributeValuesFilter(attributeName, attributeValue);
   }

}
