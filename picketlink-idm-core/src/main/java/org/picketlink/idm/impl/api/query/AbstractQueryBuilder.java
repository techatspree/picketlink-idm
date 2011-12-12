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

package org.picketlink.idm.impl.api.query;

import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;
import org.picketlink.idm.impl.api.model.SimpleGroup;
import org.picketlink.idm.impl.api.model.GroupKey;
import org.picketlink.idm.impl.api.model.SimpleUser;
import org.picketlink.idm.api.query.UnsupportedQueryCriterium;
import org.picketlink.idm.api.query.QueryBuilder;
import org.picketlink.idm.api.SortOrder;
import org.picketlink.idm.api.UnsupportedCriterium;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.IdentityType;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class AbstractQueryBuilder implements QueryBuilder
{

   //TODO: query should check criteria with features description

   protected IdentitySearchCriteriaImpl searchCriteria = new IdentitySearchCriteriaImpl();

   public QueryBuilder sort(SortOrder order) throws UnsupportedQueryCriterium
   {
      try
      {
         searchCriteria.sort(order);
      }
      catch (UnsupportedCriterium unsupportedCriterium)
      {
         throw new UnsupportedQueryCriterium(unsupportedCriterium);
      }
      return this;
   }

   public QueryBuilder sortAttributeName(String name) throws UnsupportedQueryCriterium
   {
      try
      {
         searchCriteria.sortAttributeName(name);
      }
      catch (UnsupportedCriterium unsupportedCriterium)
      {
         throw new UnsupportedQueryCriterium(unsupportedCriterium);
      }
      return this;
   }

   public QueryBuilder page(int firstResult, int maxResults) throws UnsupportedQueryCriterium
   {
      try
      {
         searchCriteria.page(firstResult, maxResults);
      }
      catch (UnsupportedCriterium unsupportedCriterium)
      {
         throw new UnsupportedQueryCriterium(unsupportedCriterium);
      }
      return this;
   }

   public QueryBuilder attributeValuesFilter(String attributeName, String[] attributeValue) throws UnsupportedQueryCriterium
   {
      try
      {
         searchCriteria.attributeValuesFilter(attributeName, attributeValue);
      }
      catch (UnsupportedCriterium unsupportedCriterium)
      {
         throw new UnsupportedQueryCriterium(unsupportedCriterium);
      }
      return this;
   }

   public QueryBuilder idFilter(String idFilter) throws UnsupportedQueryCriterium
   {
      try
      {
         searchCriteria.nameFilter(idFilter);
      }
      catch (UnsupportedCriterium unsupportedCriterium)
      {
         throw new UnsupportedQueryCriterium(unsupportedCriterium);
      }
      return this;
   }


   protected void checkNotNullArgument(Object arg, String name)
   {
      if (arg == null)
      {
         throw new IllegalArgumentException(name + " cannot be null");
      }
   }

   protected Group createGroupFromId(String id)
   {
      return new SimpleGroup(new GroupKey(id));
   }

   protected IdentityType createIdentityTypeFromId(String id)
   {
      if (GroupKey.validateKey(id))
      {
         GroupKey groupKey = new GroupKey(id);

         return new SimpleGroup(groupKey);
      }
      else
      {
         return new SimpleUser(id);
      }
   }

}
