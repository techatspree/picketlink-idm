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

package org.picketlink.idm.impl.cache;

import org.picketlink.idm.spi.store.IdentityStore;
import org.picketlink.idm.spi.store.FeaturesMetaData;
import org.picketlink.idm.spi.store.IdentityStoreInvocationContext;
import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.spi.model.IdentityObjectRelationshipType;
import org.picketlink.idm.spi.model.IdentityObjectRelationship;
import org.picketlink.idm.spi.model.IdentityObjectCredential;
import org.picketlink.idm.spi.exception.OperationNotSupportedException;
import org.picketlink.idm.spi.configuration.IdentityStoreConfigurationContext;
import org.picketlink.idm.spi.search.IdentityObjectSearchCriteria;
import org.picketlink.idm.spi.cache.IdentityStoreCacheProvider;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.impl.types.SimpleIdentityObjectRelationship;

import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * IdentityStore implementation that wraps another IdentityStore and uses JBossCache to cache results.
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class JBossCacheIdentityStoreWrapper extends JBossCacheAttributeStoreWrapper implements IdentityStore
{

   private static Logger log = Logger.getLogger(JBossCacheIdentityStoreWrapper.class.getName());


   //TODO: cache and IdentitySession transaction

   private final IdentityStore identityStore;

   public JBossCacheIdentityStoreWrapper(IdentityStore identityStore, IdentityStoreCacheProvider cacheSupport, String cacheScope) throws IdentityException
   {
      super(identityStore, cacheSupport, cacheScope);

      this.identityStore = identityStore;

      log.fine("------------------------------------------------------");
      log.fine("JBossCacheIdentityStoreWrapper created ....." +
         "(IdentityStore: " + identityStore.getId() + "; cache scope: " + cacheScope + ")");
      log.fine("------------------------------------------------------");

   }

   public void bootstrap(IdentityStoreConfigurationContext configurationContext) throws IdentityException
   {
      identityStore.bootstrap(configurationContext);
   }

   

   public FeaturesMetaData getSupportedFeatures()
   {
      return identityStore.getSupportedFeatures();
   }

   

   public IdentityObject createIdentityObject(IdentityStoreInvocationContext invocationCtx,
                                              String name,
                                              IdentityObjectType identityObjectType) throws IdentityException
   {
      

      IdentityObject io = identityStore.createIdentityObject(invocationCtx, name, identityObjectType);



      if (io != null)
      {
         cacheSupport.invalidateIdentityObjectRelationshipNameSearches(getCacheNS(invocationCtx));
         cacheSupport.invalidateIdentityObjectRelationshipSearches(getCacheNS(invocationCtx));
         cacheSupport.invalidateIdentityObjectSearches(getCacheNS(invocationCtx));
         cacheSupport.invalidateIdentityObjectCount(getCacheNS(invocationCtx), identityObjectType.getName());

      }
      return io;
   }



   public IdentityObject createIdentityObject(IdentityStoreInvocationContext invocationCtx,
                                              String name,
                                              IdentityObjectType identityObjectType,
                                              Map<String, String[]> attributes) throws IdentityException
   {
      IdentityObject io = identityStore.createIdentityObject(invocationCtx, name, identityObjectType, attributes);

      if (io != null)
      {

         cacheSupport.invalidateIdentityObjectRelationshipNameSearches(getCacheNS(invocationCtx));
         cacheSupport.invalidateIdentityObjectRelationshipSearches(getCacheNS(invocationCtx));
         cacheSupport.invalidateIdentityObjectSearches(getCacheNS(invocationCtx));
         cacheSupport.invalidateIdentityObjectCount(getCacheNS(invocationCtx), identityObjectType.getName());


      }
      return io;
   }

   public void removeIdentityObject(IdentityStoreInvocationContext invocationCtx,
                                    IdentityObject identity) throws IdentityException
   {
      identityStore.removeIdentityObject(invocationCtx, identity);

      cacheSupport.invalidateIdentityObjectRelationshipNameSearches(getCacheNS(invocationCtx));
      cacheSupport.invalidateIdentityObjectRelationshipSearches(getCacheNS(invocationCtx));
      cacheSupport.invalidateIdentityObjectSearches(getCacheNS(invocationCtx));
      cacheSupport.invalidateIdentityObjectAttriubtes(getCacheNS(invocationCtx), identity);
      cacheSupport.invalidateIdentityObjectCount(getCacheNS(invocationCtx), identity.getIdentityType().getName());

   }

   public int getIdentityObjectsCount(IdentityStoreInvocationContext invocationCtx,
                                      IdentityObjectType identityType) throws IdentityException
   {
      int count = cacheSupport.getIdentityObjectCount(getCacheNS(invocationCtx), identityType.getName());

      if (count != -1)
      {
         return count;
      }
      count = identityStore.getIdentityObjectsCount(invocationCtx, identityType);

      cacheSupport.putIdentityObjectCount(getCacheNS(invocationCtx), identityType.getName(), count);

      return count;

   }

   public IdentityObject findIdentityObject(IdentityStoreInvocationContext invocationContext,
                                            String name,
                                            IdentityObjectType identityObjectType) throws IdentityException
   {
      IdentityObjectSearchImpl search = new IdentityObjectSearchImpl();
      search.setName(name);
      if (identityObjectType != null)
      {
         search.setType(identityObjectType.getName());
      }
      Collection<IdentityObject> results = cacheSupport.getIdentityObjectSearch(getCacheNS(invocationContext), search);

      if (results != null && results.size() == 1)
      {
         return results.iterator().next();
      }

      IdentityObject io = identityStore.findIdentityObject(invocationContext, name, identityObjectType);

      if (io != null)
      {
         Set<IdentityObject> temp = new HashSet<IdentityObject>();
         temp.add(io);
         cacheSupport.putIdentityObjectSearch(getCacheNS(invocationContext), search, temp);
      }

      return io;
   }

   public IdentityObject findIdentityObject(IdentityStoreInvocationContext invocationContext,
                                            String id) throws IdentityException
   {

      IdentityObjectSearchImpl search = new IdentityObjectSearchImpl();
      search.setId(id);

      Collection<IdentityObject> results = cacheSupport.getIdentityObjectSearch(getCacheNS(invocationContext), search);

      if (results != null && results.size() == 1)
      {                                                                            
         return results.iterator().next();
      }

      IdentityObject io = identityStore.findIdentityObject(invocationContext, id);

      if (io != null)
      {
         Set<IdentityObject> temp = new HashSet<IdentityObject>();
         temp.add(io);
         cacheSupport.putIdentityObjectSearch(getCacheNS(invocationContext), search, temp);
      }

      return io;
   }

   public Collection<IdentityObject> findIdentityObject(IdentityStoreInvocationContext invocationCtx,
                                                        IdentityObjectType identityType,
                                                        IdentityObjectSearchCriteria criteria) throws IdentityException
   {

      //TODO: fix

//      IdentityObjectSearchImpl search = new IdentityObjectSearchImpl();
//      search.setIdentityObjectSearchCriteria(criteria);
//      if (identityType != null)
//      {
//         search.setType(identityType.getName());
//      }

//      Collection<IdentityObject> results = cacheSupport.getIdentityObjectSearch(getCacheNS(invocationCtx), search);

//      if (results != null)
//      {
//         return results;
//      }

      Collection<IdentityObject> results = identityStore.findIdentityObject(invocationCtx, identityType, criteria);

//      cacheSupport.putIdentityObjectSearch(getCacheNS(invocationCtx), search, results);

      return results;



   }

   

   public Collection<IdentityObject> findIdentityObject(IdentityStoreInvocationContext invocationCtx,
                                                        IdentityObject identity,
                                                        IdentityObjectRelationshipType relationshipType,
                                                        boolean parent,
                                                        IdentityObjectSearchCriteria criteria) throws IdentityException
   {

      IdentityObjectSearchImpl search = new IdentityObjectSearchImpl();
      search.setIdentityObjectSearchCriteria(criteria);
      search.setParent(parent);
      if (relationshipType != null)
      {
         search.setRelationshipType(relationshipType.getName());
      }
      if (identity != null)
      {
         search.setName(identity.getName());
         search.setType(identity.getIdentityType().getName());
      }

      Collection<IdentityObject> results = cacheSupport.getIdentityObjectSearch(getCacheNS(invocationCtx), search);

      if (results != null)
      {
         //TODO
         return results;
      }

      results = identityStore.findIdentityObject(invocationCtx, identity, relationshipType, parent, criteria);

      cacheSupport.putIdentityObjectSearch(getCacheNS(invocationCtx), search, results);

      return results;
   }




   public IdentityObjectRelationship createRelationship(IdentityStoreInvocationContext invocationCxt,
                                                        IdentityObject fromIdentity,
                                                        IdentityObject toIdentity,
                                                        IdentityObjectRelationshipType relationshipType,
                                                        String relationshipName,
                                                        boolean createNames) throws IdentityException
   {
      cacheSupport.invalidateIdentityObjectRelationshipNameSearches(getCacheNS(invocationCxt));
      cacheSupport.invalidateIdentityObjectRelationshipSearches(getCacheNS(invocationCxt));
      cacheSupport.invalidateIdentityObjectSearches(getCacheNS(invocationCxt));

      return identityStore.createRelationship(invocationCxt,
         fromIdentity, toIdentity, relationshipType, relationshipName, createNames);


   }

   public void removeRelationship(IdentityStoreInvocationContext invocationCxt,
                                  IdentityObject fromIdentity,
                                  IdentityObject toIdentity,
                                  IdentityObjectRelationshipType relationshipType,
                                  String relationshipName) throws IdentityException
   {
      identityStore.removeRelationship(invocationCxt, fromIdentity, toIdentity, relationshipType, relationshipName);

      cacheSupport.invalidateIdentityObjectRelationshipNameSearches(getCacheNS(invocationCxt));
      cacheSupport.invalidateIdentityObjectRelationshipSearches(getCacheNS(invocationCxt));
      cacheSupport.invalidateIdentityObjectSearches(getCacheNS(invocationCxt));
      IdentityObjectRelationship relationship = new SimpleIdentityObjectRelationship(fromIdentity, toIdentity, relationshipName, relationshipType);
      cacheSupport.invalidateRelationshipProperties(getCacheNS(invocationCxt), relationship);

   }




   public void removeRelationships(IdentityStoreInvocationContext invocationCtx,
                                   IdentityObject identity1,
                                   IdentityObject identity2,
                                   boolean named) throws IdentityException
   {
      identityStore.removeRelationships(invocationCtx, identity1, identity2, named);

      cacheSupport.invalidateIdentityObjectRelationshipNameSearches(getCacheNS(invocationCtx));
      cacheSupport.invalidateIdentityObjectRelationshipSearches(getCacheNS(invocationCtx));
      cacheSupport.invalidateIdentityObjectSearches(getCacheNS(invocationCtx));
      cacheSupport.invalidateRelationshipProperties(getCacheNS(invocationCtx));
   }


   public Set<IdentityObjectRelationship> resolveRelationships(IdentityStoreInvocationContext invocationCxt,
                                                               IdentityObject fromIdentity,
                                                               IdentityObject toIdentity,
                                                               IdentityObjectRelationshipType relationshipType) throws IdentityException
   {

      IdentityObjectRelationshipSearchImpl search = new IdentityObjectRelationshipSearchImpl();
      if (fromIdentity != null)
      {
         search.setFromIOName(fromIdentity.getName());
         search.setFromIOType(fromIdentity.getIdentityType().getName());
      }
      if (toIdentity != null)
      {
         search.setToIOName(toIdentity.getName());
         search.setToIOType(toIdentity.getIdentityType().getName());
      }
      if (relationshipType != null)
      {
         search.setRelationshipType(relationshipType.getName());
      }
      
      Set<IdentityObjectRelationship> results = cacheSupport.getIdentityObjectRelationshipSearch(getCacheNS(invocationCxt), search);

      if (results != null)
      {
         return results;
      }

      results = identityStore.resolveRelationships(invocationCxt, fromIdentity, toIdentity, relationshipType);

      cacheSupport.putIdentityObjectRelationshipSearch(getCacheNS(invocationCxt), search, results);

      return results;

   }

   public Set<IdentityObjectRelationship> resolveRelationships(IdentityStoreInvocationContext invocationCtx,
                                                               IdentityObject identity,
                                                               IdentityObjectRelationshipType relationshipType,
                                                               boolean parent,
                                                               boolean named,
                                                               String name) throws IdentityException
   {

      IdentityObjectRelationshipSearchImpl search = new IdentityObjectRelationshipSearchImpl();
      if (identity != null)
      {
         search.setIoName(identity.getName());
         search.setIoType(identity.getIdentityType().getName());
      }
      if (relationshipType != null)
      {
         search.setRelationshipType(relationshipType.getName());
      }
      search.setParent(parent);
      search.setNamed(named);
      search.setName(name);

      Set<IdentityObjectRelationship> results = cacheSupport.getIdentityObjectRelationshipSearch(getCacheNS(invocationCtx), search);

      if (results != null)
      {
         return results;
      }

      results = identityStore.resolveRelationships(invocationCtx,
         identity, relationshipType, parent, named, name);

      cacheSupport.putIdentityObjectRelationshipSearch(getCacheNS(invocationCtx), search, results);


      return results;
   }

   public String createRelationshipName(IdentityStoreInvocationContext ctx,
                                        String name) throws IdentityException, OperationNotSupportedException
   {

      cacheSupport.invalidateIdentityObjectRelationshipNameSearches(getCacheNS(ctx));
      cacheSupport.invalidateIdentityObjectRelationshipSearches(getCacheNS(ctx));
      cacheSupport.invalidateIdentityObjectSearches(getCacheNS(ctx));

      return identityStore.createRelationshipName(ctx, name);
   }

   public String removeRelationshipName(IdentityStoreInvocationContext ctx,
                                        String name) throws IdentityException, OperationNotSupportedException
   {
      cacheSupport.invalidateIdentityObjectRelationshipNameSearches(getCacheNS(ctx));
      cacheSupport.invalidateIdentityObjectRelationshipSearches(getCacheNS(ctx));
      cacheSupport.invalidateIdentityObjectSearches(getCacheNS(ctx));
      cacheSupport.invalidateRelationshipNameProperties(getCacheNS(ctx), name);

      return identityStore.removeRelationshipName(ctx, name);
   }

   public Set<String> getRelationshipNames(IdentityStoreInvocationContext ctx,
                                           IdentityObjectSearchCriteria criteria) throws IdentityException, OperationNotSupportedException
   {

      IdentityObjectRelationshipNameSearchImpl search = new IdentityObjectRelationshipNameSearchImpl();
      search.setIdentityObjectSearchCriteria(criteria);

      Set<String> results = cacheSupport.getIdentityObjectRelationshipNameSearch(getCacheNS(ctx), search);

      if (results != null)
      {
         return results;
      }

      results = identityStore.getRelationshipNames(ctx, criteria);

      cacheSupport.putIdentityObjectRelationshipNameSearch(getCacheNS(ctx), search, results);

      return results;

   }

   public Set<String> getRelationshipNames(IdentityStoreInvocationContext ctx,
                                           IdentityObject identity,
                                           IdentityObjectSearchCriteria criteria) throws IdentityException, OperationNotSupportedException
   {

      IdentityObjectRelationshipNameSearchImpl search = new IdentityObjectRelationshipNameSearchImpl();
      if (identity != null)
      {
         search.setIoName(identity.getName());
         search.setIoType(identity.getIdentityType().getName());
      }
      search.setIdentityObjectSearchCriteria(criteria);

      Set<String> results = cacheSupport.getIdentityObjectRelationshipNameSearch(getCacheNS(ctx), search);

      if (results != null)
      {
         return results;
      }

      results = identityStore.getRelationshipNames(ctx, identity, criteria);

      cacheSupport.putIdentityObjectRelationshipNameSearch(getCacheNS(ctx), search, results);

      return results;
   }


   public Map<String, String> getRelationshipNameProperties(IdentityStoreInvocationContext ctx, String name) throws IdentityException, OperationNotSupportedException
   {
      Map<String, String> properties = cacheSupport.getProperties(getCacheNS(ctx), name);

      if (properties != null)
      {
         return properties;
      }

      properties = identityStore.getRelationshipNameProperties(ctx, name);

      cacheSupport.putProperties(getCacheNS(ctx), name, properties);

      return properties;

   }

   public void setRelationshipNameProperties(IdentityStoreInvocationContext ctx, String name, Map<String, String> properties) throws IdentityException, OperationNotSupportedException
   {

      identityStore.setRelationshipNameProperties(ctx, name, properties);

      cacheSupport.invalidateRelationshipNameProperties(getCacheNS(ctx), name);
      cacheSupport.putProperties(getCacheNS(ctx), name, getRelationshipNameProperties(ctx, name));
   }

   public void removeRelationshipNameProperties(IdentityStoreInvocationContext ctx, String name, Set<String> properties) throws IdentityException, OperationNotSupportedException
   {
      identityStore.removeRelationshipNameProperties(ctx, name, properties);

      cacheSupport.invalidateRelationshipNameProperties(getCacheNS(ctx), name);
      cacheSupport.putProperties(getCacheNS(ctx), name, getRelationshipNameProperties(ctx, name));

   }

   public Map<String, String> getRelationshipProperties(IdentityStoreInvocationContext ctx, IdentityObjectRelationship relationship) throws IdentityException, OperationNotSupportedException
   {
      Map<String, String> properties = cacheSupport.getProperties(getCacheNS(ctx), relationship);

      if (properties != null)
      {
         return properties;
      }

      properties = identityStore.getRelationshipProperties(ctx, relationship);

      cacheSupport.putProperties(getCacheNS(ctx), relationship, properties);

      return properties;

   }

   public void setRelationshipProperties(IdentityStoreInvocationContext ctx, IdentityObjectRelationship relationship, Map<String, String> properties) throws IdentityException, OperationNotSupportedException
   {
      identityStore.setRelationshipProperties(ctx, relationship, properties);
      
      cacheSupport.invalidateRelationshipProperties(getCacheNS(ctx), relationship);
      cacheSupport.putProperties(getCacheNS(ctx), relationship, getRelationshipProperties(ctx, relationship));

   }

   public void removeRelationshipProperties(IdentityStoreInvocationContext ctx, IdentityObjectRelationship relationship, Set<String> properties) throws IdentityException, OperationNotSupportedException
   {
      identityStore.removeRelationshipProperties(ctx, relationship, properties);

      cacheSupport.invalidateRelationshipProperties(getCacheNS(ctx), relationship);
      cacheSupport.putProperties(getCacheNS(ctx), relationship, getRelationshipProperties(ctx, relationship));
   }

   public boolean validateCredential(IdentityStoreInvocationContext ctx,
                                     IdentityObject identityObject,
                                     IdentityObjectCredential credential) throws IdentityException
   {
      // Should not be cached
      return identityStore.validateCredential(ctx, identityObject, credential);
   }

   public void updateCredential(IdentityStoreInvocationContext ctx,
                                IdentityObject identityObject,
                                IdentityObjectCredential credential) throws IdentityException
   {
      // Should not be cached
      identityStore.updateCredential(ctx, identityObject, credential);
   }

   @Override
   public String toString()
   {
      return "JBossCacheIdentityStoreWrapper (IdentityStore=" + identityStore.getId() + ")";
   }

}
