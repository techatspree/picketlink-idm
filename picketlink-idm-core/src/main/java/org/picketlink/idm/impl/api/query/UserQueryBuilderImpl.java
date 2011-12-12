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

import org.picketlink.idm.api.query.UserQueryBuilder;
import org.picketlink.idm.api.query.UserQuery;
import org.picketlink.idm.api.query.UnsupportedQueryCriterium;
import org.picketlink.idm.api.query.QueryBuilder;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.SortOrder;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class UserQueryBuilderImpl extends AbstractQueryBuilder implements UserQueryBuilder
{
   private String userId;

   private Set<Group> groupsAssociatedWith = new HashSet<Group>();

   private Set<Group> groupsConnectedWithRole = new HashSet<Group>();

   private Set<Group> groupsRelated = new HashSet<Group>();

   public UserQuery createQuery()
   {
      return new UserQueryImpl(
         searchCriteria,
         userId,
         groupsAssociatedWith,
         groupsConnectedWithRole,
         groupsRelated
      );
   }

   public UserQueryBuilder reset()
   {
      searchCriteria = new IdentitySearchCriteriaImpl();
      userId = null;
      groupsAssociatedWith = new HashSet<Group>();
      groupsConnectedWithRole = new HashSet<Group>();
      groupsRelated = new HashSet<Group>();

      return this;
   }

   public UserQueryBuilder withUserId(String id)
   {
      checkNotNullArgument(id, "User id");
      userId = id;
      return this;
   }

   public UserQueryBuilder addAssociatedGroup(Group group)
   {
      checkNotNullArgument(group, "Group");
      groupsAssociatedWith.add(group);
      return this;
   }

   public UserQueryBuilder addAssociatedGroup(String id)
   {
      checkNotNullArgument(id, "Group id");
      groupsAssociatedWith.add(createGroupFromId(id));
      return this;
   }

   public UserQueryBuilder addAssociatedGroups(Collection<Group> groups)
   {
      checkNotNullArgument(groups, "Groups");
      groupsAssociatedWith.addAll(groups);
      return this;
   }

   public UserQueryBuilder addAssociatedGroupsKeys(Collection<String> groups)
   {
      checkNotNullArgument(groups, "Groups");
      for (String groupId : groups)
      {
         groupsAssociatedWith.add(createGroupFromId(groupId));
      }
      return this;
   }

   public UserQueryBuilder addGroupConnectedWithRole(Group group)
   {
      checkNotNullArgument(group, "Group");
      groupsConnectedWithRole.add(group);
      return this;
   }

   public UserQueryBuilder addGroupConnectedWithRole(String id)
   {
      checkNotNullArgument(id, "Group id");
      groupsConnectedWithRole.add(createGroupFromId(id));
      return this;
   }

   public UserQueryBuilder addGroupsConnectedWithRole(Collection<Group> groups)
   {
      checkNotNullArgument(groups, "Groups");
      groupsConnectedWithRole.addAll(groups);
      return this;
   }

   public UserQueryBuilder addGroupsKeysConnectedWithRole(Collection<String> groups)
   {
      checkNotNullArgument(groups, "Groups");
      for (String groupId : groups)
      {
         groupsConnectedWithRole.add(createGroupFromId(groupId));
      }
      return this;
   }

   public UserQueryBuilder addRelatedGroup(Group group)
   {
      checkNotNullArgument(group, "Group");
      groupsRelated.add(group);
      return this;
   }

   public UserQueryBuilder addRelatedGroup(String id)
   {
      checkNotNullArgument(id, "Group id");
      groupsRelated.add(createGroupFromId(id));
      return this;
   }

   public UserQueryBuilder addRelatedGroups(Collection<Group> group)
   {
      checkNotNullArgument(group, "Group");
      groupsRelated.addAll(group);
      return this;
   }

   public UserQueryBuilder addRelatedGroupsKeys(Collection<String> ids)
   {
      checkNotNullArgument(ids, "Groups ids");
      for (String id : ids)
      {
         groupsRelated.add(createGroupFromId(id));
      }
      return this;
   }

   public UserQueryBuilder sort(SortOrder order) throws UnsupportedQueryCriterium
   {
      return (UserQueryBuilder)super.sort(order);
   }

   public UserQueryBuilder sortAttributeName(String name) throws UnsupportedQueryCriterium
   {
      return (UserQueryBuilder)super.sortAttributeName(name);
   }

   public UserQueryBuilder page(int firstResult, int maxResults) throws UnsupportedQueryCriterium
   {
      return (UserQueryBuilder)super.page(firstResult, maxResults);
   }

   public UserQueryBuilder attributeValuesFilter(String attributeName, String[] attributeValue) throws UnsupportedQueryCriterium
   {
      return (UserQueryBuilder)super.attributeValuesFilter(attributeName, attributeValue);
   }

   public UserQueryBuilder idFilter(String idFilter) throws UnsupportedQueryCriterium
   {
      return (UserQueryBuilder)super.idFilter(idFilter);
   }
}
