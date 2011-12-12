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

package org.picketlink.idm.impl.model.ldap;

import org.picketlink.idm.spi.model.IdentityObjectRelationship;
import org.picketlink.idm.spi.model.IdentityObjectRelationshipType;
import org.picketlink.idm.spi.model.IdentityObject;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class LDAPIdentityObjectRelationshipImpl implements IdentityObjectRelationship
{

   private final String name;

   private final IdentityObject from;

   private final IdentityObject to;

   //TODO: 
   //private final IdentityObjectType type;

   public LDAPIdentityObjectRelationshipImpl(String name, IdentityObject from, IdentityObject to)
   {
      this.name = name;
      this.from = from;
      this.to = to;
   }

   public String getName()
   {
      return name;
   }

   public IdentityObjectRelationshipType getType()
   {
      //TODO: for now just hardcoded value
      return new IdentityObjectRelationshipType()
      {
         public String getName()
         {
            return "JBOSS_IDENTITY_MEMBERSHIP";
         }
      };
   }

   public IdentityObject getFromIdentityObject()
   {
      return from;
   }

   public IdentityObject getToIdentityObject()
   {
      return to;
   }
}
