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

import org.picketlink.idm.api.IdentitySearchCriteria;
import org.picketlink.idm.api.SortOrder;
import org.picketlink.idm.api.UnsupportedCriterium;
import org.picketlink.idm.api.IdentityType;
import org.picketlink.idm.api.Attribute;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.spi.search.IdentityObjectSearchCriteria;
import org.picketlink.idm.impl.helper.Tools;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.io.Serializable;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class IdentitySearchCriteriaImpl implements IdentitySearchCriteria, IdentityObjectSearchCriteria, Serializable
{

   private boolean sorted = false;

   private String sortByName;

   private boolean ascending = true;

   private boolean paged = false;

   private int firstResult;

   private int maxResults;

   private boolean filtered = false;

   private final Map<String, String[]> attributes = new HashMap<String, String[]>();

   private String filter;

   public IdentitySearchCriteriaImpl()
   {

   }

   public IdentitySearchCriteriaImpl(IdentityObjectSearchCriteria criteria)
   {
      sorted = criteria.isSorted();
      sortByName = criteria.getSortAttributeName();
      ascending = criteria.isAscending();
      filtered = criteria.isFiltered();
      filter = criteria.getFilter();
      attributes.putAll(criteria.getValues());
      paged = criteria.isPaged();
      firstResult = criteria.getFirstResult();
      maxResults = criteria.getMaxResults();
   }


   public IdentitySearchCriteria sort(SortOrder order) throws UnsupportedCriterium
   {
      sorted = true;
      if (order.equals(SortOrder.ASCENDING))
      {
         ascending = true;
      }
      else
      {
         ascending = false;
      }

      return this;
   }

   public IdentitySearchCriteria sortAttributeName(String name) throws UnsupportedCriterium
   {
      sorted = true;
      sortByName = name;

      return this;
   }

   public IdentitySearchCriteria page(int firstResult, int maxResults) throws UnsupportedCriterium
   {
      paged = true;

      this.firstResult = firstResult;
      this.maxResults = maxResults;


      return this;
   }

   public IdentitySearchCriteria attributeValuesFilter(String attributeName, String[] attributeValue) throws UnsupportedCriterium
   {
      if (attributeName == null)
      {
         throw new IllegalArgumentException("Attribute name is null");
      }

      if (attributeValue == null)
      {
         throw new IllegalArgumentException("Attribute values are null");
         
      }

      filtered = true;

      attributes.put(attributeName, attributeValue);

      return this;
   }

   public IdentitySearchCriteria nameFilter(String filter) throws UnsupportedCriterium
   {
      if (filter == null)
      {
         throw new IllegalArgumentException("ID filter is null");
      }

      this.filter = filter;

      return this;
   }

   public String getFilter()
   {
      return filter;
   }

   public boolean isSorted()
   {
      return sorted;
   }

   public boolean isAscending()
   {
      return ascending;
   }

   public String getSortAttributeName()
   {
      return sortByName;
   }

   public boolean isPaged()
   {
      return paged;
   }

   public int getFirstResult()
   {
      return firstResult;
   }

   public int getMaxResults()
   {
      return maxResults;
   }

   public boolean isFiltered()
   {
      return filtered;
   }

   public Map<String, String[]> getValues()
   {
      return attributes;
   }

   public void setPaged(boolean paged)
   {
      this.paged = paged;
   }


   // Static helper methods:



   public static <T extends IdentityType> List<T> applyCriteria(IdentitySession identitySession,
                                                                IdentityObjectSearchCriteria criteria,
                                                                List<T> types) throws Exception
   {


      // First pass - filters

      if(criteria.isFiltered())
      {
         if (criteria.getFilter() != null)
         {
            filterByIdFilter(types, criteria.getFilter());
         }

         if (criteria.getValues() != null && criteria.getValues().size() > 0)
         {
            filterByAttributesValues(identitySession, types, criteria.getValues());
         }

      }

      // Second pass - sort

      if (criteria.isSorted())
      {
         if (criteria.getSortAttributeName() != null)
         {
             sortByAttributeName(identitySession, types, criteria.getSortAttributeName(), criteria.isAscending());
         }
         else
         {
            sortByName(types, criteria.isAscending());
         }
      }

      List<T> results = types;

      // Third pass - cut the page

      if (criteria.isPaged())
      {
         results = cutPageFromResults(results, criteria);
      }

      return results;
   }

   //TODO: quick impl. should be reviewed
   private static void filterByAttributesValues(IdentitySession identitySession,
                                                Collection<? extends IdentityType> types,
                                                Map<String, String[]> attrs) throws Exception
   {
      Set<IdentityType> toRemove = new HashSet<IdentityType>();

      for (IdentityType type : types)
      {
         //TODO: AttributeManager should have .getAttributes(type, names) to improve and not obtain everything
         Map<String, Attribute> presentAttrs = identitySession.getAttributesManager().getAttributes(type);

         for (Map.Entry<String, String[]> entry : attrs.entrySet())
         {
            if (presentAttrs.containsKey(entry.getKey()))
            {
               Set<String> given = new HashSet<String>(Arrays.asList(entry.getValue()));
               Attribute attr = presentAttrs.get(entry.getKey());

               Collection present = null;

               if (attr != null)
               {
                  present = attr.getValues();
               }
               else
               {
                  present = Collections.emptySet();
               }

               for (String s : given)
               {
                  if (!present.contains(s))
                  {
                     toRemove.add(type);
                     break;
                  }
               }

            }
            else
            {
               toRemove.add(type);
               break;

            }
         }
      }

      for (IdentityType type : toRemove)
      {
         types.remove(type);
      }

   }

   //TODO: quick impl. should be reviewed
   private static void filterByIdFilter(List<? extends IdentityType> types, String filter) throws Exception
   {
      Set<IdentityType> toRemove = new HashSet<IdentityType>();

      String regex = Tools.wildcardToRegex(filter);


      for (IdentityType type : types)
      {
         String id = null;

         if (type instanceof User)
         {
            id = type.getKey();
         }
         else if (type instanceof Group)
         {
            id = ((Group)type).getName();
         }
         else
         {
            // shouldn't happen
            throw new IllegalStateException();
         }

         if (!id.matches(regex))
         {
            toRemove.add(type);
         }
      }

      for (IdentityType type : toRemove)
      {
         types.remove(type);
      }

   }

   private static <T extends IdentityType> void sortByName(List<T> objects, final boolean ascending)
   {
      Collections.sort(objects, new Comparator<T>(){
         public int compare(T o1, T o2)
         {
            if (o1 instanceof User && o2 instanceof User)
            {
               if (ascending)
               {
                  return o1.getKey().compareTo(o2.getKey());
               }
               else
               {
                  return o2.getKey().compareTo(o1.getKey());
               }
            }
            else
            {
               Group g1 = (Group)o1;
               Group g2 = (Group)o2;

               if (ascending)
               {
                  return g1.getName().compareTo(g2.getName());
               }
               else
               {
                  return g2.getName().compareTo(g1.getName());
               }

            }
         }
      });
   }

   private static <T extends IdentityType> void sortByAttributeName(IdentitySession identitySession,
                                                                    List<T> objects,
                                                                    String attributeName, final boolean ascending)
      throws Exception
   {

      //TODO: Check if attribute has "text" type and delegate to name sort if not

      // Pre fetch attributes
      final Map<T, String> attributes = new HashMap<T, String>();

      for (T object : objects)
      {
         Attribute attr = identitySession.getAttributesManager().getAttribute(object, attributeName);

         if (attr != null && attr.getValue() != null)
         {
            attributes.put(object, attr.getValue().toString());
         }
         else
         {
            attributes.put(object, "");
         }
      }



      Collections.sort(objects, new Comparator<T>(){
         public int compare(T o1, T o2)
         {
            String a1 = attributes.get(o1);
            String a2 = attributes.get(o2);

            if (ascending)
            {
               return a1.compareTo(a2);
            }
            else
            {
               return a2.compareTo(a1);
            }
         }
      });
   }

   //TODO: dummy and inefficient...
   private static <T extends IdentityType> List<T> cutPageFromResults(List<T> objects,
                                                                      IdentityObjectSearchCriteria criteria)
   {
      List<T> results = new LinkedList<T>();

      if (criteria.getMaxResults() == 0)
      {
         for (int i = criteria.getFirstResult(); i < objects.size(); i++)
         {
            if (i < objects.size())
            {
               results.add(objects.get(i));
            }
         }
      }
      else
      {
         for (int i = criteria.getFirstResult(); i < criteria.getFirstResult() + criteria.getMaxResults(); i++)
         {
            if (i < objects.size())
            {
               results.add(objects.get(i));
            }
         }
      }
      return results;
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

      IdentitySearchCriteriaImpl that = (IdentitySearchCriteriaImpl)o;

      if (ascending != that.ascending)
      {
         return false;
      }
      if (filtered != that.filtered)
      {
         return false;
      }
      if (firstResult != that.firstResult)
      {
         return false;
      }
      if (maxResults != that.maxResults)
      {
         return false;
      }
      if (paged != that.paged)
      {
         return false;
      }
      if (sorted != that.sorted)
      {
         return false;
      }
      if (attributes != null ? !attributes.equals(that.attributes) : that.attributes != null)
      {
         return false;
      }
      if (filter != null ? !filter.equals(that.filter) : that.filter != null)
      {
         return false;
      }
      if (sortByName != null ? !sortByName.equals(that.sortByName) : that.sortByName != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = (sorted ? 1 : 0);
      result = 31 * result + (sortByName != null ? sortByName.hashCode() : 0);
      result = 31 * result + (ascending ? 1 : 0);
      result = 31 * result + (paged ? 1 : 0);
      result = 31 * result + firstResult;
      result = 31 * result + maxResults;
      result = 31 * result + (filtered ? 1 : 0);
      result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
      result = 31 * result + (filter != null ? filter.hashCode() : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return Integer.toString(hashCode());
   }
}
