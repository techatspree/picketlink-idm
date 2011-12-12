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

package org.picketlink.idm.impl.api.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.Transaction;
import org.picketlink.idm.api.PersistenceManager;
import org.picketlink.idm.api.RelationshipManager;
import org.picketlink.idm.api.AttributesManager;
import org.picketlink.idm.api.RoleManager;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.Role;
import org.picketlink.idm.api.event.EventListener;
import org.picketlink.idm.api.query.UserQuery;
import org.picketlink.idm.api.query.GroupQuery;
import org.picketlink.idm.api.query.RoleQuery;
import org.picketlink.idm.api.query.UserQueryBuilder;
import org.picketlink.idm.api.query.GroupQueryBuilder;
import org.picketlink.idm.api.query.RoleQueryBuilder;
import org.picketlink.idm.api.query.QueryException;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.common.exception.FeatureNotSupportedException;
import org.picketlink.idm.spi.store.IdentityStoreSession;
import org.picketlink.idm.spi.store.IdentityStoreInvocationContext;
import org.picketlink.idm.spi.repository.IdentityStoreRepository;
import org.picketlink.idm.spi.configuration.IdentityConfigurationContext;
import org.picketlink.idm.impl.store.SimpleIdentityStoreInvocationContext;
import org.picketlink.idm.impl.api.session.context.IdentitySessionContext;
import org.picketlink.idm.impl.api.session.context.IdentitySessionContextImpl;
import org.picketlink.idm.impl.api.session.context.IdentityStoreInvocationContextResolver;
import org.picketlink.idm.impl.api.session.mapper.IdentityObjectTypeMapper;
import org.picketlink.idm.impl.api.session.managers.PersistenceManagerImpl;
import org.picketlink.idm.impl.api.session.managers.RelationshipManagerImpl;
import org.picketlink.idm.impl.api.session.managers.AttributesManagerImpl;
import org.picketlink.idm.impl.api.session.managers.RoleManagerImpl;
import org.picketlink.idm.impl.api.session.SimpleTransactionImpl;
import org.picketlink.idm.impl.api.query.UserQueryImpl;
import org.picketlink.idm.impl.api.query.GroupQueryImpl;
import org.picketlink.idm.impl.api.query.RoleQueryImpl;
import org.picketlink.idm.impl.api.query.UserQueryExecutorImpl;
import org.picketlink.idm.impl.api.query.GroupQueryExecutorImpl;
import org.picketlink.idm.impl.api.query.RoleQueryExecutorImpl;
import org.picketlink.idm.impl.api.query.UserQueryBuilderImpl;
import org.picketlink.idm.impl.api.query.GroupQueryBuilderImpl;
import org.picketlink.idm.impl.api.query.RoleQueryBuilderImpl;
import org.picketlink.idm.cache.APICacheProvider;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class IdentitySessionImpl implements IdentitySession, Serializable
{

   private static final long serialVersionUID = 7615238887627699243L;

   private static Logger log = Logger.getLogger(IdentitySessionImpl.class.getName());
   
   private final String realmName;

   private final IdentitySessionContext sessionContext;

   private final PersistenceManager persistenceManager;

   private final RelationshipManager relationshipManager;

   private final AttributesManager profileManager;

   private final RoleManager roleManager;

   private final UserQueryExecutorImpl userQueryExecutor;

   private final GroupQueryExecutorImpl groupQueryExecutor;

   private final RoleQueryExecutorImpl roleQueryExecutor;

   private final Collection<EventListener> listeners = new LinkedList<EventListener>();

   private final APICacheProvider apiCacheProvider;

   private final String cacheNS;

   public IdentitySessionContext getSessionContext()
   {
      return sessionContext;
   }

   public IdentitySessionImpl(String realmName,
                              IdentityStoreRepository repository,
                              IdentityObjectTypeMapper typeMapper,
                              APICacheProvider apiCacheProvider,
                              IdentityConfigurationContext configurationContext,
                              Map<String, List<String>> realmOptions) throws IdentityException
   {
      this.realmName = realmName;

      IdentityStoreSession storeSession = repository.createIdentityStoreSession();
      final IdentityStoreInvocationContext invocationCtx = new SimpleIdentityStoreInvocationContext(storeSession, realmName,  String.valueOf(this.hashCode()));

      IdentityStoreInvocationContextResolver resolver = new IdentityStoreInvocationContextResolver()
      {
         public IdentityStoreInvocationContext resolveInvocationContext()
         {
            return invocationCtx;
         }
      };
      
      this.apiCacheProvider = apiCacheProvider;

      if (apiCacheProvider != null)
      {
         // Find cache scope
         List<String> cacheScope = realmOptions.get("cache.scope");

         if (cacheScope != null && cacheScope.size() > 1 && cacheScope.get(0).equals("session"))
         {
            cacheNS = apiCacheProvider.getNamespace(realmName, String.valueOf(this.hashCode()));
         }
         else
         {
            cacheNS = apiCacheProvider.getNamespace(realmName);
         }
      }
      else
      {
         cacheNS = null;
      }

      sessionContext = new IdentitySessionContextImpl(repository, typeMapper, resolver);

      this.persistenceManager = new PersistenceManagerImpl(this);
      this.relationshipManager = new RelationshipManagerImpl(this);
      this.profileManager = new AttributesManagerImpl(this);
      this.roleManager = new RoleManagerImpl(this);
      this.userQueryExecutor = new UserQueryExecutorImpl(this);
      this.groupQueryExecutor = new GroupQueryExecutorImpl(this);
      this.roleQueryExecutor = new RoleQueryExecutorImpl(this);
      






   }

   public String getId()
   {
      return String.valueOf(this.hashCode());
   }


   public String getRealmName()
   {
      return realmName;
   }

   public void close() throws IdentityException
   {
      sessionContext.resolveStoreInvocationContext().getIdentityStoreSession().close();
   }

   public void save() throws IdentityException
   {
      sessionContext.resolveStoreInvocationContext().getIdentityStoreSession().save();
   }

   public void clear() throws IdentityException
   {
      sessionContext.resolveStoreInvocationContext().getIdentityStoreSession().clear();
   }

   public boolean isOpen()
   {

      return sessionContext.resolveStoreInvocationContext().getIdentityStoreSession().isOpen();
   }

   public Transaction beginTransaction()
   {
      Transaction transaction = new SimpleTransactionImpl(sessionContext.resolveStoreInvocationContext().getIdentityStoreSession());
      transaction.start();
      return transaction;
   }

   public Transaction getTransaction()
   {
      return new SimpleTransactionImpl(sessionContext.resolveStoreInvocationContext().getIdentityStoreSession());
   }

   public PersistenceManager getPersistenceManager()
   {
      return persistenceManager;
   }

   public RelationshipManager getRelationshipManager()
   {
      return relationshipManager;
   }

   public AttributesManager getAttributesManager()
   {
      return profileManager;
   }

   public RoleManager getRoleManager() throws FeatureNotSupportedException
   {
      if (!getSessionContext().getIdentityStoreRepository().getSupportedFeatures().isNamedRelationshipsSupported())
      {
         throw new FeatureNotSupportedException("Role management not supported by underlaying configured identity stores");
      }

      return roleManager;
   }

   public UserQueryBuilder createUserQueryBuilder()
   {
      return new UserQueryBuilderImpl();

   }

   public GroupQueryBuilder createGroupQueryBuilder()
   {
      return new GroupQueryBuilderImpl();

   }

   public RoleQueryBuilder createRoleQueryBuilder() throws FeatureNotSupportedException
   {
      if (!getSessionContext().getIdentityStoreRepository().getSupportedFeatures().isNamedRelationshipsSupported())
      {
         throw new FeatureNotSupportedException("Role management not supported by underlaying configured identity stores");
      }

      return new RoleQueryBuilderImpl();
   }

   public Collection<User> execute(UserQuery userQuery) throws QueryException
   {
      try
      {
         if (apiCacheProvider != null)
         {
            Collection<User> results = apiCacheProvider.getUserQuery(cacheNS, userQuery);
            if (results != null)
            {
               return results;
            }
         }

         Collection<User> results = userQueryExecutor.execute((UserQueryImpl)userQuery);

         if (apiCacheProvider != null)
         {
            apiCacheProvider.putUserQuery(cacheNS, userQuery, results);
         }

         return results;
      }
      catch (QueryException e)
      {
         if (log.isLoggable(Level.FINER))
         {
            log.log(Level.FINER, "Exception occurred: ", e);
         }
         throw e;
      }
   }

   public User uniqueResult(UserQuery userQuery) throws QueryException
   {
      try
      {
         if (apiCacheProvider != null)
         {
            User result = apiCacheProvider.getUserQueryUnique(cacheNS, userQuery);
            if (result != null)
            {
               return result;
            }
         }

         User result = userQueryExecutor.uniqueResult((UserQueryImpl)userQuery);

         if (apiCacheProvider != null)
         {
            apiCacheProvider.putUserQueryUnique(cacheNS, userQuery, result);
         }

         return result;
      }
      catch (QueryException e)
      {
         if (log.isLoggable(Level.FINER))
         {
            log.log(Level.FINER, "Exception occurred: ", e);
         }
         throw e;
      }
   }

   public List<User> list(UserQuery userQuery) throws QueryException
   {

      try
      {
         if (apiCacheProvider != null)
         {
            Collection<User> results = apiCacheProvider.getUserQuery(cacheNS, userQuery);
            if (results != null && results instanceof List)
            {
               return (List<User>)results;
            }
         }

         List<User> results = userQueryExecutor.list((UserQueryImpl)userQuery);

         if (apiCacheProvider != null)
         {
            apiCacheProvider.putUserQuery(cacheNS, userQuery, results);
         }

         return results;
      }
      catch (QueryException e)
      {
         if (log.isLoggable(Level.FINER))
         {
            log.log(Level.FINER, "Exception occurred: ", e);
         }
         throw e;
      }
   }

   public Collection<Group> execute(GroupQuery groupQuery) throws QueryException
   {
      try
      {
         if (apiCacheProvider != null)
         {
            Collection<Group> results = apiCacheProvider.getGroupQuery(cacheNS, groupQuery);
            if (results != null)
            {
               return results;
            }
         }

         Collection<Group> results = groupQueryExecutor.execute((GroupQueryImpl)groupQuery);

         if (apiCacheProvider != null)
         {
            apiCacheProvider.putGroupQuery(cacheNS, groupQuery, results);
         }

         return results;
      }
      catch (QueryException e)
      {
         if (log.isLoggable(Level.FINER))
         {
            log.log(Level.FINER, "Exception occurred: ", e);
         }
         throw e;
      }
   }

   public Group uniqueResult(GroupQuery groupQuery) throws QueryException
   {
      try
      {
         if (apiCacheProvider != null)
         {
            Group result = apiCacheProvider.getGroupQueryUnique(cacheNS, groupQuery);
            if (result != null)
            {
               return result;
            }
         }

         Group group = groupQueryExecutor.uniqueResult((GroupQueryImpl)groupQuery);

         if (apiCacheProvider != null)
         {
            apiCacheProvider.putGroupQueryUnique(cacheNS, groupQuery, group);
         }

         return group;
      }
      catch (QueryException e)
      {
         if (log.isLoggable(Level.FINER))
         {
            log.log(Level.FINER, "Exception occurred: ", e);
         }
         throw e;
      }
   }

   public List<Group> list(GroupQuery groupQuery) throws QueryException
   {
      try
      {
         if (apiCacheProvider != null)
         {
            Collection<Group> results = apiCacheProvider.getGroupQuery(cacheNS, groupQuery);
            if (results != null && results instanceof List)
            {
               return (List<Group>)results;
            }
         }

         List<Group> results = groupQueryExecutor.list((GroupQueryImpl)groupQuery);

         if (apiCacheProvider != null)
         {
            apiCacheProvider.putGroupQuery(cacheNS, groupQuery, results);
         }

         return results;
      }
      catch (QueryException e)
      {
         if (log.isLoggable(Level.FINER))
         {
            log.log(Level.FINER, "Exception occurred: ", e);
         }
         throw e;
      }
   }

   public Collection<Role> execute(RoleQuery roleQuery) throws QueryException
   {
      try
      {
         if (apiCacheProvider != null)
         {
            Collection<Role> results = apiCacheProvider.getRoleQuery(cacheNS, roleQuery);
            if (results != null)
            {
               return results;
            }
         }

         Collection<Role> results = roleQueryExecutor.execute((RoleQueryImpl)roleQuery);

         if (apiCacheProvider != null)
         {
            apiCacheProvider.putRoleQuery(cacheNS, roleQuery, results);
         }

         return results;
      }
      catch (QueryException e)
      {
         if (log.isLoggable(Level.FINER))
         {
            log.log(Level.FINER, "Exception occurred: ", e);
         }
         throw e;
      }
   }

   public Role uniqueResult(RoleQuery roleQuery) throws QueryException
   {
      try
      {
         if (apiCacheProvider != null)
         {
            Role result = apiCacheProvider.getRoleQueryUnique(cacheNS, roleQuery);
            if (result != null)
            {
               return result;
            }
         }

         Role result = roleQueryExecutor.uniqueResult((RoleQueryImpl)roleQuery);

         if (apiCacheProvider != null)
         {
            apiCacheProvider.putRoleQueryUnique(cacheNS, roleQuery, result);
         }

         return result;
      }
      catch (QueryException e)
      {
         if (log.isLoggable(Level.FINER))
         {
            log.log(Level.FINER, "Exception occurred: ", e);
         }
         throw e;
      }
   }

   public List<Role> list(RoleQuery roleQuery) throws QueryException
   {
      try
      {
         if (apiCacheProvider != null)
         {
            Collection<Role> results = apiCacheProvider.getRoleQuery(cacheNS, roleQuery);
            if (results != null && results instanceof List)
            {
               return (List<Role>)results;
            }
         }

         List<Role> results = roleQueryExecutor.list((RoleQueryImpl)roleQuery);

         if (apiCacheProvider != null)
         {
            apiCacheProvider.putRoleQuery(cacheNS, roleQuery, results);
         }

         return results;
      }
      catch (QueryException e)
      {
         if (log.isLoggable(Level.FINER))
         {
            log.log(Level.FINER, "Exception occurred: ", e);
         }
         throw e;
      }
   }

   public void registerListener(EventListener listener)
   {
      listeners.add(listener);
   }

   public Collection<EventListener> getListeners()
   {
      return listeners;
   }

   public APICacheProvider getApiCacheProvider()
   {
      return apiCacheProvider;
   }

   public String getCacheNS()
   {
      return cacheNS;
   }
}
