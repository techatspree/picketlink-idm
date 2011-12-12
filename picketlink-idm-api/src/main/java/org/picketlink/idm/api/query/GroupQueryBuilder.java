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

package org.picketlink.idm.api.query;

import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.SortOrder;

import java.util.Collection;

/**
 * Expose operations to set conditions for Group search and create GroupQuery
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface GroupQueryBuilder extends QueryBuilder
{
   /**
    * Create GroupQuery based on conditions set using other GroupQueryBuilder methods.
    * @return
    */
   GroupQuery createQuery();

   /**
    * Reset all query conditions
    * @return
    */
   GroupQueryBuilder reset();

   /**
    * Search for a Group with a given key
    *
    * @param key
    * @return
    */
   GroupQueryBuilder setKey(String key);

   /**
    * Search for  groups with a given name and type
    * @param name
    * @param type
    * @return
    */
   GroupQueryBuilder setNameAndType(String name, String type);

   /**
    * Search for groups with a given name
    *
    * @param name
    * @return
    */
   GroupQueryBuilder setName(String name);

   /**
    * Search for groups with a given type
    * @param type
    * @return
    */
   GroupQueryBuilder setType(String type);

   /**
    * Search for groups that are associated with a given group
    *
    * @param group
    * @param parent
    * @return
    */
   GroupQueryBuilder addAssociatedGroup(Group group, boolean parent);

   /**
    * Search for groups that are associated with a given group
    *
    * @param key
    * @param parent
    * @return
    */
   GroupQueryBuilder addAssociatedGroup(String key, boolean parent);

   /**
    * Search for groups that are associated with a given groups
    * @param groups
    * @param parent
    * @return
    */
   GroupQueryBuilder addAssociatedGroups(Collection<Group> groups, boolean parent);

   /**
    * Search for groups that are associated with a given groups
    *
    * @param keys
    * @param parent
    * @return
    */
   GroupQueryBuilder addAssociatedGroupsKeys(Collection<String> keys, boolean parent);

   /**
    * Search for groups that are associated with a given user
    *
    * @param user
    * @return
    */
   GroupQueryBuilder addAssociatedUser(User user);

   /**
    * Search for groups that are associated with a given user
    *
    * @param key
    * @return
    */
   GroupQueryBuilder addAssociatedUser(String key);

   /**
    * Search for groups that are associated with a given users
    *
    * @param users
    * @return
    */
   GroupQueryBuilder addAssociatedUsers(Collection<User> users);

   /**
    * Search for groups that are associated with a given users
    *
    * @param keys
    * @return
    */
   GroupQueryBuilder addAssociatedUsersKeys(Collection<String> keys);

   /**
    * Search for groups that are connected with a Role with a given user
    *
    * @param user
    * @return
    */
   GroupQueryBuilder addUserConnectedByRole(User user);

   /**
    * Search for groups that are connected with a Role with a given user
    *
    * @param key
    * @return
    */
   GroupQueryBuilder addUserConnectedByRole(String key);

   /**
    * Search for groups that are connected with a Role with a given users
    *
    * @param users
    * @return
    */
   GroupQueryBuilder addUsersConnectedByRole(Collection<User> users);

   /**
    * Search for groups that are connected with a Role with a given users
    *
    * @param ids
    * @return
    */
   GroupQueryBuilder addUsersIdsConnectedByRole(Collection<String> ids);

   /**
    * Search for groups that are associated or connected with a Role with a given user
    *
    * @param user
    * @return
    */
   GroupQueryBuilder addRelatedUser(User user);

   /**
    * Search for groups that are associated or connected with a Role with a given user
    *
    * @param id
    * @return
    */
   GroupQueryBuilder addRelatedUser(String id);

   /**
    * Sort results
    *
    * @param order
    * @return
    * @throws UnsupportedQueryCriterium
    */
   GroupQueryBuilder sort(SortOrder order) throws UnsupportedQueryCriterium;

   /**
    * Sort by attribute name
    *
    * @param name
    * @return
    * @throws UnsupportedQueryCriterium
    */
   GroupQueryBuilder sortAttributeName(String name) throws UnsupportedQueryCriterium;

   /**
    * Return specified page from results
    *
    * @param firstResult
    * @param maxResults
    * @return
    * @throws UnsupportedQueryCriterium
    */
   GroupQueryBuilder page(int firstResult, int maxResults) throws UnsupportedQueryCriterium;

   /**
    * Filter resuts by a given attribute values. All values need to be present 
    *
    * @param attributeName
    * @param attributeValue
    * @return
    * @throws UnsupportedQueryCriterium
    */
   GroupQueryBuilder attributeValuesFilter(String attributeName, String[] attributeValue) throws UnsupportedQueryCriterium;

}
