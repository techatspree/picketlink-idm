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

import org.picketlink.idm.spi.configuration.metadata.RealmConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityConfigurationMetaData;
import org.picketlink.idm.spi.configuration.IdentityConfigurationContextRegistry;
import org.picketlink.idm.spi.configuration.RealmConfigurationContext;

import java.io.Serializable;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class RealmConfigurationContextImpl
   extends IdentityConfigurationContextImpl implements RealmConfigurationContext, Serializable
{

   private final RealmConfigurationMetaData realmMD;

   public RealmConfigurationContextImpl(IdentityConfigurationMetaData configurationMD, IdentityConfigurationContextRegistry configurationRegistry, RealmConfigurationMetaData realmMD)
   {
      super(configurationMD, configurationRegistry);
      this.realmMD = realmMD;
   }

   public RealmConfigurationMetaData getRealmConfigurationMetaData()
   {
      return realmMD;
   }
}
