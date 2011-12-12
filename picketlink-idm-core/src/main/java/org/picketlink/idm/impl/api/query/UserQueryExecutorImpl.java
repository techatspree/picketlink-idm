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

import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.query.QueryException;
import org.picketlink.idm.impl.NotYetImplementedException;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class UserQueryExecutorImpl extends AbstractQueryExecutor implements Serializable
{
   private static final long serialVersionUID = -4196998772910705233L;

   private static Logger log = Logger.getLogger(UserQueryExecutorImpl.class.getName());

   public UserQueryExecutorImpl(IdentitySession identitySession)
   {
      super(identitySession);
   }

   public Collection<User> execute(UserQueryImpl q) throws QueryException
   {

      List<User> resultsMain = new LinkedList<User>();

      try
      {

         User uniqueUser = null;
         List<User> resultsAssociatedGroups = new LinkedList<User>();
         List<User> resultsConnectedWithRoleGroups = new LinkedList<User>();
         List<User> resultsRelatedGroups = new LinkedList<User>();

         // if no conditions perform full search
         if (q.userId == null &&
            q.groupsAssociatedWith.size() == 0 &&
            q.groupsConnectedWithRole.size() == 0 &&
            q.groupsRelated.size() == 0)
         {
            return identitySession.getPersistenceManager().findUser(q.searchCriteria);
         }

         // Process each condition separately

         if (q.userId != null)
         {
            uniqueUser = identitySession.getPersistenceManager().findUser(q.userId);
         }

         if (q.groupsAssociatedWith.size() > 0)
         {
            for (Group group : q.groupsAssociatedWith)
            {
               addAllPreservingDuplicates(resultsAssociatedGroups, identitySession.getRelationshipManager().
                  findAssociatedUsers(group, false, q.searchCriteria));
            }
         }

         if (q.groupsConnectedWithRole.size() > 0)
         {
            for (Group group : q.groupsConnectedWithRole)
            {
               addAllPreservingDuplicates(resultsConnectedWithRoleGroups, identitySession.getRoleManager().
                  findUsersWithRelatedRole(group, q.searchCriteria));
            }
         }

         if (q.groupsRelated.size() > 0)
         {
            for (Group group : q.groupsRelated)
            {
               addAllPreservingDuplicates(resultsRelatedGroups, identitySession.getRelationshipManager().
                  findRelatedUsers(group, q.searchCriteria));
            }
         }

         // If only one condition was present just return it

         if (q.userId != null &&
            uniqueUser != null &&
            q.groupsAssociatedWith.size() == 0 &&
            resultsAssociatedGroups.size() == 0 &&
            q.groupsConnectedWithRole.size() == 0 &&
            resultsConnectedWithRoleGroups.size() == 0 &&
            q.groupsRelated.size() == 0 &&
            resultsRelatedGroups.size() == 0)
         {
            resultsMain.add(uniqueUser);
            resultsMain = IdentitySearchCriteriaImpl.applyCriteria(identitySession, q.searchCriteria, resultsMain);
            return resultsMain;
         }
         else if (q.userId == null &&
            uniqueUser == null &&
            q.groupsAssociatedWith.size() > 0 &&
            resultsAssociatedGroups.size() > 0 &&
            q.groupsConnectedWithRole.size() == 0 &&
            resultsConnectedWithRoleGroups.size() == 0 &&
            q.groupsRelated.size() == 0 &&
            resultsRelatedGroups.size() == 0)
         {
            return resultsAssociatedGroups;
         }
         else if (q.userId == null &&
            uniqueUser == null &&
            q.groupsAssociatedWith.size() == 0 &&
            resultsAssociatedGroups.size() == 0 &&
            q.groupsConnectedWithRole.size() > 0 &&
            resultsConnectedWithRoleGroups.size() > 0 &&
            q.groupsRelated.size() == 0 &&
            resultsRelatedGroups.size() == 0)
         {
            return resultsConnectedWithRoleGroups;
         }
         else if (q.userId == null &&
            uniqueUser == null &&
            q.groupsAssociatedWith.size() == 0 &&
            resultsAssociatedGroups.size() == 0 &&
            q.groupsConnectedWithRole.size() == 0 &&
            resultsConnectedWithRoleGroups.size() == 0 &&
            q.groupsRelated.size() > 0 &&
            resultsRelatedGroups.size() > 0)
         {
            return resultsRelatedGroups;
         }


         // Merge results with logical AND

         boolean first = true;

         if (q.userId != null && uniqueUser != null)
         {
            first = false;
            resultsMain.add(uniqueUser);
         }

         if (q.groupsAssociatedWith.size() > 0)
         {
            if (first)
            {
               resultsMain = resultsAssociatedGroups;
               first = false;
               
            }
            else
            {
               resultsMain = mergeIdentityTypeWithAND(resultsMain, resultsAssociatedGroups);
            }
         }

         if (q.groupsConnectedWithRole.size() > 0)
         {
            if (first)
            {
               resultsMain = resultsConnectedWithRoleGroups;
               first = false;

            }
            else
            {
               resultsMain = mergeIdentityTypeWithAND(resultsMain, resultsConnectedWithRoleGroups);
            }
         }

         if (q.groupsRelated.size() > 0)
         {
            if (first)
            {
               resultsMain = resultsRelatedGroups;
               first = false;
            }
            else
            {
               resultsMain = mergeIdentityTypeWithAND(resultsMain, resultsRelatedGroups);
            }
         }

         // As results were merged criteria need to be applied separately
         if (resultsMain.size() > 0)
         {
            resultsMain = IdentitySearchCriteriaImpl.applyCriteria(identitySession, q.searchCriteria, resultsMain);
         }

      }
      catch (Exception e)
      {
         throw new QueryException("Failed to execute query", e);
      }

      return resultsMain;
   }

   public User uniqueResult(UserQueryImpl userQuery) throws QueryException
   {

      Collection<User> results = execute(userQuery);

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

   public List<User> list(UserQueryImpl userQuery) throws QueryException
   {
      Collection<User> results = execute(userQuery);

      if (results instanceof List)
      {
         return (List<User>)results;
      }

      log.info("Internal Error! Returned collection is not instanceof List");

      //TODO:
      return new LinkedList(results);
   }
}
