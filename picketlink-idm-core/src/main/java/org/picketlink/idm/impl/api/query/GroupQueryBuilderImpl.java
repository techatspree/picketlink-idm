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

import org.picketlink.idm.api.query.GroupQueryBuilder;
import org.picketlink.idm.api.query.GroupQuery;
import org.picketlink.idm.api.query.UnsupportedQueryCriterium;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.SortOrder;
import org.picketlink.idm.impl.api.model.GroupKey;
import org.picketlink.idm.impl.api.model.SimpleGroup;
import org.picketlink.idm.impl.api.model.SimpleUser;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class GroupQueryBuilderImpl extends AbstractQueryBuilder implements GroupQueryBuilder
{
   private GroupKey groupKey;

   private String groupName;

   private String groupType;

   private Set<Group> associatedParentGroups = new HashSet<Group>();

   private Set<Group> associatedChildGroups = new HashSet<Group>();

   private Set<User> usersAssociated = new HashSet<User>();

   private Set<User> usersRelated = new HashSet<User>();

   private Set<User> usersConnectedByRole = new HashSet<User>();

   private void prepare()
   {
      if (groupKey == null && (groupName != null && groupType != null))
      {
         groupKey = new GroupKey(groupName, groupType);
      }

   }

   public GroupQuery createQuery()
   {
      prepare();

      return new GroupQueryImpl(
         searchCriteria,
         groupKey,
         groupName,
         groupType,
         associatedParentGroups,
         associatedChildGroups,
         usersAssociated,
         usersRelated,
         usersConnectedByRole);
   }

   public GroupQueryBuilder reset()
   {
      searchCriteria = new IdentitySearchCriteriaImpl();
      groupKey = null;
      groupName = null;
      groupType = null;
      associatedParentGroups = new HashSet<Group>();
      associatedChildGroups = new HashSet<Group>();
      usersAssociated = new HashSet<User>();
      usersRelated = new HashSet<User>();
      usersConnectedByRole = new HashSet<User>();

      return this;
   }

   public GroupQueryBuilder setKey(String id)
   {
      groupKey = new GroupKey(id);
      return this;
   }

   public GroupQueryBuilder setNameAndType(String name, String type)
   {
      groupKey = new GroupKey(name, type);
      groupName = name;
      groupType = type;

      return this;
   }

   public GroupQueryBuilder setName(String name)
   {
      checkNotNullArgument(name, "Group name");
      groupName = name;
      return this;
   }

   public GroupQueryBuilder setType(String type)
   {
      checkNotNullArgument(type, "Group type");
      groupType = type;
      return this;
   }

   public GroupQueryBuilder addAssociatedGroup(Group group, boolean parent)
   {
      checkNotNullArgument(group, "Group");
      if (parent)
      {
         associatedParentGroups.add(group);
      }
      else
      {
         associatedChildGroups.add(group);
      }
      return this;
   }

   public GroupQueryBuilder addAssociatedGroup(String id, boolean parent)
   {
      checkNotNullArgument(id, "Group id");

      Group group = new SimpleGroup(new GroupKey(id));

      if (parent)
      {
         associatedParentGroups.add(group);
      }
      else
      {
         associatedChildGroups.add(group);
      }


      return this;
   }

   public GroupQueryBuilder addAssociatedGroups(Collection<Group> groups, boolean parent)
   {
      checkNotNullArgument(groups, "Groups");

      if (parent)
      {
         associatedParentGroups.addAll(groups);
      }
      else
      {
         associatedChildGroups.addAll(groups);
      }

      return this;
   }

   public GroupQueryBuilder addAssociatedGroupsKeys(Collection<String> ids, boolean parent)
   {
      checkNotNullArgument(ids, "Groups ids");
      for (String groupId : ids)
      {
         Group group = new SimpleGroup(new GroupKey(groupId));

         if (parent)
         {
            associatedParentGroups.add(group);
         }
         else
         {
            associatedChildGroups.add(group);
         }
      }

      return this;
   }

   public GroupQueryBuilder addAssociatedUser(User user)
   {
      checkNotNullArgument(user, "User");
      usersAssociated.add(user);
      return this;
   }

   public GroupQueryBuilder addAssociatedUser(String id)
   {
      checkNotNullArgument(id, "User id");
      usersAssociated.add(new SimpleUser(id));
      return this;
   }

   public GroupQueryBuilder addAssociatedUsers(Collection<User> users)
   {
      checkNotNullArgument(users, "Users");
      usersAssociated.addAll(users);
      return this;
   }

   public GroupQueryBuilder addAssociatedUsersKeys(Collection<String> ids)
   {
      checkNotNullArgument(ids, "Users ids");
      for (String id : ids)
      {
         usersAssociated.add(new SimpleUser(id));
      }
      return this;
   }

   public GroupQueryBuilder addUserConnectedByRole(User user)
   {
      checkNotNullArgument(user, "User");
      usersConnectedByRole.add(user);
      return this;
   }

   public GroupQueryBuilder addUserConnectedByRole(String id)
   {
      checkNotNullArgument(id, "User id");
      usersConnectedByRole.add(new SimpleUser(id));
      return this;
   }

   public GroupQueryBuilder addUsersConnectedByRole(Collection<User> users)
   {
      checkNotNullArgument(users, "Users");
      usersConnectedByRole.addAll(users);
      return this;
   }

   public GroupQueryBuilder addUsersIdsConnectedByRole(Collection<String> ids)
   {
      checkNotNullArgument(ids, "Users ids");
      for (String id : ids)
      {
         usersConnectedByRole.add(new SimpleUser(id));
      }
      return this;
   }

   public GroupQueryBuilder addRelatedUser(User user)
   {
      checkNotNullArgument(user, "User");
      usersRelated.add(user);
      return this;
   }

   public GroupQueryBuilder addRelatedUser(String id)
   {
      checkNotNullArgument(id, "User id");
      usersRelated.add(new SimpleUser(id));
      return this;
   }

   public GroupQueryBuilder sort(SortOrder order) throws UnsupportedQueryCriterium
   {
      return (GroupQueryBuilder)super.sort(order);
   }

   public GroupQueryBuilder sortAttributeName(String name) throws UnsupportedQueryCriterium
   {
      return (GroupQueryBuilder)super.sortAttributeName(name);
   }

   public GroupQueryBuilder page(int firstResult, int maxResults) throws UnsupportedQueryCriterium
   {
      return (GroupQueryBuilder)super.page(firstResult, maxResults);
   }

   public GroupQueryBuilder attributeValuesFilter(String attributeName, String[] attributeValue) throws UnsupportedQueryCriterium
   {
      return (GroupQueryBuilder)super.attributeValuesFilter(attributeName, attributeValue);
   }

}
