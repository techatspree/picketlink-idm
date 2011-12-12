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

import org.picketlink.idm.api.IdentityType;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.Role;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.Attribute;
import org.picketlink.idm.spi.search.IdentityObjectSearchCriteria;
import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.impl.helper.Tools;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public abstract class AbstractQueryExecutor
{

   protected IdentitySession identitySession;

   protected AbstractQueryExecutor(IdentitySession identitySession)
   {
      this.identitySession = identitySession;
   }

   protected <T extends IdentityType> List<T> mergeIdentityTypeWithAND(List<T> first, List<T> second)
   {
      List<T> results = new LinkedList<T>();

      for (T identityType : first)
      {
         if (second.contains(identityType))
         {
            results.add(identityType);
         }
      }

      return results;
   }

   protected <T extends IdentityType> void addAllPreservingDuplicates(Collection<T> first, Collection<T> second)
   {
      for (T t : second)
      {
         if (!first.contains(t))
         {
            first.add(t);
         }
      }
   }
   



   protected List<Role> applyCriteriaRoles(IdentityObjectSearchCriteria criteria, List<Role> roles)
   {
      //TODO: No criteria in RoleQueryBuilder for now...
      
      return roles;
   }

   
}
