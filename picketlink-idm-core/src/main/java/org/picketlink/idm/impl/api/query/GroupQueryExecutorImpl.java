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

import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.query.QueryException;
import org.picketlink.idm.impl.NotYetImplementedException;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class GroupQueryExecutorImpl extends AbstractQueryExecutor implements Serializable
{
   private static final long serialVersionUID = -6202216015171375769L;

   public GroupQueryExecutorImpl(IdentitySession identitySession)
   {
      super(identitySession);
   }

   public Collection<Group> execute(GroupQueryImpl q) throws QueryException
   {
      List<Group> mainResults = new LinkedList<Group>();

      try
      {
         Group uniqueResult = null;

         List<Group> resultsAssociatedParentGroups = new LinkedList<Group>();
         List<Group> resultsAssociatedChildGroups = new LinkedList<Group>();
         List<Group> resultsUsersAssociated = new LinkedList<Group>();
         List<Group> resultsUsersRelated = new LinkedList<Group>();
         List<Group> resultsUsersConnectedByRole = new LinkedList<Group>();

         // If no conditions

         if (q.groupKey == null &&
            q.groupName == null &&
            q.groupType == null &&
            q.associatedParentGroups.size() == 0 &&
            q.associatedChildGroups.size() == 0 &&
            q.usersAssociated.size() == 0 &&
            q.usersConnectedByRole.size() == 0 &&
            q.usersRelated.size() == 0)
         {
            throw new QueryException("Not enought information to perform a query. Cannot query groups without at least " +
               "group type");

            //TODO: extend API for that maybe

         }

         if (q.groupKey != null)
         {
            uniqueResult = identitySession.getPersistenceManager().
               findGroup(q.groupKey.getName(), q.groupKey.getType());
         }

         // Process each condition

         if (q.associatedParentGroups.size() > 0)
         {
            for (Group group : q.associatedParentGroups)
            {
               addAllPreservingDuplicates(resultsAssociatedParentGroups, identitySession.getRelationshipManager().findAssociatedGroups(
                  group, q.groupType, true, false, q.searchCriteria));
            }
         }

         if (q.associatedChildGroups.size() > 0)
         {
            for (Group group : q.associatedChildGroups)
            {
               addAllPreservingDuplicates(resultsAssociatedChildGroups, identitySession.getRelationshipManager().findAssociatedGroups(
                  group, q.groupType, false, false, q.searchCriteria));
            }
         }


         if (q.usersAssociated.size() > 0)
         {
            for (User user : q.usersAssociated)
            {
               addAllPreservingDuplicates(resultsUsersAssociated,identitySession.getRelationshipManager().findAssociatedGroups(
                  user, q.groupType, q.searchCriteria));
            }
         }

         if (q.usersRelated.size() > 0)
         {
            for (User user : q.usersRelated)
            {
               addAllPreservingDuplicates(resultsUsersRelated, identitySession.getRelationshipManager().
                  findRelatedGroups(user, q.groupType, q.searchCriteria));
            }
         }

         if (q.usersConnectedByRole.size() > 0)
         {
            for (User user : q.usersConnectedByRole)
            {
               addAllPreservingDuplicates(resultsUsersConnectedByRole, identitySession.getRoleManager().
                  findGroupsWithRelatedRole(user, q.groupType, q.searchCriteria));
            }
         }

         // If only one search was performed then return it as search criteria are applied correctly

         if (q.groupKey == null &&
            uniqueResult == null &&
            q.associatedChildGroups.size() == 0 &&
            resultsAssociatedChildGroups.size() == 0 &&
            q.associatedParentGroups.size() == 0 &&
            resultsAssociatedParentGroups.size() == 0 &&
            q.usersAssociated.size() == 0 &&
            resultsUsersAssociated.size() == 0 &&
            q.usersConnectedByRole.size() == 0 &&
            resultsUsersConnectedByRole.size() == 0 &&
            q.usersRelated.size() == 0 &&
            resultsUsersRelated.size() == 0)
         {
            return mainResults;

         }
         else if (q.groupKey != null &&
            uniqueResult != null &&
            q.associatedChildGroups.size() == 0 &&
            resultsAssociatedChildGroups.size() == 0 &&
            q.associatedParentGroups.size() == 0 &&
            resultsAssociatedParentGroups.size() == 0 &&
            q.usersAssociated.size() == 0 &&
            resultsUsersAssociated.size() == 0 &&
            q.usersConnectedByRole.size() == 0 &&
            resultsUsersConnectedByRole.size() == 0 &&
            q.usersRelated.size() == 0 &&
            resultsUsersRelated.size() == 0)
         {

            mainResults.add(uniqueResult);
            IdentitySearchCriteriaImpl.applyCriteria(identitySession, q.searchCriteria, mainResults);
            return mainResults;
         }
         else if (q.groupKey == null &&
            uniqueResult == null &&
            q.associatedChildGroups.size() != 0 &&
            resultsAssociatedChildGroups.size() != 0 &&
            q.associatedParentGroups.size() == 0 &&
            resultsAssociatedParentGroups.size() == 0 &&
            q.usersAssociated.size() == 0 &&
            resultsUsersAssociated.size() == 0 &&
            q.usersConnectedByRole.size() == 0 &&
            resultsUsersConnectedByRole.size() == 0 &&
            q.usersRelated.size() == 0 &&
            resultsUsersRelated.size() == 0)
         {
            return resultsAssociatedChildGroups;

         }
         else if (q.groupKey == null &&
            uniqueResult == null &&
            q.associatedChildGroups.size() == 0 &&
            resultsAssociatedChildGroups.size() == 0 &&
            q.associatedParentGroups.size() != 0 &&
            resultsAssociatedParentGroups.size() != 0 &&
            q.usersAssociated.size() == 0 &&
            resultsUsersAssociated.size() == 0 &&
            q.usersConnectedByRole.size() == 0 &&
            resultsUsersConnectedByRole.size() == 0 &&
            q.usersRelated.size() == 0 &&
            resultsUsersRelated.size() == 0)
         {
            return resultsAssociatedParentGroups;
         }
         else if (q.groupKey == null &&
            uniqueResult == null &&
            q.associatedChildGroups.size() == 0 &&
            resultsAssociatedChildGroups.size() == 0 &&
            q.associatedParentGroups.size() == 0 &&
            resultsAssociatedParentGroups.size() == 0 &&
            q.usersAssociated.size() != 0 &&
            resultsUsersAssociated.size() != 0 &&
            q.usersConnectedByRole.size() == 0 &&
            resultsUsersConnectedByRole.size() == 0 &&
            q.usersRelated.size() == 0 &&
            resultsUsersRelated.size() == 0)
         {
            return resultsUsersAssociated;
         }
         else if (q.groupKey == null &&
            uniqueResult == null &&
            q.associatedChildGroups.size() == 0 &&
            resultsAssociatedChildGroups.size() == 0 &&
            q.associatedParentGroups.size() == 0 &&
            resultsAssociatedParentGroups.size() == 0 &&
            q.usersAssociated.size() == 0 &&
            resultsUsersAssociated.size() == 0 &&
            q.usersConnectedByRole.size() != 0 &&
            resultsUsersConnectedByRole.size() != 0 &&
            q.usersRelated.size() == 0 &&
            resultsUsersRelated.size() == 0)
         {
            return resultsUsersConnectedByRole;
         }
         else if (q.groupKey == null &&
            uniqueResult == null &&
            q.associatedChildGroups.size() == 0 &&
            resultsAssociatedChildGroups.size() == 0 &&
            q.associatedParentGroups.size() == 0 &&
            resultsAssociatedParentGroups.size() == 0 &&
            q.usersAssociated.size() == 0 &&
            resultsUsersAssociated.size() == 0 &&
            q.usersConnectedByRole.size() == 0 &&
            resultsUsersConnectedByRole.size() == 0 &&
            q.usersRelated.size() != 0 &&
            resultsUsersRelated.size() != 0)
         {
            return resultsUsersRelated;
         }

         // Merge with logical AND

         boolean first = true;

         if (q.groupKey != null)
         {
            first = false;
            mainResults.add(uniqueResult);
         }

         if (q.associatedChildGroups.size() > 0)
         {
            if (first)
            {
               first = false;
               mainResults = resultsAssociatedChildGroups;
            }
            else
            {
               mainResults = mergeIdentityTypeWithAND(mainResults, resultsAssociatedChildGroups);
            }
         }

         if (q.associatedParentGroups.size() > 0)
         {
            if (first)
            {
               first = false;
               mainResults = resultsAssociatedParentGroups;
            }
            else
            {
               mainResults = mergeIdentityTypeWithAND(mainResults, resultsAssociatedParentGroups);
            }
         }

         if (q.usersAssociated.size() > 0)
         {
            if (first)
            {
               first = false;
               mainResults = resultsUsersAssociated;
            }
            else
            {
               mainResults = mergeIdentityTypeWithAND(mainResults, resultsUsersAssociated);
            }
         }

         if (q.usersConnectedByRole.size() > 0)
         {
            if (first)
            {
               first = false;
               mainResults = resultsUsersConnectedByRole;
            }
            else
            {
               mainResults = mergeIdentityTypeWithAND(mainResults, resultsUsersConnectedByRole);
            }
         }

         if (q.usersRelated.size() > 0)
         {
            if (first)
            {
               first = false;
               mainResults = resultsUsersRelated;
            }
            else
            {
               mainResults = mergeIdentityTypeWithAND(mainResults, resultsUsersRelated);
            }
         }

         IdentitySearchCriteriaImpl.applyCriteria(identitySession, q.searchCriteria, mainResults);
      }
      catch (Exception e)
      {
         throw new QueryException("Failed to execute query", e);
      }

      return mainResults;
   }

   public Group uniqueResult(GroupQueryImpl groupQuery) throws QueryException
   {
      Collection<Group> results = execute(groupQuery);

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

   public List<Group> list(GroupQueryImpl groupQuery) throws QueryException
   {
      Collection<Group> results = execute(groupQuery);

      if (results instanceof List)
      {
         return (List<Group>)results;
      }

      //TODO:
      throw new NotYetImplementedException();
   }


}
