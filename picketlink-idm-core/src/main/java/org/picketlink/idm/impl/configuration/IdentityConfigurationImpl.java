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

package org.picketlink.idm.impl.configuration;

import org.picketlink.idm.api.cfg.IdentityConfiguration;
import org.picketlink.idm.api.cfg.IdentityConfigurationRegistry;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.impl.helper.CopyOnWriteRegistry;
import org.picketlink.idm.impl.configuration.jaxb2.JAXB2IdentityConfiguration;
import org.picketlink.idm.impl.api.session.mapper.IdentityObjectTypeMapper;
import org.picketlink.idm.impl.api.session.mapper.DirectIdentityObjectTypeMapperImpl;
import org.picketlink.idm.impl.api.session.mapper.IdentityObjectTypeMapperImpl;
import org.picketlink.idm.impl.api.IdentitySessionFactoryImpl;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.common.exception.IdentityConfigurationException;
import org.picketlink.idm.spi.configuration.metadata.IdentityConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityStoreConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityRepositoryConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.RealmConfigurationMetaData;
import org.picketlink.idm.spi.configuration.IdentityConfigurationContextRegistry;
import org.picketlink.idm.spi.configuration.IdentityConfigurationContext;
import org.picketlink.idm.spi.configuration.IdentityStoreConfigurationContext;
import org.picketlink.idm.spi.configuration.IdentityRepositoryConfigurationContext;
import org.picketlink.idm.spi.store.IdentityStore;
import org.picketlink.idm.spi.store.AttributeStore;
import org.picketlink.idm.spi.repository.IdentityStoreRepository;
import org.picketlink.idm.cache.APICacheProvider;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class IdentityConfigurationImpl
   implements IdentityConfiguration, IdentityConfigurationRegistry, IdentityConfigurationContextRegistry, Serializable
{

   private static final Logger log = Logger.getLogger(IdentityConfigurationImpl.class.getName());

   private IdentityConfigurationMetaData configMD;

   private final CopyOnWriteRegistry registry;

   public IdentityConfigurationImpl()
   {
      registry = new CopyOnWriteRegistry();
   }

   public void register(Object object, String name) throws IdentityException
   {
      if (!registry.register(name, object))
      {
          throw new IdentityException("Cannot register object in IdentityContext with name: " + name);
      }
      if (log.isLoggable(Level.FINER)) log.finer("registering object: " + name + " ; " + object.getClass());
   }

   public void unregister(String name)
   {
      if (registry.unregister(name) == null)
      {
          log.info("Cannot unregister object from IdentityContext with name: " + name);
      }
      if (log.isLoggable(Level.FINER)) log.finer("unregistering object: " + name);
   }

   public Object getObject(String name) throws IdentityException
   {
      Object o = registry.getRegistration(name);
      if (o == null)
      {
         throw new IdentityException("No such mapping in IdentityContext: " + name);
      }
      return o;
   }

   public IdentityConfigurationRegistry getIdentityConfigurationRegistry()
   {
      return this;
   }

    public IdentityConfiguration configure(IdentityConfigurationMetaData configMD)
   {
      this.configMD = configMD;

      return this;
   }

   public IdentityConfiguration configure(File configFile) throws IdentityConfigurationException
   {
      IdentityConfigurationMetaData configMD = JAXB2IdentityConfiguration.createConfigurationMetaData(configFile);

      this.configMD = configMD;

      return this;

   }

   public IdentityConfiguration configure(String configResource) throws IdentityConfigurationException
   {
      IdentityConfigurationMetaData configMD = JAXB2IdentityConfiguration.createConfigurationMetaData(configResource);

      this.configMD = configMD;

      return this;

   }

   public IdentitySessionFactory buildIdentitySessionFactory() throws IdentityConfigurationException
   {
      Map<String, IdentitySessionConfigurationContext> realmMap = null;

      if (configMD == null)
      {
         throw new IdentityConfigurationException("Failed to build IdentitySessionFactory. Configuration not initialized");
      }

      try
      {
         realmMap = createRealmMap(configMD);
      }
      catch (Exception e)
      {
         throw new IdentityConfigurationException("Failed to build IdentitySessionFactory", e);
      }

      return new IdentitySessionFactoryImpl(configMD, realmMap);
   }

   private Map<String, IdentitySessionConfigurationContext> createRealmMap(IdentityConfigurationMetaData configMD) throws Exception
   {
      //TODO: some validation, sanity checks and error reporting


      IdentityConfigurationContext configurationContext = new IdentityConfigurationContextImpl(configMD, this);

      // IdentityStore

      Map<String, IdentityStore> bootstrappedIdentityStores = new HashMap<String, IdentityStore>();
      Map<String, AttributeStore> bootstrappedAttributeStores = new HashMap<String, AttributeStore>();

      for (IdentityStoreConfigurationMetaData metaData : configMD.getIdentityStores())
      {
         Class storeClass = null;
         try
         {
            storeClass = Class.forName(metaData.getClassName());
         }
         catch (ClassNotFoundException e)
         {
            throw new IdentityException("Cannot instantiate identity store:" + metaData.getClassName(), e);
         }
         Class partypes[] = new Class[1];
         partypes[0] = String.class;

         Constructor ct = storeClass.getConstructor(partypes);
         Object argList[] = new Object[1];
         argList[0] = metaData.getId();

         IdentityStore store = (IdentityStore)ct.newInstance(argList);

         IdentityStoreConfigurationContext storeConfigurationCtx =
            new IdentityStoreConfigurationContextImpl(configMD, this, metaData);

         store.bootstrap(storeConfigurationCtx);

         bootstrappedIdentityStores.put(store.getId(), store);
         bootstrappedAttributeStores.put(store.getId(), store);
      }

      // IdentityRepository

      Map<String, IdentityStoreRepository> bootstrappedRepositories = new HashMap<String, IdentityStoreRepository>();

      for (IdentityRepositoryConfigurationMetaData metaData : configMD.getRepositories())
      {
         Class repoClass = null;
         try
         {
            repoClass = Class.forName(metaData.getClassName());
         }
         catch (ClassNotFoundException e)
         {
            throw new IdentityException("Cannot instantiate identity store:" + metaData.getClassName(), e);
         }
         Class partypes[] = new Class[1];
         partypes[0] = String.class;

         Constructor ct = repoClass.getConstructor(partypes);
         Object argList[] = new Object[1];
         argList[0] = metaData.getId();

         IdentityStoreRepository repo = (IdentityStoreRepository)ct.newInstance(argList);

         IdentityRepositoryConfigurationContext repoConfigurationContext =
            new IdentityRepositoryConfigurationContextImpl(configMD, this, metaData);

         repo.bootstrap(repoConfigurationContext, bootstrappedIdentityStores, bootstrappedAttributeStores);

         bootstrappedRepositories.put(repo.getId(), repo);
      }

      // Realms

      Map<String, IdentitySessionConfigurationContext> sessionCtxMap = new HashMap<String, IdentitySessionConfigurationContext>();

      for (RealmConfigurationMetaData metaData : configMD.getRealms())
      {
         String realmName = metaData.getId();

         IdentityStoreRepository repo = bootstrappedRepositories.get(metaData.getIdentityRepositoryIdRef());

         IdentityObjectTypeMapper mapper = null;

         if (metaData.getGroupTypeMappings() == null || metaData.getGroupTypeMappings().isEmpty())
         {
            // use direct type mapper
            mapper = new DirectIdentityObjectTypeMapperImpl(metaData.getIdentityMapping());
         }
         else
         {
            mapper = new IdentityObjectTypeMapperImpl(metaData.getGroupTypeMappings(), metaData.getIdentityMapping());
         }

         // Cache

         Map<String, String> cacheProps = new HashMap<String, String>();
         String cacheClassName = null;
         String cacheRegistryName = null;

         // Parse all 'cache.' prefixed options
         for (String key : metaData.getOptions().keySet())
         {
            if (key.startsWith("cache."))
            {
               if (metaData.getOptions().get(key).size() > 0)
               {
                  cacheProps.put(key, metaData.getOptions().get(key).get(0));
               }
               if (key.equals("cache.providerClass") && metaData.getOptions().get(key).size() > 0)
               {
                  cacheClassName = metaData.getOptions().get(key).get(0);
               }

               if (key.equals("cache.providerRegistryName") && metaData.getOptions().get(key).size() > 0)
               {
                  cacheRegistryName = metaData.getOptions().get(key).get(0);
               }
            }
         }

         APICacheProvider provider = null;

         if (cacheRegistryName != null)
         {
            try
            {
               provider = (APICacheProvider)configurationContext.getConfigurationRegistry().getObject(cacheRegistryName);
            }
            catch (Exception e)
            {
               throw new IdentityException("Cannot find APICacheProvider in ConfigurationRegistry using provided name:" + cacheRegistryName, e);
            }

         }

         // Instantiate provider
         if (cacheClassName != null)
         {
            Class repoClass = null;
            try
            {
               repoClass = Class.forName(cacheClassName);
            }
            catch (ClassNotFoundException e)
            {
               throw new IdentityException("Cannot instantiate APICacheProvider:" + cacheClassName, e);
            }

            Constructor ct = repoClass.getConstructor();

            provider = (APICacheProvider)ct.newInstance();

            provider.initialize(cacheProps, (IdentityConfigurationRegistry)configurationContext.getConfigurationRegistry());
         }


         //IdentitySession session = new IdentitySessionImpl(realmName, repo, mapper);
         IdentitySessionConfigurationContext sessionConfigCtx =
            new IdentitySessionConfigurationContext(realmName, configMD, repo, mapper, provider, configurationContext, metaData.getOptions());

         sessionCtxMap.put(realmName, sessionConfigCtx);
      }

      return sessionCtxMap;
   }

}
