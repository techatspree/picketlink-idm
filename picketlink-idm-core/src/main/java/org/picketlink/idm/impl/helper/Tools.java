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

import java.util.List;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class Tools
{

   public static <E> List<E> toList(Enumeration<E> e)
   {
      if (e == null)
      {
         throw new IllegalArgumentException();
      }
      List<E> list = new ArrayList<E>();
      while (e.hasMoreElements())
      {
         list.add(e.nextElement());
      }
      return list;
   }

   public static String wildcardToRegex(String wildcard){
       StringBuffer s = new StringBuffer(wildcard.length());
       s.append('^');
       for (int i = 0, is = wildcard.length(); i < is; i++) {
           char c = wildcard.charAt(i);
           switch(c) {
               case '*':
                   s.append(".*");
                   break;
//               case '?':
//                   s.append(".");
//                   break;
                   // escape special regexp-characters
               case '(': case ')': case '[': case ']': case '$':
               case '^': case '.': case '{': case '}': case '|':
               case '\\':
                   s.append("\\");
                   s.append(c);
                   break;
               default:
                   s.append(c);
                   break;
           }
       }
       s.append('$');
       return(s.toString());
   }

   /**
    * Process dn and retrieves a part from it:
    * uid=xxx,dc=example,dc=org - retrieves xxx
    *
    * @param dn
    * @return
    */
   public static String stripDnToName(String dn)
   {
      if (dn == null || dn.length() == 0)
      {
         throw new IllegalArgumentException("Cannot process empty dn");
      }
      String name = null;

      String[] parts = dn.split(",");

      parts = parts[0].split("=");
      if (parts.length != 2)
      {
         throw new IllegalArgumentException("Wrong dn format: " + dn);
      }

      return parts[1];
   }

   public static String getOptionSingleValue(String optionName, Map<String, List<String>> options)
   {
      if (options == null || options.size() == 0)
      {
         return null;
      }

      List<String> values = options.get(optionName);

      if (values != null && values.size() > 0)
      {
         return values.get(0);
      }

      return null;
   }
}
