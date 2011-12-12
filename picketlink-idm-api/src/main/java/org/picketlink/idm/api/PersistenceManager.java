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

/**
 * Exposes all management operations on Group and User objects.
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface PersistenceManager
{

   /**
    * @return Session associated with this object instance
    */
   IdentitySession getIdentitySession();


   /**
    * @return object describing supported features
    */
   PersistenceManagerFeaturesDescription getFeaturesDescription();

   /**
    * Create IdentitySearchCriteria
    * @return
    */
   IdentitySearchCriteria createIdentitySearchCriteria();


   // Create

   /**
    * <p>Create User object</p>
    * @param id
    * @throws IdentityException
    * @return
    */
   User createUser(String id) throws IdentityException;

   /**
    * <p>Create a group of a particular type</p>
    * @param groupName
    * @param groupType
    * @return
    */
   Group createGroup(String groupName, String groupType)
   throws IdentityException;

   /**
    * <p>Create a group key. Result string can be used in other methods as a simple reference instead of whole object.
    * Group key contains encoded group type and name imformation.
    * In default implementation it can look as follows: "jbpid_group_id_._._GROUP_TYPE_._._GROUP_NAME".
    * Still prefix and format of key can change in the future so this method should be used to create it.</p>
    * @param groupName
    * @param groupType
    * @return
    */
   String createGroupKey(String groupName, String groupType);

   /**
    * <p>Create a user key. Result string can be used in other methods as a simple reference instead of whole object.
    * User key contains encoded id information. The most common implementation will be that key and id values are simply
    * equals. Still format of key can change in the future so this method should be used to create it.</p>
    * @param id
    * @return
    */
   String createUserKey(String id);

   // Remove

   /**
    * Remove given user
    *
    * @param user
    * @param force - if true all present relationships will be removed, if false any present relationship will cause
    * removal to fail 
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void removeUser(User user, boolean force) throws IdentityException;

   /**
    * Remove given user
    *
    * @param key
    * @param force - if true all present relationships will be removed, if false any present relationship will cause
    * removal to fail
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void removeUser(String key, boolean force) throws IdentityException;

   /**
    * Remove given group
    *
    * @param group
    * @param force - if true all present relationships will be removed, if false any present relationship will cause
    * removal to fail
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void removeGroup(Group group, boolean force) throws IdentityException;

   /**
    * Remove given group
    *
    * @param groupKey
    * @param force - if true all present relationships will be removed, if false any present relationship will cause
    * removal to fail
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void removeGroup(String groupKey, boolean force) throws IdentityException;

   // Search

   /**
    * @return a number of stored users
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   int getUserCount()
      throws IdentityException;

   /**
    * @param groupType
    * @return a number of stored groups with a given type
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   int getGroupTypeCount(String groupType)
      throws IdentityException;

   /**
    * Find user with a given key
    *
    * @param key
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   User findUser(String key) throws IdentityException;

   /**
    * Obtain users
    *
    * @param criteria
    * @return
    * @throws IdentityException
    */
   Collection<User> findUser(IdentitySearchCriteria criteria) throws IdentityException;

   // Search Groups

   /**
    * Find group with a given name and type
    *
    * @param name
    * @param groupType
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Group findGroup(String name, String groupType) throws IdentityException;

   /**
    * Find group with a given id
    *
    * @param key
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Group findGroupByKey(String key) throws IdentityException;


   /**
    * Find groups
    *
    * @param groupType
    * @param criteria
    * @return
    * @throws IdentityException
    */
   Collection<Group> findGroup(String groupType, IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find groups
    * 
    * @param groupType
    * @return
    * @throws IdentityException
    */
   Collection<Group> findGroup(String groupType) throws IdentityException;

}
