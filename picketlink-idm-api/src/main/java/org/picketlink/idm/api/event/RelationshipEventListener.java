package org.picketlink.idm.api.event;

import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.IdentitySession;/*
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
 * EventListener related to relationships operations
 */
public interface RelationshipEventListener extends EventListener
{

   /**
    * Method invoked before association is created between Group and User
    * @param session
    * @param parent
    * @param member
    */
   void preUserAssociationCreate(IdentitySession session, Group parent, User member);

   /**
    * Method invoked before association is created between Groups
    *
    * @param session
    * @param parent
    * @param member
    */
   void preGroupAssociationCreate(IdentitySession session, Group parent, Group member);

   /**
    * Method invoked after association is created between Group and User
    *
    * @param session
    * @param parent
    * @param member
    */
   void postUserAssociationCreate(IdentitySession session, Group parent, User member);

   /**
    * Method invoked after association is created between Groups
    *
    * @param session
    * @param parent
    * @param member
    */
   void postGroupAssociationCreate(IdentitySession session, Group parent, Group member);

   /**
    * Method invoked before association is removed between Group and User
    * @param session
    * @param parent
    * @param member
    */
   void preUserAssociationRemove(IdentitySession session, Group parent, User member);

   /**
    * Method invoked before association is removed between Groups
    * @param session
    * @param parent
    * @param member
    */
   void preGroupAssociationRemove(IdentitySession session, Group parent, Group member);

   /**
    * Method invoked after association is removed between Group and User
    *
    * @param session
    * @param parent
    * @param member
    */
   void postUserAssociationRemove(IdentitySession session, Group parent, User member);

   /**
    * Method invoked after association is removed between Groups
    *
    * @param session
    * @param parent
    * @param member
    */
   void postGroupAssociationRemove(IdentitySession session, Group parent, Group member);

}
