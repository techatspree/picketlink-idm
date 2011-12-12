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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.picketlink.idm.spi.repository.IdentityStoreRepository;
import org.picketlink.idm.spi.configuration.metadata.IdentityConfigurationMetaData;
import org.picketlink.idm.spi.configuration.IdentityConfigurationContext;
import org.picketlink.idm.impl.api.session.mapper.IdentityObjectTypeMapper;
import org.picketlink.idm.cache.APICacheProvider;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class IdentitySessionConfigurationContext implements Serializable
{

   private static final long serialVersionUID = 3263619777028197717L;
   
   private final String realmName;
   private final IdentityConfigurationContext identityConfigurationContext;
   private final IdentityConfigurationMetaData identityConfigurationMetaData;
   private final IdentityStoreRepository repository;
   private final IdentityObjectTypeMapper typeMapper;
   private final APICacheProvider apiCacheProvider;
   private final Map<String, List<String>> realmOptions;

   public IdentitySessionConfigurationContext(String realmName,
                                              IdentityConfigurationMetaData identityConfigurationMetaData,
                                              IdentityStoreRepository repository,
                                              IdentityObjectTypeMapper typeMapper,
                                              APICacheProvider apiCacheProvider,
                                              IdentityConfigurationContext identityConfigurationContext,
                                              Map<String, List<String>> realmOptions)
   {
      this.realmName = realmName;
      this.identityConfigurationMetaData = identityConfigurationMetaData;
      this.repository = repository;
      this.typeMapper = typeMapper;
      this.apiCacheProvider = apiCacheProvider;
      this.identityConfigurationContext = identityConfigurationContext;
      this.realmOptions = realmOptions;
   }

   public String getRealmName()
   {
      return realmName;
   }

   public IdentityConfigurationMetaData getIdentityConfigurationMetaData()
   {
      return identityConfigurationMetaData;
   }

   public IdentityStoreRepository getRepository()
   {
      return repository;
   }

   public IdentityObjectTypeMapper getTypeMapper()
   {
      return typeMapper;
   }

   public APICacheProvider getApiCacheProvider()
   {
      return apiCacheProvider;
   }

   public IdentityConfigurationContext getIdentityConfigurationContext()
   {
      return identityConfigurationContext;
   }

   public Map<String, List<String>> getRealmOptions()
   {
      return realmOptions;
   }
}
