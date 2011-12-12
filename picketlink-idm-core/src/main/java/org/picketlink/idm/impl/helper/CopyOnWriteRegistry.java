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

package org.picketlink.idm.impl.helper;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;
import java.util.Collection;
import java.io.Serializable;

/**
 * Implementation of a registry that implements copy on write semantics.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class CopyOnWriteRegistry implements Serializable
{

   /** . */
   private volatile Map content;

   public CopyOnWriteRegistry()
   {
      content = new HashMap();
   }

   /**
    * Register an object.
    *
    * @param key    the registration key
    * @param object the registered object
    * @return true if the registration was made
    * @throws IllegalArgumentException if the one argument is null
    */
   public synchronized boolean register(Object key, Object object) throws IllegalArgumentException
   {
      if (key == null)
      {
         throw new IllegalArgumentException("No null key accepted");
      }
      if (object == null)
      {
         throw new IllegalArgumentException("No null value accepted");
      }
      if (content.containsKey(key))
      {
         return false;
      }
      Map tmp = new HashMap(content);
      tmp.put(key, object);
      content = tmp;
      return true;
   }

   /**
    * Unregister an object.
    *
    * @param key the registration key
    * @return true if the unregistration was made
    * @throws IllegalArgumentException if the key is null
    */
   public synchronized Object unregister(Object key) throws IllegalArgumentException
   {
      if (key == null)
      {
         throw new IllegalArgumentException("No null key accepted");
      }
      if (content.containsKey(key))
      {
         Map tmp = new HashMap(content);
         Object registration = tmp.remove(key);
         content = tmp;
         return registration;
      }
      return null;
   }

   /**
    * Return an unmodifiable set containing the keys.
    *
    * @return the keys
    */
   public Set getKeys()
   {
      return Collections.unmodifiableSet(content.keySet());
   }

   /**
    * Return an unmodifable collection containing the registrations.
    *
    * @return the registrations
    */
   public Collection getRegistrations()
   {
      return Collections.unmodifiableCollection(content.values());
   }

   /**
    * Return a registration or null if it does not exist.
    *
    * @param key the registration key
    * @return the registeted object
    * @throws IllegalArgumentException if the key is null
    */
   public Object getRegistration(Object key) throws IllegalArgumentException
   {
      if (key == null)
      {
         throw new IllegalArgumentException("No null key accepted");
      }
      return content.get(key);
   }
}

