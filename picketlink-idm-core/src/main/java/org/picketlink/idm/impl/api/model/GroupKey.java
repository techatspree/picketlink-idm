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

package org.picketlink.idm.impl.api.model;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class GroupKey
{
   public static final String PREFIX = "jbpid_group_id";

   public static final String SEPARATOR = "_._._";

   public static final String SEPARATOR_REGEX = "_\\._\\._";

   private String name;

   private String type;

   public GroupKey(String name, String type)
   {
      if (name == null)
      {
         throw new IllegalArgumentException("name is null");
      }
      if (type == null)
      {
         throw new IllegalArgumentException("type is null");
      }

      this.name = name;
      this.type = type;
   }

   public GroupKey(String id)
   {
      if (id == null)
      {
         throw new IllegalArgumentException("id is null");
      }

      String[] parts = id.split(SEPARATOR_REGEX);

      if (!validateKey(id))
      {
         throw new IllegalArgumentException("group id not following required format: " +
            PREFIX + SEPARATOR + "groupType" + SEPARATOR + "name : " + id);
      }

      name = parts[2];
      type = parts[1];
   }

   public static boolean validateKey(String key)
   {
      String[] parts = key.split(SEPARATOR_REGEX);

      if (parts == null || parts.length != 3 || parts[0].equals(PREFIX.substring(0, PREFIX.length() - 1)))
      {
         return false;
      }

      return true;
   }

   public static String parseKey(String name, String type)
   {
      GroupKey gid = new GroupKey(name, type);
      String id = gid.getKey();
      gid = null;
      return id;
   }

   public String getName()
   {
      return name;
   }

   public String getType()
   {
      return type;
   }

   public String getKey()
   {
      return PREFIX + SEPARATOR + type + SEPARATOR + name;
   }
}
