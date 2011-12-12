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

package org.picketlink.idm.impl.types;

import org.picketlink.idm.spi.model.IdentityObjectRelationship;
import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectRelationshipType;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class SimpleIdentityObjectRelationship implements IdentityObjectRelationship
{
   private IdentityObject fromIdentityObject;

   private IdentityObject toIdentityObject;

   private String name;

   private IdentityObjectRelationshipType type;

   public SimpleIdentityObjectRelationship(IdentityObject fromIdentityObject, IdentityObject toIdentityObject, String name, IdentityObjectRelationshipType type)
   {
      this.fromIdentityObject = fromIdentityObject;
      this.toIdentityObject = toIdentityObject;
      this.name = name;
      this.type = type;
   }

   public IdentityObject getFromIdentityObject()
   {
      return fromIdentityObject;
   }

   public IdentityObject getToIdentityObject()
   {
      return toIdentityObject;
   }

   public String getName()
   {
      return name;
   }

   public IdentityObjectRelationshipType getType()
   {
      return type;
   }
}
