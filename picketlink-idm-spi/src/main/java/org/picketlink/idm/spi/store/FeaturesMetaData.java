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

import java.util.Set;

import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.spi.model.IdentityObjectRelationshipType;
import org.picketlink.idm.spi.model.IdentityObjectCredentialType;
import org.picketlink.idm.common.exception.IdentityException;

/**
 * Describe the features supported by an Identity Store
 * 
 * @author boleslaw dot dawidowicz at redhat anotherdot com
 * @author Anil.Saldhana@redhat.com
 * @since Jul 10, 2008
 */
public interface FeaturesMetaData
{ 
   /**
    * Check if IdentityObjects with a given IdentityObjectType can be created and removed
    *
    * @param objectType
    * @return
    */
   boolean isIdentityObjectAddRemoveSupported(IdentityObjectType objectType);


   /**
    * Check if RelationshipNames can be created and removed
    *
    * @return
    */
   boolean isRelationshipNameAddRemoveSupported();

   /**
    *
    * @param identityObjectType
    * @param storeSearchConstraint
    * @return
    */
   boolean isSearchCriteriaTypeSupported(IdentityObjectType identityObjectType, IdentityObjectSearchCriteriaType storeSearchConstraint);

   /**
    *
    * @param constraint
    * @return
    */
   boolean isRoleNameSearchCriteriaTypeSupported(IdentityObjectSearchCriteriaType constraint);

   /**
    * @return set of identity types that can be persisted
    */
   Set<String> getSupportedIdentityObjectTypes();

   /**
    * @param identityObjectType
    * @return boolean describing if given identity type can be persisted or retrieved using this identity store
    */
   boolean isIdentityObjectTypeSupported(IdentityObjectType identityObjectType);

   /**
    * @param fromType
    * @param toType
    * @param relationshipType
    * @return true is given relationship can be persisted or retrieved
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   boolean isRelationshipTypeSupported(IdentityObjectType fromType, IdentityObjectType toType,
         IdentityObjectRelationshipType relationshipType)
   throws IdentityException;

   /**
    * 
    * @return
    */
   boolean isNamedRelationshipsSupported();

   /**
    * 
    * @return
    */
   boolean isRelationshipPropertiesSupported();


   /**
    * @return Set of relationship type names supported in this store
    */
   Set<String> getSupportedRelationshipTypes();

   /**
    * 
    * @param identityObjectType
    * @param credentialType
    * @return
    */
   boolean isCredentialSupported(IdentityObjectType identityObjectType, IdentityObjectCredentialType credentialType);

}
