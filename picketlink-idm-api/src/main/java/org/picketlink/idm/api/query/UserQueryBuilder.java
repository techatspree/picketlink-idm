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
import org.picketlink.idm.api.SortOrder;

import java.util.Collection;

/**
 * Expose operations to set conditions for User search and create UserQuery
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface UserQueryBuilder extends QueryBuilder
{

   /**
    * Create UserQuery based on specified conditions
    * @return
    */
   UserQuery createQuery();

   /**
    * Reset all set conditions
    * @return
    */
   UserQueryBuilder reset();

   /**
    * Search for user with a given id
    * @param id
    * @return
    */
   UserQueryBuilder withUserId(String id);

   /**
    * Search for users associated with a given group
    *
    * @param group
    * @return
    */
   UserQueryBuilder addAssociatedGroup(Group group);

   /**
    * Search for users associated with a given group
    *
    * @param key
    * @return
    */
   UserQueryBuilder addAssociatedGroup(String key);

   /**
    * Search for users associated with a given groups
    *
    * @param groups
    * @return
    */
   UserQueryBuilder addAssociatedGroups(Collection<Group> groups);

   /**
    * Search for users associated with a given groups
    *
    * @param groupKeys
    * @return
    */
   UserQueryBuilder addAssociatedGroupsKeys(Collection<String> groupKeys);

   /**
    * Search for users that are connected with role with a given group
    *
    *
    * @param group
    * @return
    */
   UserQueryBuilder addGroupConnectedWithRole(Group group);

   /**
    * Search for users that are connected with role with a given group
    *
    * @param groupKey
    * @return
    */
   UserQueryBuilder addGroupConnectedWithRole(String groupKey);

   /**
    * Search for users that are connected with role with a given groups
    *
    * @param groups
    * @return
    */
   UserQueryBuilder addGroupsConnectedWithRole(Collection<Group> groups);

   /**
    * Search for users that are connected with role with a given group
    *
    * @param groupKeys
    * @return
    */
   UserQueryBuilder addGroupsKeysConnectedWithRole(Collection<String> groupKeys);

   /**
    * Search for users that are connected with role or associated with a given group
    *
    * @param group
    * @return
    */
   UserQueryBuilder addRelatedGroup(Group group);

   /**
    * Search for users that are connected with role or associated with a given group
    *
    * @param key
    * @return
    */
   UserQueryBuilder addRelatedGroup(String key);

   /**
    * Search for users that are connected with role or associated with a given group
    *
    * @param group
    * @return
    */
   UserQueryBuilder addRelatedGroups(Collection<Group> group);

   /**
    * Search for users that are connected with role or associated with a given group
    *
    * @param groupKeys
    * @return
    */
   UserQueryBuilder addRelatedGroupsKeys(Collection<String> groupKeys);

   /**
    * Sort results
    *
    * @param order
    * @return
    * @throws UnsupportedQueryCriterium
    */
   UserQueryBuilder sort(SortOrder order) throws UnsupportedQueryCriterium;

   /**
    * Sort results using given attribute name
    * @param name
    * @return
    * @throws UnsupportedQueryCriterium
    */
   UserQueryBuilder sortAttributeName(String name) throws UnsupportedQueryCriterium;

   /**
    * Return page from results
    *
    * @param firstResult
    * @param maxResults
    * @return
    * @throws UnsupportedQueryCriterium
    */
   UserQueryBuilder page(int firstResult, int maxResults) throws UnsupportedQueryCriterium;

   /**
    * Filter results using attribute values. All specified values must be present
    *
    * @param attributeName
    * @param attributeValue
    * @return
    * @throws UnsupportedQueryCriterium
    */
   UserQueryBuilder attributeValuesFilter(String attributeName, String[] attributeValue) throws UnsupportedQueryCriterium;

   /**
    * Filter results using id filter. Wildcard '*' can be used.
    * 
    * @param idFilter
    * @return
    * @throws UnsupportedQueryCriterium
    */
   UserQueryBuilder idFilter(String idFilter) throws UnsupportedQueryCriterium;

}
