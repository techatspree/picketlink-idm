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
package org.picketlink.idm.cache;

import org.picketlink.idm.api.*;
import org.picketlink.idm.api.cfg.IdentityConfigurationRegistry;
import org.picketlink.idm.api.query.GroupQuery;
import org.picketlink.idm.api.query.RoleQuery;
import org.picketlink.idm.api.query.UserQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Cache provider for Identity API. Namespaces enable flexible use (per realm or per session)
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface APICacheProvider
{

   
   /**
    * Initialize provider.
    * @param properties
    */
   void initialize(Map<String, String> properties, IdentityConfigurationRegistry configurationRegistry);

   /**
    * Invalidate whole namespace
    *
    * @param ns
    */
   void invalidate(String ns);

   /**
    * Invalidate whole cache including all namespaces;
    */
   void invalidateAll();

   /**
    * Generate realm namespace.
    *
    * @param realmId - if null will generate root namespace
    * @return
    */
   String getNamespace(String realmId);

   /**
    * Generate namespace based on realm and session ids.
    *
    * @param realmId if null will generate root namespace
    * @param sessionId if null will generate realm namespace
    * @return
    */
   String getNamespace(String realmId, String sessionId);


   // Persistence Manager


   /**
    * Store user in cache
    *
    * @param ns
    * @param user
    */
   void putUser(String ns, User user);

   /**
    * Get user from cache
    *
    * @param ns
    * @param id
    * @return
    */
   User getUser(String ns, String id);

   /**
    * Remove user from cache
    *
    * @param ns
    * @param id
    */
   void removeUser(String ns, String id);

   /**
    * Store users in cache
    *
    * @param ns
    * @param criteria
    * @param users
    */
   void putUsers(String ns, IdentitySearchCriteria criteria, Collection<User> users);

   /**
    * Get users from cache
    *
    * @param ns
    * @param criteria
    * @return
    */
   Collection<User> getUsers(String ns, IdentitySearchCriteria criteria);

   /**
    * Invalidate all stored users
    *
    * @param ns
    */
   void invalidateUsers(String ns);

   /**
    * Store user count in cache
    * @param ns
    * @param count
    */
   void putUserCount(String ns, int count);

   /**
    * Get user count
    * @param ns
    * @return
    */
   int getUserCount(String ns);

   /**
    * Invalidate user count
    * @param ns
    */
   void invalidateUserCount(String ns);

   //

   /**
    * Put group in cache
    *
    * @param ns
    * @param group
    */
   void putGroup(String ns, Group group);

   /**
    * Get group from cache
    *
    * @param ns
    * @param groupType
    * @param groupName
    * @return
    */
   Group getGroup(String ns, String groupType, String groupName);

   /**
    * Remove group from cache
    *
    * @param ns
    * @param groupType
    * @param groupName
    */
   void removeGroup(String ns, String groupType, String groupName);

   /**
    * Put groups in cache
    *
    * @param ns
    * @param criteria
    * @param groups
    */
   void putGroups(String ns, IdentitySearchCriteria criteria, Collection<Group> groups);

   /**
    * Get groups from cache
    *
    * @param ns
    * @param criteria
    * @return
    */
   Collection<Group> getGroups(String ns, IdentitySearchCriteria criteria);

   /**
    * Invalidate all stored groups
    *
    * @param ns
    */
   void invalidateGroups(String ns);

   /**
    * Store group count
    *
    * @param ns
    * @param groupType
    * @param count
    */
   void putGroupCount(String ns, String groupType, int count);

   /**
    * Get group count
    *
    * @param ns
    * @param groupType
    * @return
    */
   int getGroupCount(String ns, String groupType);

   /**
    * Invalidate group count
    *
    * @param ns
    * @param groupType
    */
   void invalidateGroupCount(String ns, String groupType);

   //

   /**
    * Store role
    *
    * @param ns
    * @param role
    */
   void putRole(String ns, Role role);

   /**
    * Get role
    *
    * @param ns
    * @param role
    * @return
    */
   Role getRole(String ns, Role role);

   /**
    * Remove role
    *
    * @param ns
    * @param role
    */
   void removeRole(String ns, Role role);

   /**
    * Store role type
    *
    * @param ns
    * @param roleType
    */
   void putRoleType(String ns, RoleType roleType);

   /**
    * Get role type
    *
    * @param ns
    * @param roleType
    * @return
    */
   RoleType getRoleType(String ns, RoleType roleType);

   /**
    * Remove role type
    *
    * @param ns
    * @param roleType
    */
   void removeRoleType(String ns, RoleType roleType);


   // Attribute

   /**
    * Store attributes
    *
    * @param ns
    * @param id
    * @param attributes
    */
   void putAttributes(String ns, String id, Map<String, Attribute> attributes);

   /**
    * Get attributes
    *
    * @param ns
    * @param id
    * @return
    */
   Map<String, Attribute> getAttributes(String ns, String id);

   /**
    * Invalidate attributes
    *
    * @param ns
    * @param id
    */
   void invalidateAttributes(String ns, String id);

   /**
    * Invalidate attributes
    *
    * @param ns
    */
   void invalidateAttributes(String ns);

   // Properties

   /**
    * Store properties
    *
    * @param ns
    * @param role
    * @param properties
    */
   void putProperties(String ns, Role role, Map<String, String> properties);

   /**
    * Get properties
    *
    * @param ns
    * @param role
    * @return
    */
   Map<String, String> getProperties(String ns, Role role);

   /**
    * Invalidate role properties
    *
    * @param ns
    * @param role
    */
   void invalidateRoleProperties(String ns, Role role);

   /**
    * Invalidate role properties
    *
    * @param ns
    */
   void invalidateRoleProperties(String ns);

   /**
    * Store properties
    *
    * @param ns
    * @param roleType
    * @param properties
    */
   void putProperties(String ns, RoleType roleType, Map<String, String> properties);

   /**
    * Get role type properties
    *
    * @param ns
    * @param roleType
    * @return
    */
   Map<String, String> getProperties(String ns, RoleType roleType);

   /**
    * Invalidate role type properties
    *
    * @param ns
    * @param roleType
    */
   void invalidateRoleTypeProperties(String ns, RoleType roleType);

   /**
    * Invalidate role type properties
    *
    * @param ns
    */
   void invalidateRoleTypeProperties(String ns);


   // Searches

   /**
    * Invalidate all searches stored in cache
    *
    * @param ns
    */
   void invalidateAllSearches(String ns);

   /**
    * Store user search
    *
    * @param ns
    * @param search
    * @param results
    */
   void putUserSearch(String ns, UserSearch search, Collection<User> results);

   /**
    * Get user search
    *
    * @param ns
    * @param search
    * @return
    */
   Collection<User> getUserSearch(String ns, UserSearch search);

   /**
    * Store group search
    *
    * @param ns
    * @param search
    * @param results
    */
   void putGroupSearch(String ns, GroupSearch search, Collection<Group> results);

   /**
    * Get group search
    *
    * @param ns
    * @param search
    * @return
    */
   Collection<Group> getGroupSearch(String ns, GroupSearch search);

   /**
    * Store relationship search
    *
    * @param ns
    * @param search
    * @param result
    */
   void putRelationshipSearch(String ns, RelationshipSearch search, Boolean result);

   /**
    * Get relationship Search
    *
    * @param ns
    * @param search
    * @return
    */
   Boolean getRelationshipSearch(String ns, RelationshipSearch search);

   /**
    * Store role search
    * @param ns
    * @param search
    * @param results
    */
   void putRoleSearch(String ns, RoleSearch search, Collection<Role> results);

   /**
    * Get role search
    * 
    * @param ns
    * @param search
    * @return
    */
   Collection<Role> getRoleSearch(String ns, RoleSearch search);

   /**
    * Store role type search
    *
    * @param ns
    * @param search
    * @param results
    */
   void putRoleTypeSearch(String ns, RoleTypeSearch search, Collection<RoleType> results);

   /**
    * Get role type search
    *
    * @param ns
    * @param search
    * @return
    */
   Collection<RoleType> getRoleTypeSearch(String ns, RoleTypeSearch search);


   // Queries

   /**
    * Invalidate all queries
    *
    * @param ns
    */
   void invalidateAllQueries(String ns);

   /**
    * Store user query
    *
    * @param ns
    * @param q
    * @param results
    */
   void putUserQuery(String ns, UserQuery q, Collection<User> results);

   /**
    * Store user query unique result
    *
    * @param ns
    * @param q
    * @param user
    */
   void putUserQueryUnique(String ns, UserQuery q, User user);

   /**
    * Get user query
    *
    * @param ns
    * @param q
    * @return
    */
   Collection<User> getUserQuery(String ns, UserQuery q);

   /**
    * Get user query unique result
    * @param ns
    * @param q
    * @return
    */
   User getUserQueryUnique(String ns, UserQuery q);

   /**
    * Invalidate user queries
    * @param ns
    */
   void invalidateUserQueries(String ns);

   //

   /**
    * Store group query
    *
    * @param ns
    * @param q
    * @param results
    */
   void putGroupQuery(String ns, GroupQuery q, Collection<Group> results);

   /**
    * Store group query unique result
    * \
    * @param ns
    * @param q
    * @param group
    */
   void putGroupQueryUnique(String ns, GroupQuery q, Group group);

   /**
    * Get group query
    *
    * @param ns
    * @param q
    * @return
    */
   Collection<Group> getGroupQuery(String ns, GroupQuery q);

   /**
    * Get group query unique
    *
    * @param ns
    * @param q
    * @return
    */
   Group getGroupQueryUnique(String ns, GroupQuery q);

   /**
    * Invalidate all group queries
    * @param ns
    */
   void invalidateGroupQueries(String ns);

   //

   /**
    * Store role query
    *
    * @param ns
    * @param q
    * @param results
    */
   void putRoleQuery(String ns, RoleQuery q, Collection<Role> results);

   /**
    * Store role query unique result
    *
    * @param ns
    * @param q
    * @param role
    */
   void putRoleQueryUnique(String ns, RoleQuery q, Role role);

   /**
    * Get role query
    *
    * @param ns
    * @param q
    * @return
    */
   Collection<Role> getRoleQuery(String ns, RoleQuery q);

   /**
    * Get role query
    *
    * @param ns
    * @param q
    * @return
    */
   Role getRoleQueryUnique(String ns, RoleQuery q);

   /**
    * Invalidate all role queries
    * 
    * @param ns
    */
   void invalidateRoleQueries(String ns);

}
