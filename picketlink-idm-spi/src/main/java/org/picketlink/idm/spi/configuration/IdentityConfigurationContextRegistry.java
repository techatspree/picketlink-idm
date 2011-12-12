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

package org.picketlink.idm.spi.configuration;

import org.picketlink.idm.common.exception.IdentityException;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface IdentityConfigurationContextRegistry
{

   /**
    * Retister object in context
    * @param object representing object
    * @param name to map object
    * @throws org.picketlink.idm.common.exception.IdentityException thrown if such object is already registered or operation fail.
    */
   public void register(Object object, String name) throws IdentityException;

   /**
    * Remove object from context
    * @param name of identity object
    */
   public void unregister(String name);

   /**
    * Retrieve registered object
    * @param name
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException thrown if no such object exists in context
    */
   public Object getObject(String name) throws IdentityException;
}
