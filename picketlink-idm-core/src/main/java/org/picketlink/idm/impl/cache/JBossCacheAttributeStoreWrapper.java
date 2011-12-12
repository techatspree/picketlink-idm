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

import org.picketlink.idm.spi.store.AttributeStore;
import org.picketlink.idm.spi.store.IdentityStoreInvocationContext;
import org.picketlink.idm.spi.store.IdentityStoreSession;
import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.spi.model.IdentityObjectAttribute;
import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.configuration.metadata.IdentityObjectAttributeMetaData;
import org.picketlink.idm.spi.cache.IdentityStoreCacheProvider;
import org.picketlink.idm.common.exception.IdentityException;

import java.util.logging.Logger;
import java.util.Set;
import java.util.Map;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class JBossCacheAttributeStoreWrapper implements AttributeStore
{

   private static Logger log = Logger.getLogger(JBossCacheAttributeStoreWrapper.class.getName());

   private final AttributeStore attributeStore;

   protected final IdentityStoreCacheProvider cacheSupport;

   protected final String cacheScope;

   public JBossCacheAttributeStoreWrapper(AttributeStore attributeStore, IdentityStoreCacheProvider cacheSupport, String cacheScope)
   {
      this.cacheSupport = cacheSupport;
      this.attributeStore = attributeStore;
      this.cacheScope = cacheScope;
   }

   protected String getCacheNS(IdentityStoreInvocationContext ctx)
   {

      if (cacheScope != null && cacheScope.equals("realm"))
      {
         return cacheSupport.getNamespace(attributeStore.getId(), ctx.getRealmId());
      }
      if (cacheScope != null && cacheScope.equals("session"))
      {
         return cacheSupport.getNamespace(attributeStore.getId(), ctx.getRealmId(), ctx.getSessionId());
      }

      return cacheSupport.getNamespace(attributeStore.getId());

   }

   public String getId()
   {
      return attributeStore.getId();
   }

   public IdentityStoreSession createIdentityStoreSession() throws IdentityException
   {
      return attributeStore.createIdentityStoreSession();
   }

   public Set<String> getSupportedAttributeNames(IdentityStoreInvocationContext invocationContext,
                                                 IdentityObjectType identityType) throws IdentityException
   {
      // Doesn't need to be cached
      return attributeStore.getSupportedAttributeNames(invocationContext, identityType);
   }

   public Map<String, IdentityObjectAttributeMetaData> getAttributesMetaData(IdentityStoreInvocationContext invocationContext,
                                                                             IdentityObjectType identityType)
   {
      // Doesn't need to be cached
      return attributeStore.getAttributesMetaData(invocationContext, identityType);
   }

   public Map<String, IdentityObjectAttribute> getAttributes(IdentityStoreInvocationContext invocationContext,
                                                             IdentityObject identity) throws IdentityException
   {

      Map<String, IdentityObjectAttribute> results = cacheSupport.getIdentityObjectAttributes(getCacheNS(invocationContext), identity);
      if (results != null)
      {
         return results;
      }

      results = attributeStore.getAttributes(invocationContext, identity);

      cacheSupport.putIdentityObjectAttributes(getCacheNS(invocationContext), identity, results);


      return results;
   }

   public IdentityObjectAttribute getAttribute(IdentityStoreInvocationContext invocationContext,
                                               IdentityObject identity,
                                               String name) throws IdentityException
   {
      Map<String, IdentityObjectAttribute> results = cacheSupport.getIdentityObjectAttributes(getCacheNS(invocationContext), identity);

      if (results != null && results.containsKey(name))
      {
         return results.get(name);
      }

      IdentityObjectAttribute result = attributeStore.getAttribute(invocationContext, identity, name);

      // Put fresh attributes in cache
      if (result != null)
      {
         cacheSupport.invalidateIdentityObjectAttriubtes(getCacheNS(invocationContext), identity);
         cacheSupport.putIdentityObjectAttributes(getCacheNS(invocationContext), identity, attributeStore.getAttributes(invocationContext, identity));
      }
      return result;

   }

   public void updateAttributes(IdentityStoreInvocationContext invocationCtx,
                                IdentityObject identity,
                                IdentityObjectAttribute[] attributes) throws IdentityException
   {
      attributeStore.updateAttributes(invocationCtx, identity, attributes);

      cacheSupport.invalidateIdentityObjectAttriubtes(getCacheNS(invocationCtx), identity);
      cacheSupport.putIdentityObjectAttributes(getCacheNS(invocationCtx), identity, attributeStore.getAttributes(invocationCtx, identity));
   }

   public void addAttributes(IdentityStoreInvocationContext invocationCtx,
                             IdentityObject identity,
                             IdentityObjectAttribute[] attributes) throws IdentityException
   {
      attributeStore.addAttributes(invocationCtx, identity, attributes);

      cacheSupport.invalidateIdentityObjectAttriubtes(getCacheNS(invocationCtx), identity);
      cacheSupport.putIdentityObjectAttributes(getCacheNS(invocationCtx), identity, attributeStore.getAttributes(invocationCtx, identity));
   }

   public void removeAttributes(IdentityStoreInvocationContext invocationCtx,
                                IdentityObject identity,
                                String[] attributeNames) throws IdentityException
   {
      attributeStore.removeAttributes(invocationCtx, identity, attributeNames);

      cacheSupport.invalidateIdentityObjectAttriubtes(getCacheNS(invocationCtx), identity);
      cacheSupport.putIdentityObjectAttributes(getCacheNS(invocationCtx), identity, attributeStore.getAttributes(invocationCtx, identity));
   }

   //TODO: cache
   public IdentityObject findIdentityObjectByUniqueAttribute(IdentityStoreInvocationContext invocationCtx, IdentityObjectType identityObjectType, IdentityObjectAttribute attribute) throws IdentityException
   {
      return attributeStore.findIdentityObjectByUniqueAttribute(invocationCtx, identityObjectType, attribute);
   }


   protected Logger getLog()
   {
      return log;
   }

   @Override
   public String toString()
   {
      return "JBossCacheAttributeStoreWrapper (AttributeStore=" + attributeStore.getId() + ")";
   }

}
