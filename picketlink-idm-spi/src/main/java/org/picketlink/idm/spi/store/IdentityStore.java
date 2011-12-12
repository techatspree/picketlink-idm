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
package org.picketlink.idm.spi.store;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.Map;

import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.spi.model.IdentityObjectRelationshipType;
import org.picketlink.idm.spi.model.IdentityObjectRelationship;
import org.picketlink.idm.spi.exception.OperationNotSupportedException;
import org.picketlink.idm.spi.configuration.IdentityStoreConfigurationContext;
import org.picketlink.idm.spi.model.IdentityObjectCredential;
import org.picketlink.idm.spi.search.IdentityObjectSearchCriteria;
import org.picketlink.idm.common.exception.IdentityException;

/**
 * Represents an Identity Store. Implementation of this interface perform operations on real identity storage like DB or LDAP
 *
 * @author boleslaw dot dawidowicz at redhat anotherdot com
 * @author Anil.Saldhana@redhat.com
 * @since Jul 10, 2008
 */
public interface IdentityStore extends AttributeStore 
{
   
   /**
    * Set up the store
    * 
    * @param configurationContext
    * @throws IOException
    */
   void bootstrap(IdentityStoreConfigurationContext configurationContext) throws IdentityException;

   /**
    * @return id of this identity store
    */
   String getId();


   /**
    * @return FeaturesDescription object describing what
    *         operation are supported by this store
    */
   FeaturesMetaData getSupportedFeatures();

   // Operations

   /**
    * Create new identity with a given name
    *
    * @param invocationCtx
    * @param name
    * @param identityObjectType
    * @return
    * @throws IdentityException
    */
   IdentityObject createIdentityObject(IdentityStoreInvocationContext invocationCtx,
                                       String name,
                                       IdentityObjectType identityObjectType) throws IdentityException;

   /**
    * Create new identity with a given name
    *
    * @param invocationCtx
    * @param name
    * @param identityObjectType
    * @return
    * @throws IdentityException
    */
   IdentityObject createIdentityObject(IdentityStoreInvocationContext invocationCtx,
                                       String name,
                                       IdentityObjectType identityObjectType,
                                       Map<String, String[]> attributes) throws IdentityException;




   /**
    * Remove given identity
    *
    * @param invocationCtx
    * @param identity
    * @return
    * @throws IdentityException
    */
   void removeIdentityObject(IdentityStoreInvocationContext invocationCtx, IdentityObject identity) throws IdentityException;

   /**
    * @param invocationCtx
    * @param identityType
    * @return a number of stored identities with a given type
    * @throws IdentityException
    */
   int getIdentityObjectsCount(IdentityStoreInvocationContext invocationCtx, IdentityObjectType identityType)
      throws IdentityException;

   /**
    * Find identity with a given name
    *
    * @param invocationContext
    * @param name
    * @param identityObjectType
    * @return
    * @throws IdentityException
    */
   IdentityObject findIdentityObject(IdentityStoreInvocationContext invocationContext, String name, IdentityObjectType identityObjectType) throws IdentityException;

   /**
    * Find identity with a given id
    *
    * @param invocationContext
    * @param id
    * @return
    * @throws IdentityException
    */
   IdentityObject findIdentityObject(IdentityStoreInvocationContext invocationContext, String id) throws IdentityException;


   /**
    * Find identities with a given type
    * 
    * @param invocationCtx
    * @param identityType
    * @param criteria
    * @return
    * @throws IdentityException
    */
   Collection<IdentityObject> findIdentityObject(IdentityStoreInvocationContext invocationCtx,
                                                 IdentityObjectType identityType,
                                                 IdentityObjectSearchCriteria criteria) throws IdentityException;
   /**
    * Find identites that have relationship with given identity. Relationships are directional (from parent to child).
    *
    * @param invocationCxt
    * @param identity
    * @param relationshipType
    * @param parent defines if given identity is parent or child side in the
    *                         relationship - default is true (parent)
    * @param criteria
    * @return
    * @throws IdentityException
    */
   Collection<IdentityObject> findIdentityObject(IdentityStoreInvocationContext invocationCxt, IdentityObject identity,
                                                    IdentityObjectRelationshipType relationshipType,
                                                    boolean parent,
                                                    IdentityObjectSearchCriteria criteria) throws IdentityException;



   /**
    * Create directional relationship of a given type between identities
    *
    * @param invocationCxt
    * @param fromIdentity
    * @param toIdentity
    * @param relationshipType
    * @param relationshipName
    * @param createNames
    * @throws IdentityException
    */
   IdentityObjectRelationship createRelationship(IdentityStoreInvocationContext invocationCxt, IdentityObject fromIdentity,
                           IdentityObject toIdentity,
                           IdentityObjectRelationshipType relationshipType, String relationshipName, boolean createNames) throws IdentityException;

   /**
    * Remove relationship between identities. Relationships can be directional so
    * order of parameters matters
    *
    * @param invocationCxt
    * @param fromIdentity
    * @param toIdentity
    * @param relationshipType
    * @param relationshipName
    * @throws IdentityException
    */
   void removeRelationship(IdentityStoreInvocationContext invocationCxt, IdentityObject fromIdentity,
                           IdentityObject toIdentity,
                           IdentityObjectRelationshipType relationshipType, String relationshipName) throws IdentityException;

