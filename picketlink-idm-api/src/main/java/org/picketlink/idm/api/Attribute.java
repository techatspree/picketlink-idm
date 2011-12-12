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

package org.picketlink.idm.api;

import java.util.Collection;

/**
 * Abstract representation of attribute related to IdentityType object. Can have many values
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface Attribute
{
   /**
    * @return attribute name
    */
   String getName();

   /**
    * @return attribute value. If attribute has many values it may be any one of them.
    */
   Object getValue();

   /**
    * Add attribute value. It will be appended.
    * @param value
    */
   void addValue(Object value);

   /**
    * @return attribute values
    */
   Collection<?> getValues();

   /**
    * @return number of attribute values
    */
   int getSize();
}
