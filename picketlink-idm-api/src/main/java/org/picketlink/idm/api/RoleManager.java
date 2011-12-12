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

package org.picketlink.idm.api;

import org.picketlink.idm.common.exception.IdentityException;

import java.util.Collection;
import java.util.Map;

/**
 * Management operations on Role objects. Role support is optional and depends on the capabilities of the underlying
 * configured identity stores capabilities.
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface RoleManager
{

   /**
    * @return Session associated with this object instance
    */
   IdentitySession getIdentitySession();

   /**
    * @return
    */
   RoleManagerFeaturesDescription getSupportedFeatures();

   /**
    *
    * @return
    */
   IdentitySearchCriteria createIdentitySearchCriteria();

   // RoleType

   /**
    * Create RoleType
    * @param name
    * @return
    * @throws IdentityException
    */
   RoleType createRoleType(String name) throws IdentityException;

   /**
    * Remove RoleType
    * @param name
    * @throws IdentityException
    */
   void removeRoleType(String name) throws IdentityException;

   /**
    * Remove RoleType
    * @param roleType
    * @throws IdentityException
    */
   void removeRoleType(RoleType roleType) throws IdentityException;

   /**
    * Get RoleType
    * @param name
    * @return
    * @throws IdentityException
    */
   RoleType getRoleType(String name) throws IdentityException;

   /**
    * Find all RoleType objects stored
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findRoleTypes(IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all RoleType objects stored
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findRoleTypes() throws IdentityException;


   // Role
   /**
    * Create role
    * @param roleType
    * @param user
    * @param group
    * @return
    * @throws IdentityException
    */
   Role createRole(RoleType roleType, User user, Group group) throws IdentityException;

   /**
    * Create role
    * @param roleTypeName
    * @param userId
    * @param groupKey
    * @return
    * @throws IdentityException
    */
   Role createRole(String roleTypeName, String userId, String groupKey) throws IdentityException;

   /**
    * Create role
    * @param roleType
    * @param user
    * @param group
    * @return
    * @throws IdentityException
    */
   Role getRole(RoleType roleType, User user, Group group) throws IdentityException;

   /**
    * Create role
    * @param roleTypeName
    * @param userId
    * @param groupKey
    * @return
    * @throws IdentityException
    */
   Role getRole(String roleTypeName, String userId, String groupKey) throws IdentityException;

   /**
    * Remove Role
    * @param roleType
    * @param user
    * @param group
    * @throws IdentityException
    */
   void removeRole(RoleType roleType, User user, Group group) throws IdentityException;

   /**
    * Remove Role
    * @param roleTypeName
    * @param userId
    * @param groupKey
    * @throws IdentityException
    */
   void removeRole(String roleTypeName, String userId, String groupKey) throws IdentityException;

   /**
    * Remove Role
    * @param role
    * @throws IdentityException
    */
   void removeRole(Role role) throws IdentityException;

   /**
    * Check if Role is present
    * @param user
    * @param group
    * @param roleType
    * @return
    * @throws IdentityException
    */
   boolean hasRole(User user, Group group, RoleType roleType) throws IdentityException;

   /**
    * Check if Role is present
    * @param userId
    * @param groupKey
    * @param roleTypeName
    * @return
    * @throws IdentityException
    */
   boolean hasRole(String userId, String groupKey, String roleTypeName) throws IdentityException;

   /**
    * Find RoleType objects for roles associated with a given User and Group
    * @param user
    * @param group
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findRoleTypes(User user, Group group,
                                      IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find RoleType objects for roles associated with a given User and Group
    * @param userId
    * @param groupKey
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findRoleTypes(String userId, String groupKey,
                                      IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find RoleType objects for roles associated with a given User and Group
    * @param user
    * @param group
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findRoleTypes(User user, Group group) throws IdentityException;

   /**
    * Find RoleType objects for roles associated with a given User
    * @param user
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findUserRoleTypes(User user) throws IdentityException;

    /**
    * Find RoleType objects for roles associated with a given User
    * @param user
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findUserRoleTypes(User user, IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find RoleType objects for roles associated with a given User
    * @param userId
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findUserRoleTypes(String userId, IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find RoleType objects for roles associated with a given Group
    * @param group
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findGroupRoleTypes(Group group) throws IdentityException;

   /**
    * Find RoleType objects for roles associated with a given Group
    * @param group
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findGroupRoleTypes(Group group,
                                      IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find RoleType objects for roles associated with a given Group
    * @param groupKey
    * @return
    * @throws IdentityException
    */
   Collection<RoleType> findGroupRoleTypes(String groupKey,
                                      IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all Groups with which User has a Role association
    *
    * @param user
    * @param criteria
    * @return
    * @throws IdentityException
    */
   Collection<Group> findGroupsWithRelatedRole(User user,
                                               IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all Groups with which User has a Role association
    *
    * @param userId
    * @param criteria
    * @return
    * @throws IdentityException
    */
   Collection<Group> findGroupsWithRelatedRole(String userId,
                                               IdentitySearchCriteria criteria) throws IdentityException;


   /**
    * Find all Groups with which User has a Role association
    *
    * @param user
    * @param criteria
    * @return
    * @throws IdentityException
    */
   Collection<Group> findGroupsWithRelatedRole(User user,
                                               String groupType,
                                               IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all Groups with which User has a Role association
    *
    * @param userId
    * @param criteria
    * @return
    * @throws IdentityException
    */
   Collection<Group> findGroupsWithRelatedRole(String userId,
                                               String groupType,
                                               IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all Users with which Group has a Role association
    *
    * @param group
    * @param criteria
    * @return
    * @throws IdentityException
    */
   Collection<User> findUsersWithRelatedRole(Group group,
                                               IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all Users with which Group has a Role association
    *
    * @param groupKey
    * @param criteria
    * @return
    * @throws IdentityException
    */
   Collection<User> findUsersWithRelatedRole(String groupKey,
                                               IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find Role objects with a given RoleType associated with a given IdentityType
    * @param identityType
    * @param roleType
    * @return
    * @throws IdentityException
    */
   <T extends IdentityType> Collection<Role> findRoles(T identityType,
                                                       RoleType roleType) throws IdentityException;

   /**
    * Find Role objects with a given RoleType name associated with IdentityType for a given key
    * @param key
    * @param roleTypeName
    * @return
    * @throws IdentityException
    */
   <T extends IdentityType> Collection<Role> findRoles(String key,
                                                       String roleTypeName) throws IdentityException;

   /**
    *
    * @param role
    * @return
    */
   Map<String,String> getProperties(Role role) throws IdentityException;

   /**
    *
    * @param roleType
    * @return
    */
   Map<String,String> getProperties(RoleType roleType) throws IdentityException;

   /**
    *
    * @param roleTypeName
    * @return
    */
   Map<String,String> getProperties(String roleTypeName) throws IdentityException;

   /**
    *
    * @param role
    * @param name
    * @param value
    */
   void setProperty(Role role, String name, String value) throws IdentityException;

   /**
    *
    * @param roleType
    * @param name
    * @param value
    */
   void setProperty(RoleType roleType, String name, String value) throws IdentityException;

   /**
    *
    * @param roleTypeName
    * @param name
    * @param value
    */
   void setProperty(String roleTypeName, String name, String value) throws IdentityException;

   /**
    *
    * @param role
    * @param properties
    */
   void setProperties(Role role, Map<String, String> properties) throws IdentityException;

   /**
    *
    * @param roleType
    * @param properties
    */
   void setProperties(RoleType roleType, Map<String, String> properties) throws IdentityException;

   /**
    *
    * @param roleTypeName
    * @param properties
    */
   void setProperties(String roleTypeName, Map<String, String> properties) throws IdentityException;

   /**
    *
    * @param role
    * @param name
    */
   void removeProperty(Role role, String name) throws IdentityException;

   /**
    *
    * @param roleType
    * @param name
    */
   void removeProperty(RoleType roleType, String name) throws IdentityException;

   /**
    * 
    * @param roleTypeName
    * @param name
    */
   void removeProperty(String roleTypeName, String name) throws IdentityException;




}
