package org.picketlink.idm.api.event;

import org.picketlink.idm.api.*;/*
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
 * EventListener related to actions performed on IdentityType
 */
public interface IdentityTypeEventListener extends EventListener
{

   /**
    * Method invoked before given IdentityType is created
    * @param session
    * @param identityType
    */
   void preCreate(IdentitySession session, IdentityType identityType);

   /**
    * Method invoked after given IdentityType is created
    * @param session
    * @param identityType
    */
   void postCreate(IdentitySession session, IdentityType identityType);

   /**
    * Method invoked before given IdentityType is removed
    * @param session
    * @param identityType
    */
   void preRemove(IdentitySession session, IdentityType identityType);

   /**
    * Method invoked after given IdentityType is removed
    * @param session
    * @param identityType
    */
   void postRemove(IdentitySession session, IdentityType identityType);

   /**
    * Method invoked before attributes are added for a given IdentityType
    * @param session
    * @param identityType
    * @param attributes
    */
   void preAttributesAdd(IdentitySession session, IdentityType identityType,  Attribute[] attributes);

   /**
    * Method invoked after attributes are added for a given IdentityType
    * @param session
    * @param identityType
    * @param attributes
    */
   void postAttributesAdd(IdentitySession session, IdentityType identityType,  Attribute[] attributes);

   /**
    * Method invoked before attributes are removed for a given IdentityType
    * @param session
    * @param identityType
    * @param attributes
    */
   void preAttributesRemove(IdentitySession session, IdentityType identityType,  String[] attributes);

   /**
    * Method invoked after attributes are removed for a given IdentityType
    * @param session
    * @param identityType
    * @param attributes
    */
   void postAttributesRemove(IdentitySession session, IdentityType identityType,  String[] attributes);

   /**
    * Method invoked before attributes are updated for a given IdentityType
    * @param session
    * @param identityType
    * @param attributes
    */
   void preAttributesUpdate(IdentitySession session, IdentityType identityType,  Attribute[] attributes);

   /**
    * Method invoked after attributes are updated for a given IdentityType
    * @param session
    * @param identityType
    * @param attributes
    */
   void postAttributesUpdate(IdentitySession session, IdentityType identityType,  Attribute[] attributes);

   /**
    * Method invoked before credential is updated for a given User
    * @param session
    * @param user
    * @param credential
    */
   void preCredentialUpdate(IdentitySession session, User user, Credential credential);

   /**
    * Method invoked after credential is updated for a given User
    * @param session
    * @param user
    * @param credential
    */
   void postCredentialUpdate(IdentitySession session, User user, Credential credential);
}
