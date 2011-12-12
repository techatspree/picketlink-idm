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

package org.picketlink.idm.impl.api.session.context;

import org.picketlink.idm.spi.repository.IdentityStoreRepository;
import org.picketlink.idm.spi.store.IdentityStoreInvocationContext;
import org.picketlink.idm.impl.api.session.context.IdentitySessionContext;
import org.picketlink.idm.impl.api.session.mapper.IdentityObjectTypeMapper;
import org.picketlink.idm.impl.api.session.context.IdentityStoreInvocationContextResolver;

import java.io.Serializable;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class IdentitySessionContextImpl implements IdentitySessionContext, Serializable
{
   private final IdentityStoreRepository identityStoreRepository;

   private final IdentityObjectTypeMapper identityObjectTypeMapper;

   private final IdentityStoreInvocationContextResolver contextResolver;  

   private static final long serialVersionUID = 6968037075942357128L;


   public IdentitySessionContextImpl(IdentityStoreRepository identityStoreRepository, IdentityObjectTypeMapper identityObjectTypeMapper, IdentityStoreInvocationContextResolver contextResolver)
   {
      this.identityStoreRepository = identityStoreRepository;
      this.identityObjectTypeMapper = identityObjectTypeMapper;
      this.contextResolver = contextResolver;
   }

   public IdentityStoreRepository getIdentityStoreRepository()
   {
      return identityStoreRepository;
   }

   public IdentityObjectTypeMapper getIdentityObjectTypeMapper()
   {
      return identityObjectTypeMapper;
   }

   public IdentityStoreInvocationContext resolveStoreInvocationContext()
   {
      return contextResolver.resolveInvocationContext();
   }
}
