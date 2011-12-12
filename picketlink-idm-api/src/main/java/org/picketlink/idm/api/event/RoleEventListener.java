package org.picketlink.idm.api.event;

import org.picketlink.idm.api.Role;
import org.picketlink.idm.api.IdentitySession;

import java.util.Map;
import java.util.Collection;/*
* JBoss, a division of Red Hat
* Copyright 2009, Red Hat Middleware, LLC, and individual contributors as indicated
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

/**
 *  EventListener related to actions performed on a Role
 */
public interface RoleEventListener extends EventListener
{
   
    /**
    * Method invoked before Role is created
     * @param session
    * @param role
    */
   void preCreate(IdentitySession session, Role role);

   /**
    * Method invoked after Role is created
    * @param session
    * @param role
    */
   void postCreate(IdentitySession session, Role role);

   /**
    * Method invoked before Role is created
    * @param session
    * @param role
    */
   void preRemove(IdentitySession session, Role role);

   /**
    * Method invoked after Role is created
    * @param session
    * @param role
    */
   void postRemove(IdentitySession session, Role role);

   /**
    * Method invoked before properties are set
    * @param session
    * @param role
    * @param properties
    */
   void prePropertiesSet(IdentitySession session, Role role, Map<String, String> properties);

   /**
    * Method invoked after properties are set
    * @param session
    * @param role
    * @param properties
    */
   void postPropertiesSet(IdentitySession session, Role role, Map<String, String> properties);

   /**
    * Method invoked before properties are removed
    * @param session
    * @param role
    * @param names
    */
   void prePropertiesRemove(IdentitySession session, Role role, Collection<String> names);

   /**
    * Method invoked after properties are removed
    * @param session
    * @param role
    * @param names
    */
   void postPropertiesRemove(IdentitySession session, Role role, Collection<String> names);
   
}
