/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
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
import org.picketlink.idm.common.exception.FeatureNotSupportedException;
import org.picketlink.idm.api.query.RoleQuery;
import org.picketlink.idm.api.query.GroupQuery;
import org.picketlink.idm.api.query.UserQuery;
import org.picketlink.idm.api.query.GroupQueryBuilder;
import org.picketlink.idm.api.query.UserQueryBuilder;
import org.picketlink.idm.api.query.RoleQueryBuilder;
import org.picketlink.idm.api.query.QueryException;
import org.picketlink.idm.api.event.EventListener;

import java.util.Collection;
import java.util.List;

/**
 * Expose all identity management operations within a given realm
 *
 * <p>An Identity Session</p>
 * @author boleslaw dot dawidowicz at redhat anotherdot com
 * @author Anil.Saldhana@redhat.com
 * @since Jul 10, 2008
 */
public interface IdentitySession
{

   /**
    * @return Name of the realm this session is connected to
    */
   String getRealmName();

   /**
    * Close this session
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void close() throws IdentityException;

   /**
    * Save all pending changes
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void save() throws IdentityException;

   /**
    * Clear this session
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void clear() throws IdentityException;

   /**
    * Check if this session is open
    * @return
    */
   boolean isOpen();

   /**
    * @return
    * @throws IdentityException
    */
   Transaction beginTransaction() throws IdentityException;

   /**
    * Transaction instance assosiated with this session
    * @return
    */
   Transaction getTransaction() throws IdentityException;

   /**
    * PersistenceManager exposes management operations on Group and Identity objects
    * @return
    */
   PersistenceManager getPersistenceManager();

   /**
    * RelationshipManager enables to associate and disassociate Group and Identity objects
    * @return
    */
   RelationshipManager getRelationshipManager();

   /**
    * AttributesManager exposes operation related to Attribute objects. It enables to associate and disassociate Attribute
    * with a given Group and Identity objects
    * @return
    * @throws FeatureNotSupportedException
    */
   AttributesManager getAttributesManager();

   /**
    * RoleManager exposes operation on Role objects. This is optional feature that may be not supported
    * @return
    * @throws FeatureNotSupportedException
    */
   RoleManager getRoleManager() throws FeatureNotSupportedException ;

   /**
    * Create UserQueryBuilder object
    *
    * @return
    */
   UserQueryBuilder createUserQueryBuilder();

   /**
    * Create GroupQueryBuilder object
    *
    * @return
    */
   GroupQueryBuilder createGroupQueryBuilder();

   /**
    * Create RoleQueryBuilder object
    *
    * @return
    */
   RoleQueryBuilder createRoleQueryBuilder() throws FeatureNotSupportedException;

   /**
    * Execute UserQuery
    *
    * @param userQuery
    * @return
    * @throws QueryException
    */
   Collection<User> execute(UserQuery userQuery) throws QueryException;

   /**
    * Execute UserQuery and get unique result
    *
    * @param userQuery
    * @return
    * @throws QueryException
    */
   User uniqueResult(UserQuery userQuery) throws QueryException;

   /**
    * Execute UserQuery and get results as List
    *
    * @param userQuery
    * @return
    * @throws QueryException
    */
   List<User> list(UserQuery userQuery) throws QueryException;

   /**
    * Execute GroupQuery
    *
    * @param groupQuery
    * @return
    * @throws QueryException
    */
   Collection<Group> execute(GroupQuery groupQuery) throws QueryException;

   /**
    * Execute GroupQuery and get unique result
    *
    * @param groupQuery
    * @return
    * @throws QueryException
    */
   Group uniqueResult(GroupQuery groupQuery) throws QueryException;

   /**
    * Execute GroupQuery and get results as List
    *
    * @param groupQuery
    * @return
    * @throws QueryException
    */
   List<Group> list(GroupQuery groupQuery) throws QueryException;

   /**
    * Execute RoleQuery
    *
    * @param roleQuery
    * @return
    * @throws QueryException
    */
   Collection<Role> execute(RoleQuery roleQuery) throws QueryException;

   /**
    * Execute RoleQuery and get unique result
    *
    * @param roleQuery
    * @return
    * @throws QueryException
    */
   Role uniqueResult(RoleQuery roleQuery) throws QueryException;

   /**
    * Execute RoleQuery and get results as List
    * 
    * @param roleQuery
    * @return
    * @throws QueryException
    */
   List<Role> list(RoleQuery roleQuery) throws QueryException;

   /**
    * Register EventListener
    * 
    * @param listener
    */
   void registerListener(EventListener listener);

}