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
 * Management of relationships between Identity and Group objects.
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface RelationshipManager
{

   /**
    * @return Session associated with this object instance
    */
   IdentitySession getIdentitySession();

   /**
    * @return
    */
   RelationshipManagerFeaturesDescription getFeaturesDescription();

   /**
    *
    * @return
    */
   IdentitySearchCriteria createIdentitySearchCriteria();

   // Assignation

   /**
    * <p>Associate groups</p>
    * @param parents
    * @param members
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateGroups(Collection<Group> parents, Collection<Group> members)
   throws IdentityException;

   /**
    * <p>Associate groups</p>
    * @param parent
    * @param members
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateGroups(Group parent, Collection<Group> members)
   throws IdentityException;

   /**
    * <p>Associate groups</p>
    * @param parentKeys
    * @param memberKeys
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateGroupsByKeys(Collection<String> parentKeys, Collection<String> memberKeys)
   throws IdentityException;

   /**
    * <p>Associate groups</p>
    * @param parentKeys
    * @param memberKeys
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateGroupsByKeys(String parentKeys, Collection<String> memberKeys)
   throws IdentityException;

   /**
    * <p>Associate groups</p>
    * @param parent
    * @param member
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateGroups(Group parent, Group member)
   throws IdentityException;

   /**
    * <p>Associate groups</p>
    * @param parentKey
    * @param memberKey
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateGroupsByKeys(String parentKey, String memberKey)
   throws IdentityException;

   /**
    * <p>Associate identities to groups</p>
    * @param parents
    * @param members
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateUsers(Collection<Group> parents, Collection<User> members)
   throws IdentityException;

   /**
    * <p>Associate identities to group</p>
    * @param parent
    * @param members
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateUser(Group parent, Collection<User> members)
   throws IdentityException;

   /**
    * <p>Associate identities to groups</p>
    * @param parentKeys
    * @param memberKeys
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateUsersByKeys(Collection<String> parentKeys, Collection<String> memberKeys)
   throws IdentityException;

   /**
    * <p>Associate identities to groups</p>
    * @param parentKey
    * @param memberKeys
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateUsersByKeys(String parentKey, Collection<String> memberKeys)
   throws IdentityException;

   /**
    * <p>Associate identities to groups</p>
    * @param parents
    * @param members
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateUser(Group parents, User members)
   throws IdentityException;

   /**
    * <p>Associate identities to groups</p>
    * @param parentGroupKey
    * @param memberKey
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void associateUserByKeys(String parentGroupKey, String memberKey)
   throws IdentityException;

   /**
    * Disassociate all groups from a given user
    *
    * @param user
    * @throws IdentityException
    */
   void disassociateGroups(User user) throws IdentityException;

   /**
    * Disassociate all groups from a given user
    * 
    * @param userId
    * @throws IdentityException
    */
   void disassociateGroups(String userId) throws IdentityException;


   /**
    * <p>Disassociate groups</p>
    * @param parents
    * @param members
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void disassociateGroups(Collection<Group> parents, Collection<Group> members)
   throws IdentityException;

   /**
    * <p>Disassociate groups</p>
    * @param parent
    * @param members
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void disassociateGroups(Group parent, Collection<Group> members)
   throws IdentityException;

   /**
    * <p>Disassociate groups</p>
    * @param parentKeys
    * @param memberKeys
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void disassociateGroupsByKeys(Collection<String> parentKeys, Collection<String> memberKeys)
   throws IdentityException;

   /**
    * <p>Disassociate groups</p>
    * @param parentKey
    * @param memberKeys
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void disassociateGroupsByKeys(String parentKey, Collection<String> memberKeys)
   throws IdentityException;

   /**
    * <p>Disassociate users from groups</p>
    * @param parents
    * @param members
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void disassociateUsers(Collection<Group> parents, Collection<User> members)
   throws IdentityException;

   /**
    * <p>Disassociate users from group</p>
    * @param parent
    * @param members
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void disassociateUsers(Group parent, Collection<User> members)
   throws IdentityException;

   /**
    * <p>Disassociate users from groups</p>
    * @param parentKeys
    * @param memberKeys
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void disassociateUsersByKeys(Collection<String> parentKeys, Collection<String> memberKeys)
   throws IdentityException;

   /**
    * <p>Disassociate users from groups</p>
    * @param parentKey
    * @param memberKey
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void disassociateUsersByKeys(String parentKey, Collection<String> memberKey)
   throws IdentityException;

   /**
    * <p>Check if association is present </p>
    * @param parents
    * @param members
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   <G extends IdentityType, I extends IdentityType> boolean isAssociated(Collection<G> parents, Collection<I> members)
   throws IdentityException;

   /**
    * <p>Check if association is present </p>
    * @param parentKeys
    * @param memberKeys
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   boolean isAssociatedByKeys(Collection<String> parentKeys, Collection<String> memberKeys)
   throws IdentityException;

   /**
    * <p>Check if association is present </p>
    * @param parent
    * @param member
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   <G extends IdentityType, I extends IdentityType> boolean isAssociated(G parent, I member)
   throws IdentityException;

   /**
    * <p>Check if association is present </p>
    * @param parentKey
    * @param memberKey
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   boolean isAssociatedByKeys(String parentKey, String memberKey)
   throws IdentityException;

   // Resolve relationships

   /**
    * Find groups that are associated with given group.
    * If 'parent' parameter is set to false, all parent group will be returned. If parent parameter is
    * set to true and 'cascade' is set to true all nested subgroubs will be returned.
    *
    * @param group parent group
    * @param groupType can be null
    * @param parent defines if given identity is parent or child side in the
    * relationship - default is true (parent)
    * @param cascade if true also identities from subgroubs will be retreived. Matters only when parent is set to true.
    * Default is false
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findAssociatedGroups(Group group,
                                        String groupType,
                                        boolean parent,
                                        boolean cascade) throws IdentityException;

   /**
    * Find groups that are associated with given group.
    * If 'parent' parameter is set to false, all parent group will be returned. If parent parameter is
    * set to true and 'cascade' is set to true all nested subgroubs will be returned.
    *
    * @param group parent group
    * @param groupType can be null
    * @param parent defines if given identity is parent or child side in the
    * relationship - default is true (parent)
    * @param cascade if true also identities from subgroubs will be retreived. Matters only when parent is set to true.
    * Default is false
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findAssociatedGroups(Group group,
                                        String groupType,
                                        boolean parent,
                                        boolean cascade,
                                        IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find groups that are associated with given group.
    * If 'parent' parameter is set to false, all parent group will be returned. If parent parameter is
    * set to true and 'cascade' is set to true all nested subgroubs will be returned.
    *
    * @param groupId Id of parent group
    * @param groupType can be null
    * @param parent defines if given identity is parent or child side in the
    * relationship - default is true (parent)
    * @param cascade if true also identities from subgroubs will be retreived. Matters only when parent is set to true.
    * Default is false
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findAssociatedGroups(String groupId,
                                        String groupType,
                                        boolean parent,
                                        boolean cascade,
                                        IdentitySearchCriteria criteria) throws IdentityException;

    /**
    * Find all groups that given users is associated with.
    * @param user child identity
    * @param groupType can be null
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findAssociatedGroups(User user,
                                          String groupType) throws IdentityException;

   /**
    * Find all groups that given user is associated with.
    *
    * @param user child identity
    * @param groupType can be null
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findAssociatedGroups(User user,
                                          String groupType,
                                          IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all groups that given user is associated with.
    *
    * @param userId - id of associated user
    * @param groupType can be null
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findAssociatedGroups(String userId,
                                          String groupType,
                                          IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all groups that given user is associated with.
    *
    * @param user child identity
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findAssociatedGroups(User user,
                                          IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all groups that given user is associated with.
    *
    * @param userId
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findAssociatedGroups(String userId,
                                          IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all groups that given user is associated with.
    *
    * @param user child identity
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findAssociatedGroups(User user) throws IdentityException;

   /**
    * Find users that have relationship with given parent group.
    *
    * @param group parent group
    * @param cascade if true also identities from subgroubs will be retrieved. Default is false
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<User> findAssociatedUsers(Group group,
                                           boolean cascade) throws IdentityException;

   /**
    * Find users that have relationship with given parent group.
    *
    * @param group parent group
    * @param cascade if true also identities from subgroubs will be retrieved. Default is false
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<User> findAssociatedUsers(Group group,
                                        boolean cascade,
                                        IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find users that have relationship with given parent group.
    *
    * @param groupKey parent group id
    * @param cascade if true also identities from subgroubs will be retrieved. Default is false
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<User> findAssociatedUsers(String groupKey,
                                        boolean cascade,
                                        IdentitySearchCriteria criteria) throws IdentityException;


   /**
    * Find all groups that given identity is associated with. Will return groups connected with a given user with a role
    *
    * @param user child identity
    * @param groupType can be null
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findRelatedGroups(User user,
                                          String groupType,
                                          IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find all groups that given identity is associated with. Will return groups connected with a given user with a role
    *
    * @param userId
    * @param groupType can be null
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<Group> findRelatedGroups(String userId,
                                          String groupType,
                                          IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find users that have relationship with given parent group. Will return users connected with a given group with a role
    *
    * @param group parent group
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<User> findRelatedUsers(Group group, IdentitySearchCriteria criteria) throws IdentityException;

   /**
    * Find users that have relationship with given parent group. Will return users connected with a given group with a role
    * 
    * @param groupKey parent group id
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Collection<User> findRelatedUsers(String groupKey, IdentitySearchCriteria criteria) throws IdentityException;

}

