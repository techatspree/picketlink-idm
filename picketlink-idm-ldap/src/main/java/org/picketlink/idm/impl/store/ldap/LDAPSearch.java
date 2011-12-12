
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
package org.picketlink.idm.impl.store.ldap;

import java.util.Arrays;

import javax.naming.ldap.Control;

/**
 * Helper class to cache LDAP queries
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class LDAPSearch
{
   private final String[] entryCtxs;

   private final String filter;

   private final Object[] filterArgs;

   private final String[] returningAttributes;

   private final String searchScope;

   private final Control[] requestControls;

   public LDAPSearch(String[] entryCtxs,
                     String filter,
                     Object[] filterArgs,
                     String[] returningAttributes,
                     String searchScope,
                     Control[] requestControls)
   {
      this.entryCtxs = entryCtxs;
      this.filter = filter;
      this.filterArgs = filterArgs;
      this.returningAttributes = returningAttributes;
      this.searchScope = searchScope;
      this.requestControls = requestControls;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o == null || getClass() != o.getClass())
      {
         return false;
      }

      LDAPSearch that = (LDAPSearch) o;

      if (!Arrays.equals(entryCtxs, that.entryCtxs))
      {
         return false;
      }
      if (filter != null ? !filter.equals(that.filter) : that.filter != null)
      {
         return false;
      }
      // Probably incorrect - comparing Object[] arrays with Arrays.equals
      if (!Arrays.equals(filterArgs, that.filterArgs))
      {
         return false;
      }
      if (!Arrays.equals(requestControls, that.requestControls))
      {
         return false;
      }
      if (!Arrays.equals(returningAttributes, that.returningAttributes))
      {
         return false;
      }
      if (searchScope != null ? !searchScope.equals(that.searchScope) : that.searchScope != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = entryCtxs != null ? Arrays.hashCode(entryCtxs) : 0;
      result = 31 * result + (filter != null ? filter.hashCode() : 0);
      result = 31 * result + (filterArgs != null ? Arrays.hashCode(filterArgs) : 0);
      result = 31 * result + (returningAttributes != null ? Arrays.hashCode(returningAttributes) : 0);
      result = 31 * result + (searchScope != null ? searchScope.hashCode() : 0);
      result = 31 * result + (requestControls != null ? Arrays.hashCode(requestControls) : 0);
      return result;
   }
}
