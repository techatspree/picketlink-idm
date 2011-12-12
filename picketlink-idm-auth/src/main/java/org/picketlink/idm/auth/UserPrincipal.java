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

package org.picketlink.idm.auth;

import java.security.Principal;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class UserPrincipal implements Principal
{

   /** . */
   private final String name;

   public UserPrincipal(String name)
   {
      if (name == null)
      {
         throw new IllegalArgumentException("No null principal name accepted");
      }
      this.name = name;
   }

   public String getName()
   {
      return name;
   }

   public String toString()
   {
      return "PortalPrincipal[" + name + "]";
   }

   public boolean equals(Object o)
   {
      if (o == this)
      {
         return true;
      }
      if (o instanceof Principal)
      {
         Principal that = (Principal)o;
         return name.equals(that.getName());
      }
      return false;
   }

   public int hashCode()
   {
      return name.hashCode();
   }
}