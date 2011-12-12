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

import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.Role;
import org.picketlink.idm.api.RoleType;
import org.picketlink.idm.api.query.QueryException;
import org.picketlink.idm.impl.api.model.SimpleRole;
import org.picketlink.idm.impl.NotYetImplementedException;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class RoleQueryExecutorImpl extends AbstractQueryExecutor implements Serializable
{
   private static final long serialVersionUID = -5943827542621450235L;

   public RoleQueryExecutorImpl(IdentitySession identitySession)
   {
      super(identitySession);
   }

   public Collection<Role> execute(RoleQueryImpl roleQuery) throws QueryException
   {
      List<Role> mainResults = new LinkedList<Role>();


      try
      {
         // Check all constraints combinations

         if (roleQuery.user == null &&
            roleQuery.group == null &&
            roleQuery.roleType == null)
         {
            // TODO: return all roles
            
            return mainResults;
         }
         else if (roleQuery.user != null &&
            roleQuery.group != null &&
            roleQuery.roleType != null)
         {
            Role role = identitySession.getRoleManager().getRole(roleQuery.roleType, roleQuery.user, roleQuery.group);
            if (role != null)
            {
               mainResults.add(role);
            }
            return mainResults;

         }
         else if (roleQuery.user != null &&
            roleQuery.group == null &&
            roleQuery.roleType == null)
         {
            return identitySession.getRoleManager().findRoles(roleQuery.user, null);
         }
         else if (roleQuery.user != null &&
            roleQuery.group == null &&
            roleQuery.roleType != null)
         {
            return identitySession.getRoleManager().findRoles(roleQuery.user, roleQuery.roleType);
         }
         else if (roleQuery.user == null &&
            roleQuery.group != null &&
            roleQuery.roleType == null)
         {
           return identitySession.getRoleManager().findRoles(roleQuery.group, null);
         }
         else if (roleQuery.user == null &&
            roleQuery.group != null &&
            roleQuery.roleType != null)
         {
            return identitySession.getRoleManager().findRoles(roleQuery.group, roleQuery.roleType);
         }
         else if (roleQuery.user == null &&
            roleQuery.group == null &&
            roleQuery.roleType != null)
         {
            //TODO: reconsider if this should be supported
            throw new QueryException("Not enought information to perform a query. Cannot query roles only with a given RoleType");
         }
         else if (roleQuery.user != null &&
            roleQuery.group != null &&
            roleQuery.roleType == null)
         {
            Collection<RoleType> roleTypes = identitySession.getRoleManager().
               findRoleTypes(roleQuery.user, roleQuery.group, roleQuery.searchCriteria);
            for (RoleType type : roleTypes)
            {
               mainResults.add(new SimpleRole(type, roleQuery.user, roleQuery.group));
            }
            return mainResults;
         }
      }
      catch (Exception e)
      {
         throw new QueryException("Failed to execute query", e);
      }

      mainResults = applyCriteriaRoles(roleQuery.searchCriteria, mainResults);

      return mainResults;

   }

   public Role uniqueResult(RoleQueryImpl roleQuery) throws QueryException
   {
      Collection<Role> results = execute(roleQuery);

      if (results.size() > 1)
      {
         throw new QueryException("More than one result returned");
      }
      else if (results.size() == 1)
      {
         return results.iterator().next();
      }

      return null;
   }

   public List<Role> list(RoleQueryImpl roleQuery) throws QueryException
   {
      Collection<Role> results = execute(roleQuery);

      if (results instanceof List)
      {
         return (List<Role>)results;
      }

      //TODO:
      throw new NotYetImplementedException();
   }
}
