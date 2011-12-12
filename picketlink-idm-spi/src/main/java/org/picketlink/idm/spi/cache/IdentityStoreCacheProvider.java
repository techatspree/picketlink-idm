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

package org.picketlink.idm.spi.cache;

import org.picketlink.idm.spi.configuration.IdentityConfigurationContext;
import org.picketlink.idm.spi.model.IdentityObjectAttribute;
import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectRelationshipType;
import org.picketlink.idm.spi.model.IdentityObjectRelationship;
import org.picketlink.idm.spi.search.IdentityObjectSearchCriteria;
import org.picketlink.idm.spi.configuration.IdentityRepositoryConfigurationContext;

import java.util.Map;
import java.util.Collection;
import java.util.Set;

/**
 * Cache provider for Identity SPI. Namespaces enable flexible use (per realm or per session)
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface IdentityStoreCacheProvider
{

   /**
    * Initialize provider.
    * @param properties
    */
   void initialize(Map<String, String> properties, IdentityConfigurationContext configurationContext);


   /**
    * Generate realm namespace.
    *
    * @param storeId - if null will generate root namespace
    * @return
    */
   String getNamespace(String storeId);

   /**
    * Generate namespace based on realm and session ids.
    *
    * @param storeId if null will generate root namespace
    * @param realmId if null will generate store namespace
    * @return
    */
   String getNamespace(String storeId, String realmId);

   /**
    * Generate namespace based on realm and session ids.
    *
    * @param storeId if null will generate root namespace
    * @param realmId if null will generate store namespace
    * @param sessionId if null will generate realm namespace
    * @return
    */
   String getNamespace(String storeId, String realmId, String sessionId);

   void invalidate(String ns);

   void invalidateAll();

   //

   void putIdentityObjectCount(String ns, String type, int count);

   int getIdentityObjectCount(String ns, String type);

   void invalidateIdentityObjectCount(String ns, String type);

   void putIdentityObjectSearch(String ns, IdentityObjectSearch search, Collection<IdentityObject> results);

   Collection<IdentityObject> getIdentityObjectSearch(String ns, IdentityObjectSearch search);

   void invalidateIdentityObjectSearches(String ns);

   //

   void putIdentityObjectRelationshipSearch(String ns, IdentityObjectRelationshipSearch search, Set<IdentityObjectRelationship> results);

   Set<IdentityObjectRelationship> getIdentityObjectRelationshipSearch(String ns, IdentityObjectRelationshipSearch search);

   void invalidateIdentityObjectRelationshipSearches(String ns);

   //

   void putIdentityObjectRelationshipNameSearch(String ns, IdentityObjectRelationshipNameSearch search, Set<String> results);

   Set<String> getIdentityObjectRelationshipNameSearch(String ns, IdentityObjectRelationshipNameSearch search);

   void invalidateIdentityObjectRelationshipNameSearches(String ns);

   //

   void putProperties(String ns, IdentityObjectRelationship relationship, Map<String, String> properties);

   Map<String, String> getProperties(String ns, IdentityObjectRelationship relationship);

   void invalidateRelationshipProperties(String ns, IdentityObjectRelationship relationship);

   void invalidateRelationshipProperties(String ns);

   void putProperties(String ns, String name, Map<String, String> properties);

   Map<String, String> getProperties(String ns, String name);

   void invalidateRelationshipNameProperties(String ns, String relationship);

   void invalidateRelationshipNameProperties(String ns);

   //

   void putIdentityObjectAttributes(String ns, IdentityObject identityObject, Map<String, IdentityObjectAttribute> attributes);

   Map<String, IdentityObjectAttribute> getIdentityObjectAttributes(String ns, IdentityObject identityObject);

   void invalidateIdentityObjectAttriubtes(String ns, IdentityObject identityObject);

   void invalidateIdentityObjectAttriubtes(String ns);

   //

   void putObject(String ns, int hash, Object value);

   Object getObject(String ns, int hash);

   void invalidateObject(String ns, int hash);

   void invalidateObjects(String ns);

}
