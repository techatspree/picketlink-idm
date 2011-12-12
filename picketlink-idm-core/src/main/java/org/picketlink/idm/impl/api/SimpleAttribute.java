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

package org.picketlink.idm.impl.api;

import org.picketlink.idm.spi.model.IdentityObjectAttribute;
import org.picketlink.idm.api.Attribute;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class SimpleAttribute implements IdentityObjectAttribute, Attribute, Serializable
{

   private final String name;

   private final List<Object> values = new LinkedList<Object>();

   public SimpleAttribute(String name)
   {
      this.name = name;
   }

   public SimpleAttribute(String name, Object[] values)
   {
      this.name = name;
      for (Object value : values)
      {
         this.values.add(value);
      }
   }

   public SimpleAttribute(String name, Object value)
   {
      this.name = name;
      if (value != null)
      {
         this.values.add(value);
      }
      
   }

   public SimpleAttribute(Attribute attribute)
   {
      this.name = attribute.getName();
      this.values.addAll(attribute.getValues());
   }

   public SimpleAttribute(IdentityObjectAttribute attribute)
   {
      this.name = attribute.getName();
      this.values.addAll(attribute.getValues());
   }

   public String getName()
   {
      return name;
   }

   public Collection getValues()
   {
      return Collections.unmodifiableList(values);
   }

   public Object getValue()
   {
      if (values.size() > 0)
      {
         return values.iterator().next();
      }
      else
      {
         return null;
      }
   }

   public int getSize()
   {
      return values.size();
   }

   public void addValue(Object value)
   {
      values.add(value);
   }
}