   /**
    * Remove all relationships between identities. Direction of relationships doesn't
    * matter - all active relationships
    * will be removed
    *
    * @param invocationCtx
    * @param identity1
    * @param identity2
    * @param named if false method will remove only relationship withot names. Default is true
    * @return
    * @throws IdentityException
    */
   void removeRelationships(IdentityStoreInvocationContext invocationCtx, IdentityObject identity1, IdentityObject identity2, boolean named)
      throws IdentityException;

   /**
    * Resolve relationships between two identities. Relationships can be directional
    * so order of parameters matters
    *
    * @param invocationCxt
    * @param fromIdentity
    * @param toIdentity
    * @param relationshipType - may be null
    * @return
    * @throws IdentityException
    */
   Set<IdentityObjectRelationship> resolveRelationships(IdentityStoreInvocationContext invocationCxt,
                                                        IdentityObject fromIdentity,
                                                        IdentityObject toIdentity,
                                                        IdentityObjectRelationshipType relationshipType)
      throws IdentityException;


   /**
    * Resolve relationships for a given IdentityObject. Relationships can be directional and parent switch defines which
    * role identity play in it.
    * @param invocationCxt
    * @param identity
    * @param named
    * @param name - can be null 
    * @return
    * @throws IdentityException
    */
   Set<IdentityObjectRelationship> resolveRelationships(IdentityStoreInvocationContext invocationCxt,
                                                        IdentityObject identity,
                                                        IdentityObjectRelationshipType relationshipType,
                                                        boolean parent,
                                                        boolean named,
                                                        String name)
      throws IdentityException;


   // Named relationships

   /**
    *
    * @param ctx
    * @param name
    * @return
    */
   String createRelationshipName(IdentityStoreInvocationContext ctx, String name) throws IdentityException, OperationNotSupportedException;

   /**
    *
    * @param ctx
    * @param name
    * @return
    */
   String removeRelationshipName(IdentityStoreInvocationContext ctx, String name)
      throws IdentityException, OperationNotSupportedException;

   /**
    *
    * @param ctx
    * @param name
    * @return
    * @throws IdentityException
    * @throws OperationNotSupportedException
    */
   Map<String, String> getRelationshipNameProperties(IdentityStoreInvocationContext ctx, String name)
      throws IdentityException, OperationNotSupportedException;

   /**
    *
    * @param ctx
    * @param name
    * @param properties
    * @throws IdentityException
    * @throws OperationNotSupportedException
    */
   void setRelationshipNameProperties(IdentityStoreInvocationContext ctx, String name, Map<String, String> properties)
      throws IdentityException, OperationNotSupportedException;

   /**
    * 
    * @param ctx
    * @param name
    * @param properties
    * @throws IdentityException
    * @throws OperationNotSupportedException
    */
   void removeRelationshipNameProperties(IdentityStoreInvocationContext ctx, String name, Set<String> properties)
      throws IdentityException, OperationNotSupportedException;

   /**
    *
    * @param ctx
    * @param relationship
    * @return
    * @throws IdentityException
    * @throws OperationNotSupportedException
    */
   Map<String, String> getRelationshipProperties(IdentityStoreInvocationContext ctx, IdentityObjectRelationship relationship)
      throws IdentityException, OperationNotSupportedException;

   /**
    *
    * @param ctx
    * @param relationship
    * @param properties
    * @throws IdentityException
    * @throws OperationNotSupportedException
    */
   void setRelationshipProperties(IdentityStoreInvocationContext ctx, IdentityObjectRelationship relationship, Map<String, String> properties)
      throws IdentityException, OperationNotSupportedException;

   /**
    *
    * @param ctx
    * @param relationship
    * @param properties
    * @throws IdentityException
    * @throws OperationNotSupportedException
    */
   void removeRelationshipProperties(IdentityStoreInvocationContext ctx, IdentityObjectRelationship relationship, Set<String> properties)
      throws IdentityException, OperationNotSupportedException;


   /**
    *
    * @param ctx
    * @param criteria
    * @return
    * @throws IdentityException
    * @throws OperationNotSupportedException
    */
   Set<String> getRelationshipNames(IdentityStoreInvocationContext ctx,
                                    IdentityObjectSearchCriteria criteria) throws IdentityException, OperationNotSupportedException;


   /**
    *
    * @param ctx
    * @param identity
    * @param criteria
    * @return
    * @throws IdentityException
    * @throws OperationNotSupportedException
    */
   Set<String> getRelationshipNames(IdentityStoreInvocationContext ctx,
                                    IdentityObject identity,
                                    IdentityObjectSearchCriteria criteria) throws IdentityException, OperationNotSupportedException;


   // Credentials

   /**
    *
    * @param ctx
    * @param identityObject
    * @param credential
    * @return
    * @throws IdentityException
    */
   boolean validateCredential(IdentityStoreInvocationContext ctx, IdentityObject identityObject, IdentityObjectCredential credential) throws IdentityException;

   /**
    * 
    * @param ctx
    * @param identityObject
    * @param credential
    * @throws IdentityException
    */
   void updateCredential(IdentityStoreInvocationContext ctx, IdentityObject identityObject, IdentityObjectCredential credential) throws IdentityException;

}
