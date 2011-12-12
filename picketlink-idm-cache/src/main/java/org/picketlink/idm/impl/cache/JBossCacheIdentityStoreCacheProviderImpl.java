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

import org.jboss.cache.*;

import org.picketlink.idm.spi.configuration.IdentityConfigurationContext;
import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectAttribute;
import org.picketlink.idm.spi.model.IdentityObjectRelationship;
import org.picketlink.idm.spi.cache.IdentityStoreCacheProvider;
import org.picketlink.idm.spi.cache.IdentityObjectSearch;
import org.picketlink.idm.spi.cache.IdentityObjectRelationshipSearch;
import org.picketlink.idm.spi.cache.IdentityObjectRelationshipNameSearch;
import org.picketlink.idm.spi.configuration.IdentityRepositoryConfigurationContext;
import org.picketlink.idm.impl.types.SimpleIdentityObject;
import org.picketlink.idm.impl.types.SimpleIdentityObjectType;
import org.picketlink.idm.impl.types.SimpleIdentityObjectRelationship;
import org.picketlink.idm.impl.types.SimpleIdentityObjectRelationshipType;
import org.picketlink.idm.impl.api.SimpleAttribute;

import java.io.InputStream;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.*;

/**
 * Helper class providing caching support for IdentityStore.
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class JBossCacheIdentityStoreCacheProviderImpl implements IdentityStoreCacheProvider
{
   private static Logger log = Logger.getLogger(JBossCacheIdentityStoreCacheProviderImpl.class.getName());


   private Cache cache;

   public static final String CONFIG_FILE_OPTION = "cache.configFile";

   public static final String CONFIG_CACHE_REGISTRY_OPTION = "cache.cacheRegistryName";

   public static final String NODE_OBJECT_KEY = "object";

   public static final String NODE_SEARCH_KEY = "search";

   public static final String NODE_SEARCH_UNIQUE_KEY = "query_unique";

   public static final String NODE_MAIN_ROOT = "IDM_ROOT";

   public static final String NODE_COMMON_ROOT = "COMMON_ROOT";

   public static final String NODE_IO_COUNT = "NODE_IO_COUNT";

   public static final String NODE_IO_ATTRIBUTES = "NODE_IO_ATTRIBUTES";

   public static final String NODE_OBJECTS = "NODE_OBJECTS";

   public static final String NODE_REL_PROPS = "NODE_REL_PROPS";

   public static final String NODE_REL_NAME_PROPS = "NODE_REL_NAME_PROPS";

   public static final String NODE_IO_SEARCH = "NODE_IO_SEARCH";

   public static final String NODE_IO_REL_SEARCH = "NODE_IO_REL_SEARCH";

   public static final String NODE_IO_REL_NAME_SEARCH = "NODE_IO_REL_NAME_SEARCH";

   public static final String NULL_NS_NODE = "PL_COMMON_NS";

   public static final String MAIN_ROOT = "NODE_MAIN_ROOT";

   private Fqn getRootNode()
   {
      return Fqn.fromString("/" + MAIN_ROOT);
   }

   private Fqn getNamespacedFqn(String ns)
   {
      String namespace = ns != null ? ns : NULL_NS_NODE;
      namespace = namespace.replaceAll("/", "_");
      return Fqn.fromString(getRootNode() + "/" + ns);
   }

   private Fqn getFqn(String ns, String node, Object o)
   {
      return Fqn.fromString(getNamespacedFqn(ns) + "/" + node + "/" + o);
   }

   private Fqn getFqn(String ns, String node, int hash)
   {
      return Fqn.fromString(getNamespacedFqn(ns) + "/" + node + "/" + hash);
   }

   private Fqn getFqn(String ns, String node)
   {
      return Fqn.fromString(getNamespacedFqn(ns) + "/" + node);
   }

   public void initialize(Map<String, String> properties, IdentityConfigurationContext configurationContext)
   {
      CacheFactory factory = new DefaultCacheFactory();

      String registryName = properties.get(CONFIG_CACHE_REGISTRY_OPTION);

      // Get cache from registry
      if (registryName != null)
      {
         try
         {
            this.cache = (Cache)configurationContext.getConfigurationRegistry().getObject(registryName);
         }
         catch (Exception e)
         {
            throw new IllegalArgumentException("Cannot find JBoss Cache 'Cache' object in configuration registry with provided" +
               "name: " + registryName);
         }

         return;
      }

      String config = properties.get(CONFIG_FILE_OPTION);

      if (config == null)
      {
         throw new IllegalArgumentException("Cannot find '" + CONFIG_FILE_OPTION + "' in passed properties. Failed to initialize" +
            "cache provider.");
      }

      this.cache = factory.createCache(config);

      this.cache.create();
      this.cache.start();

   }

   public void initialize(InputStream jbossCacheConfiguration)
   {
      CacheFactory factory = new DefaultCacheFactory();

      if (jbossCacheConfiguration == null)
      {
         throw new IllegalArgumentException("JBoss Cache configuration InputStream is null");
      }

      this.cache = factory.createCache(jbossCacheConfiguration);

      this.cache.create();
      this.cache.start();

   }

    public void initialize(Cache cache)
   {
      this.cache = cache;

      CacheStatus status = cache.getCacheStatus();

      if (status.createAllowed())
      {
         this.cache.create();
      }
      if (status.startAllowed())
      {
         this.cache.start();
      }

   }

   Cache getCache()
   {
      return cache;
   }


   public void invalidate(String ns)
   {
      cache.getRoot().removeChild(getNamespacedFqn(ns));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating namespace:" + ns);
      }
   }

   public void invalidateAll()
   {
      boolean success = cache.getRoot().removeChild(getRootNode());

      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating whole cache - success=" + success);
      }
   }

   public String getNamespace(String storeId)
   {
      if (storeId == null)
      {
         return NODE_COMMON_ROOT;
      }
      return storeId;
   }

   public String getNamespace(String storeId, String sessionId)
   {
      if (sessionId == null)
      {
         return getNamespace(storeId);
      }
      return storeId + "/" + sessionId;
   }

   public String getNamespace(String storeId, String sessionId, String realmId)
   {
      if (realmId == null)
      {
         return getNamespace(sessionId);
      }
      return storeId + "/" + sessionId + "/" + realmId;
   }

   public void putIdentityObjectCount(String ns, String type, int count)
   {
      Fqn nodeFqn = getFqn(ns, NODE_IO_COUNT, type);

      Node ioNode = getCache().getRoot().addChild(nodeFqn);

      ioNode.put(NODE_OBJECT_KEY, count);

      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "IdentityObject count stored in cache: " + count + "; type=" + type
          + ";namespace=" + ns);
      }
   }

   public int getIdentityObjectCount(String ns, String type)
   {
      Fqn nodeFqn = getFqn(ns, NODE_IO_COUNT, type);

      Node node = getCache().getRoot().getChild(nodeFqn);

      if (node != null)
      {
         int count = -1;
         Integer i = (Integer)node.get(NODE_OBJECT_KEY);
         if (i != null)
         {
            count = i;
         }

         if (log.isLoggable(Level.FINER) && count != -1)
         {
            log.finer(this.toString() + "IdentityObject count found in cache: " + count + "; type=" + type
             + ";namespace=" + ns);
         }

         return count;
      }

      return -1;
   }

   public void invalidateIdentityObjectCount(String ns, String type)
   {
      getCache().getRoot().removeChild(Fqn.fromString(getNamespacedFqn(ns) + "/" + NODE_IO_COUNT + "/" + type));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating IdentityObject count. Namespace:" + ns + "; type=" + type
          + ";namespace=" + ns);
      }
   }

   public void putIdentityObjectSearch(String ns, IdentityObjectSearch search, Collection<IdentityObject> results)
   {
      Fqn nodeFqn = getFqn(ns, NODE_IO_SEARCH, search.hashCode());

      Node ioNode = getCache().getRoot().addChild(nodeFqn);

      ioNode.put(NODE_OBJECT_KEY, safeCopyIO(results));

      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "IdentityObject search stored in cache: results.size()=" + results.size()
          + ";namespace=" + ns);
      }
   }

   public Collection<IdentityObject> getIdentityObjectSearch(String ns, IdentityObjectSearch search)
   {
      Fqn nodeFqn = getFqn(ns, NODE_IO_SEARCH, search.hashCode());

      Node node = getCache().getRoot().getChild(nodeFqn);

      if (node != null)
      {
         Collection<IdentityObject> results = (Collection<IdentityObject>)node.get(NODE_OBJECT_KEY);

         if (log.isLoggable(Level.FINER) && results != null)
         {
            log.finer(this.toString() + "IdentityObject search found in cache: results.size()=" + results.size()
             + ";namespace=" + ns);
         }

         return results;
      }

      return null;
   }

   public void invalidateIdentityObjectSearches(String ns)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_IO_SEARCH));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating IdentityObject searches. Namespace:" + ns);
      }
   }

   public void putIdentityObjectRelationshipSearch(String ns, IdentityObjectRelationshipSearch search, Set<IdentityObjectRelationship> results)
   {
      Fqn nodeFqn = getFqn(ns, NODE_IO_REL_SEARCH, search.hashCode());

      Node ioNode = getCache().getRoot().addChild(nodeFqn);

      ioNode.put(NODE_OBJECT_KEY, safeCopyIOR(results));

      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "IdentityObjectRelationship search stored in cache: results.size()=" + results.size()
          + ";namespace=" + ns);
      }
   }

   public Set<IdentityObjectRelationship> getIdentityObjectRelationshipSearch(String ns, IdentityObjectRelationshipSearch search)
   {
      Fqn nodeFqn = getFqn(ns, NODE_IO_REL_SEARCH, search.hashCode());

      Node node = getCache().getRoot().getChild(nodeFqn);

      if (node != null)
      {
         Set<IdentityObjectRelationship> results = (Set<IdentityObjectRelationship>)node.get(NODE_OBJECT_KEY);

         if (log.isLoggable(Level.FINER) && results != null)
         {
            log.finer(this.toString() + "IdentityObjectRelationship search found in cache: results.size()=" + results.size()
             + ";namespace=" + ns);
         }

         return results;
      }

      return null;
   }

   public void invalidateIdentityObjectRelationshipSearches(String ns)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_IO_REL_SEARCH));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating IdentityObjectRelationship searches. Namespace:" + ns);
      }
   }

   public void putIdentityObjectRelationshipNameSearch(String ns, IdentityObjectRelationshipNameSearch search, Set<String> results)
   {
     Fqn nodeFqn = getFqn(ns, NODE_IO_REL_NAME_SEARCH, search.hashCode());

      Node ioNode = getCache().getRoot().addChild(nodeFqn);

      ioNode.put(NODE_OBJECT_KEY, results);

      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "IdentityObjectRelationshipName search stored in cache: results.size()=" + results.size()
          + ";namespace=" + ns);
      }
   }

   public Set<String> getIdentityObjectRelationshipNameSearch(String ns, IdentityObjectRelationshipNameSearch search)
   {
      Fqn nodeFqn = getFqn(ns, NODE_IO_REL_NAME_SEARCH, search.hashCode());

      Node node = getCache().getRoot().getChild(nodeFqn);

      if (node != null)
      {
         Set<String> results = (Set<String>)node.get(NODE_OBJECT_KEY);

         if (log.isLoggable(Level.FINER) && results != null)
         {
            log.finer(this.toString() + "IdentityObjectRelationshipName search found in cache: results.size()=" + results.size()
             + ";namespace=" + ns);
         }

         return results;
      }

      return null;
   }

   public void invalidateIdentityObjectRelationshipNameSearches(String ns)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_IO_REL_NAME_SEARCH));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating IdentityObjectRelationshipName searches. Namespace:" + ns);
      }
   }

   public void putProperties(String ns, IdentityObjectRelationship relationship, Map<String, String> properties)
   {
      Fqn nodeFqn = getFqn(ns, NODE_REL_PROPS, decode(relationship));

      Node ioNode = getCache().getRoot().addChild(nodeFqn);

      ioNode.put(NODE_OBJECT_KEY, properties);

      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "IdentityObjectRelationship properties stored in cache: relationship="
            + relationship + "; properties.size()=" + properties.size() + ";namespace=" + ns);
      }
   }

   private String decode(IdentityObjectRelationship r)
   {
      return r.getFromIdentityObject().getIdentityType().getName() +
         r.getFromIdentityObject().getName() +
         r.getToIdentityObject().getIdentityType().getName() +
         r.getToIdentityObject().getName() +
         r.getType().getName();
   }

   public Map<String, String> getProperties(String ns, IdentityObjectRelationship relationship)
   {
      Fqn nodeFqn = getFqn(ns, NODE_REL_PROPS, decode(relationship));

      Node node = getCache().getRoot().getChild(nodeFqn);

      if (node != null)
      {
         Map<String, String> props = (Map<String, String>)node.get(NODE_OBJECT_KEY);

         if (log.isLoggable(Level.FINER) && props != null)
         {
            log.finer(this.toString() + "IdentityObjectRelationship properties found in cache: properties.size()=" + props.size() +
         "; relationship=" + relationship + ";namespace=" + ns);
         }

         return props;
      }

      return null;
   }

   public void invalidateRelationshipProperties(String ns, IdentityObjectRelationship relationship)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_REL_PROPS, decode(relationship)));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating IdentityObjectRelationship properties. Namespace:" + ns
            + "; relationship=" + relationship + ";namespace=" + ns);
      }
   }

   public void invalidateRelationshipProperties(String ns)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_REL_PROPS));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating IdentityObjectRelationship properties. Namespace:" + ns);
      }
   }

   public void putProperties(String ns, String name, Map<String, String> properties)
   {
      Fqn nodeFqn = getFqn(ns, NODE_REL_NAME_PROPS, name);

      Node ioNode = getCache().getRoot().addChild(nodeFqn);

      ioNode.put(NODE_OBJECT_KEY, properties);

      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "IdentityObjectRelationshipName properties stored in cache: name="
            + name + "; properties.size()=" + properties.size() + ";namespace=" + ns);
      }
   }

   public Map<String, String> getProperties(String ns, String name)
   {
      Fqn nodeFqn = getFqn(ns, NODE_REL_NAME_PROPS, name);

      Node node = getCache().getRoot().getChild(nodeFqn);

      if (node != null)
      {
         Map<String, String> props = (Map<String, String>)node.get(NODE_OBJECT_KEY);

         if (log.isLoggable(Level.FINER) && props != null)
         {
            log.finer(this.toString() + "IdentityObjectRelationshipName properties found in cache: properties.size()=" + props.size() +
         "; name=" + name + ";namespace=" + ns);
         }

         return props;
      }

      return null;
   }

   public void invalidateRelationshipNameProperties(String ns, String relationship)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_REL_NAME_PROPS, relationship));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating IdentityObjectRelationshipName properties." +
            " Namespace:" + ns + "; name=" + relationship);
      }
   }

   public void invalidateRelationshipNameProperties(String ns)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_REL_NAME_PROPS));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating IdentityObjectRelationshipName properties. " +
            "Namespace:" + ns);
      }

   }

   public void putIdentityObjectAttributes(String ns, IdentityObject io, Map<String, IdentityObjectAttribute> attributes)
   {
      Fqn nodeFqn = getFqn(ns, NODE_IO_ATTRIBUTES, io.getIdentityType().getName() + io.getName());

      Node ioNode = getCache().getRoot().addChild(nodeFqn);

      ioNode.put(NODE_OBJECT_KEY, safeCopyAttr(attributes));

      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "IdentityObject attributes stored in cache: io=" + io
            + "; attributes.size()=" + attributes.size() + ";namespace=" + ns);
      }
   }

   public Map<String, IdentityObjectAttribute> getIdentityObjectAttributes(String ns, IdentityObject io)
   {
      Fqn nodeFqn = getFqn(ns, NODE_IO_ATTRIBUTES, io.getIdentityType().getName() + io.getName());

      Node node = getCache().getRoot().getChild(nodeFqn);

      if (node != null)
      {
         Map<String, IdentityObjectAttribute> props = (Map<String, IdentityObjectAttribute>)node.get(NODE_OBJECT_KEY);

         if (log.isLoggable(Level.FINER) && props != null)
         {
            log.finer(this.toString() + "IIdentityObject attributes found in cache: attributes.size()=" + props.size() +
         "; io=" + io + ";namespace=" + ns);
         }

         return props;
      }

      return null;
   }

   public void invalidateIdentityObjectAttriubtes(String ns, IdentityObject io)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_IO_ATTRIBUTES, io.getIdentityType().getName() + io.getName()));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating IdentityObject attributes. Namespace:" + ns + "; io=" + io);
      }
   }

   public void invalidateIdentityObjectAttriubtes(String ns)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_IO_ATTRIBUTES));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating IdentityObject attributes. Namespace:" + ns);
      }
   }

   public void putObject(String ns, int hash, Object value)
   {
      Fqn nodeFqn = getFqn(ns, NODE_OBJECTS, hash);

      Node ioNode = getCache().getRoot().addChild(nodeFqn);

      ioNode.put(NODE_OBJECT_KEY, value);

      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Object stored in cache: hash=" + hash
            + "; value=" + value + ";namespace=" + ns);
      }
   }

   public Object getObject(String ns, int hash)
   {
      Fqn nodeFqn = getFqn(ns, NODE_OBJECTS, hash);

      Node node = getCache().getRoot().getChild(nodeFqn);

      if (node != null)
      {
         Object value = node.get(NODE_OBJECT_KEY);

         if (log.isLoggable(Level.FINER) && value != null)
         {
            log.finer(this.toString() + "Object found in cache: hash" + hash +
          ";namespace=" + ns);
         }

         return value;
      }

      return null;
   }

   public void invalidateObject(String ns, int hash)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_OBJECTS, hash));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating object. Namespace:" + ns + "; hash=" + hash);
      }
   }

   public void invalidateObjects(String ns)
   {
      getCache().getRoot().removeChild(getFqn(ns, NODE_OBJECTS));
      if (log.isLoggable(Level.FINER))
      {
         log.finer(this.toString() + "Invalidating objects. Namespace:" + ns);
      }
   }

   private List<IdentityObject> safeCopyIO(Collection<IdentityObject> res)
   {
      List<IdentityObject> nr = new LinkedList<IdentityObject>();

      for (IdentityObject io : res)
      {
         nr.add(new SimpleIdentityObject(io.getName(),
            new SimpleIdentityObjectType(io.getIdentityType().getName())));
      }

      return nr; 
   }

   private Set<IdentityObjectRelationship> safeCopyIOR(Set<IdentityObjectRelationship> res)
   {
      Set<IdentityObjectRelationship> nr = new HashSet<IdentityObjectRelationship>();

      for (IdentityObjectRelationship ior : res)
      {
         IdentityObject from = new SimpleIdentityObject(ior.getFromIdentityObject().getName(),
            new SimpleIdentityObjectType(ior.getFromIdentityObject().getIdentityType().getName()));
         IdentityObject to = new SimpleIdentityObject(ior.getToIdentityObject().getName(),
            new SimpleIdentityObjectType(ior.getToIdentityObject().getIdentityType().getName()));

         nr.add(new SimpleIdentityObjectRelationship(from, to, ior.getName(), new SimpleIdentityObjectRelationshipType(ior.getType().getName())));
      }

      return nr;
   }

   private Map<String, IdentityObjectAttribute> safeCopyAttr(Map<String, IdentityObjectAttribute> res)
   {
      Map<String, IdentityObjectAttribute> nr = new HashMap<String, IdentityObjectAttribute>();

      for (IdentityObjectAttribute attr : res.values())
      {
         nr.put(attr.getName(), new SimpleAttribute(attr));
      }

      return nr;
   }


}
