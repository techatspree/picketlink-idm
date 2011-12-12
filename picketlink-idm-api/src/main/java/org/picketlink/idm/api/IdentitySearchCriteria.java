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


/**
 * Criteria applied to identity objects searches
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface IdentitySearchCriteria
{

   /**
    * Sort results
    * @param order
    * @return
    * @throws UnsupportedCriterium
    */
   IdentitySearchCriteria sort(SortOrder order) throws UnsupportedCriterium;

   /**
    * Sort results by attribute name
    *
    * @param name
    * @return
    * @throws UnsupportedCriterium
    */
   IdentitySearchCriteria sortAttributeName(String name) throws UnsupportedCriterium;

   /**
    * Return only specified page from results
    *
    * @param firstResult
    * @param maxResults
    * @return
    * @throws UnsupportedCriterium
    */
   IdentitySearchCriteria page(int firstResult, int maxResults) throws UnsupportedCriterium;

   /**
    * Filter results by attribute values. All values must be present
    *
    * @param attributeName
    * @param attributeValue
    * @return
    * @throws UnsupportedCriterium
    */
   IdentitySearchCriteria attributeValuesFilter(String attributeName, String[] attributeValue) throws UnsupportedCriterium;

   /**
    * Filter results by id or name. Wildcard '*' can be used
    *
    * @param filter
    * @return
    * @throws UnsupportedCriterium
    */
   IdentitySearchCriteria nameFilter(String filter) throws UnsupportedCriterium;

}
