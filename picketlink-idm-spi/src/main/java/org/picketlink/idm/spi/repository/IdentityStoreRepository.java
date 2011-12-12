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
package org.picketlink.idm.spi.repository;

import java.util.Map;
import java.util.Set;

import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.spi.store.IdentityStore;
import org.picketlink.idm.spi.store.AttributeStore;
import org.picketlink.idm.spi.configuration.IdentityRepositoryConfigurationContext;
import org.picketlink.idm.common.exception.IdentityException;

/** 
 * IdentityStoreRepository exposes identity object management 
 * operations and act as an entry point to many underlying data stores.
 * Its responsibility is to map identity objects, their state and relations 
 * between them across different identity stores. By extending IdentityStore
 * interface it exposes unified entry point
 * for all identity related operations in the SPI
 * @author boleslaw dot dawidowicz at redhat anotherdot com
 * @author Anil.Saldhana@redhat.com
 * @since Jul 10, 2008
 */
public interface IdentityStoreRepository extends IdentityStore
{

   public void bootstrap(IdentityRepositoryConfigurationContext configurationContext,
                         Map<String, IdentityStore> bootstrappedIdentityStores,
                         Map<String, AttributeStore> bootstrappedAttributeStores) throws IdentityException;

   /**
    * @return a set of configured identity stores
    */
   Set<IdentityStore> getConfiguredIdentityStores();

   /**
    * @return a set of configured attribute stores
    */
   Set<AttributeStore> getConfiguredAttributeStores();

   /**
    * @return mapping of IdentityType name to the specific identity store
    */
   Map<String, IdentityStore> getIdentityStoreMappings();

   /**
    * @return mapping of IdentityType name to the specific attribute store
    */
   Map<String, AttributeStore> getAttributeStoreMappings();

   /**
    * @param identityObjectType
    * @return proper identity store to store given identity type
    */
   IdentityStore getIdentityStore(IdentityObjectType identityObjectType) throws IdentityException;

   /**
    * @param identityObjectType
    * @return proper identity store to store given identity type
    */
   AttributeStore getAttributeStore(IdentityObjectType identityObjectType) throws IdentityException;


   /**
    * Return a list of relationship policies
    * @return
    */
   //List<RelationshipPolicy<IdentityObjectType, IdentityObjectType>> getRelationshipPolicies(IdentityObjectType identityObjectType);
   
}