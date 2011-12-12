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

package org.picketlink.idm.impl.api.session.managers;

import org.picketlink.idm.api.*;
import org.picketlink.idm.api.event.*;
import org.picketlink.idm.spi.repository.IdentityStoreRepository;
import org.picketlink.idm.spi.store.IdentityStoreInvocationContext;
import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.spi.model.IdentityObjectAttribute;
import org.picketlink.idm.spi.search.IdentityObjectSearchCriteria;
import org.picketlink.idm.impl.types.SimpleIdentityObject;
import org.picketlink.idm.impl.api.session.context.IdentitySessionContext;
import org.picketlink.idm.impl.api.session.IdentitySessionImpl;
import org.picketlink.idm.impl.api.model.SimpleUser;
import org.picketlink.idm.impl.api.model.SimpleGroup;
import org.picketlink.idm.impl.api.model.GroupKey;
import org.picketlink.idm.impl.api.SimpleAttribute;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;
import org.picketlink.idm.cache.APICacheProvider;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public abstract class
   AbstractManager 
   implements Serializable
{
   protected final IdentitySessionImpl identitySession;

   protected final APICacheProvider cache;

   protected final String cacheNS;

   protected AbstractManager(IdentitySessionImpl session)
   {
      this.identitySession = session;
      this.cache = session.getApiCacheProvider();
      this.cacheNS = session.getCacheNS();
   }

   public IdentitySession getIdentitySession()
   {
      return identitySession;
   }

   public IdentitySearchCriteria createIdentitySearchCriteria()
   {
      return new IdentitySearchCriteriaImpl();
   }

   protected IdentitySessionContext getSessionContext()
   {
      if (identitySession instanceof IdentitySessionImpl)
      {
         return ((IdentitySessionImpl)identitySession).getSessionContext();
      }
      return null;
   }

   protected IdentityStoreRepository getRepository()
   {
      return getSessionContext().getIdentityStoreRepository();
   }

   protected IdentityStoreInvocationContext getInvocationContext()
   {
      return getSessionContext().resolveStoreInvocationContext();
   }

   protected User createUser(IdentityObject identityObject)
   {
      return new SimpleUser(identityObject.getName());
   }

   protected User createUserFromId(String name)
   {

      return new SimpleUser(name);
   }

   protected Group createGroup(IdentityObject identityObject)
   {
      String groupType = getSessionContext().getIdentityObjectTypeMapper().getGroupType(identityObject.getIdentityType());
      return new SimpleGroup(identityObject.getName(), groupType);
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

   protected IdentityObject createIdentityObject(User user)
   {
      IdentityObjectType iot = getSessionContext().getIdentityObjectTypeMapper().getIdentityObjectType();

      return new SimpleIdentityObject(user.getKey(), iot);
   }

   protected IdentityObject createIdentityObjectForUserName(String userName)
   {
      IdentityObjectType iot = getSessionContext().getIdentityObjectTypeMapper().getIdentityObjectType();

      return new SimpleIdentityObject(userName, iot);
   }

   protected IdentityObject createIdentityObject(Group group)
   {
      IdentityObjectType iot = getSessionContext().getIdentityObjectTypeMapper().getIdentityObjectType(group.getGroupType());

      return new SimpleIdentityObject(group.getName(), group.getKey(), iot);
   }

   protected IdentityObject createIdentityObjectForGroupId(String groupId)
   {
      GroupKey key = new GroupKey(groupId);

      IdentityObjectType iot = getSessionContext().getIdentityObjectTypeMapper().getIdentityObjectType(key.getType());

      return new SimpleIdentityObject(key.getName(), null, iot);
   }

   protected IdentityObject createIdentityObject(IdentityType identityType)
   {
      if (identityType instanceof User)
      {
         return createIdentityObject((User)identityType);
      }
      else if (identityType instanceof Group)
      {
         return createIdentityObject((Group)identityType);
      }

      throw new IllegalStateException("Not supported IdentityType extension: " + identityType.getClass());

   }

   protected IdentityObject createIdentityObject(String id)
   {
      if (GroupKey.validateKey(id))
      {
         GroupKey groupKey = new GroupKey(id);

         return createIdentityObjectForGroupId(id);
      }
      else
      {
         return createIdentityObjectForUserName(id);
      }
   }



   protected Group createGroupFromId(String id)
   {
      return new SimpleGroup(new GroupKey(id));
   }

   protected IdentityObjectSearchCriteria convertSearchControls(IdentitySearchCriteria criteria)
   {
      if (criteria == null)
      {
         return null;
      }

      if (criteria instanceof IdentityObjectSearchCriteria)
      {
         return (IdentityObjectSearchCriteria)criteria;
      }
      else
      {
         throw new IllegalArgumentException("Not supported IdentitySearchCriteria implementation: " + criteria.getClass());
      }
   }

   protected IdentityObjectType getUserObjectType()
   {
      return getSessionContext().getIdentityObjectTypeMapper().getIdentityObjectType();
   }

   protected IdentityObjectType getIdentityObjectType(String groupType)
   {
      return getSessionContext().getIdentityObjectTypeMapper().getIdentityObjectType(groupType);
   }

   protected IdentityObjectAttribute[] convertAttributes(Attribute[] attributes)
   {
      IdentityObjectAttribute[] convertedAttributes = new IdentityObjectAttribute[attributes.length];

      for (int i = 0; i < attributes.length; i++)
      {
         convertedAttributes[i] = convertAttribute(attributes[i]);
      }
      return convertedAttributes;
   }

   protected Attribute[] convertAttributes(IdentityObjectAttribute[] attributes)
   {
      Attribute[] convertedAttributes = new Attribute[attributes.length];

      for (int i = 0; i < attributes.length; i++)
      {
         convertedAttributes[i] = convertAttribute(attributes[i]);
      }
      return convertedAttributes;
   }

   protected Attribute convertAttribute(IdentityObjectAttribute attribute)
   {
      if (attribute instanceof Attribute)
      {
         return (Attribute)attribute;
      }
      else
      {
         return new SimpleAttribute(attribute);
      }
   }

   protected IdentityObjectAttribute convertAttribute(Attribute attribute)
   {
      if (attribute instanceof IdentityObjectAttribute)
      {
         return (IdentityObjectAttribute)attribute;
      }
      else
      {
         return new SimpleAttribute(attribute);
      }
   }

   protected void checkNotNullArgument(Object arg, String name)
   {
      if (arg == null)
      {
         throw new IllegalArgumentException(name + " cannot be null");
      }
   }

   protected void checkObjectName(String name)
   {
      //TODO: extract this, let to define broader set of constraints and apply also in the
      //TODO: SPI to filter what comes fromdata stores

      
      if (name.contains(GroupKey.SEPARATOR))
      {
         throw new IllegalArgumentException("name cannot contain '" + GroupKey.SEPARATOR  + "' character sequence");
      }

      if (name.contains(GroupKey.PREFIX))
      {
         throw new IllegalArgumentException("name cannot contain '" + GroupKey.PREFIX  + "' character sequence");
      }
   }

   public void preCreate(IdentityType identityType)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).preCreate(identitySession, identityType);
         }
      }
   }

   public void postCreate(IdentityType identityType)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).postCreate(identitySession, identityType);
         }
      }
   }

   public void preRemove(IdentityType identityType)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).preRemove(identitySession, identityType);
         }
      }
   }

   public void postRemove(IdentityType identityType)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).postRemove(identitySession, identityType);
         }
      }
   }

   public void preAttributesAdd(IdentityType identityType, Attribute[] attributes)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).preAttributesAdd(identitySession, identityType, attributes);
         }
      }
   }

   public void postAttributesAdd(IdentityType identityType, Attribute[] attributes)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).postAttributesAdd(identitySession, identityType, attributes);
         }
      }
   }

   public void preAttributesRemove(IdentityType identityType, String[] attributes)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).preAttributesRemove(identitySession, identityType, attributes);
         }
      }
   }

   public void postAttributesRemove(IdentityType identityType, String[] attributes)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).postAttributesRemove(identitySession, identityType, attributes);
         }
      }
   }

   public void preAttributesUpdate(IdentityType identityType, Attribute[] attributes)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).preAttributesUpdate(identitySession, identityType, attributes);
         }
      }
   }

   public void postAttributesUpdate(IdentityType identityType, Attribute[] attributes)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).postAttributesUpdate(identitySession, identityType, attributes);
         }
      }
   }

   public void preCredentialUpdate(User user, Credential credential)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).preCredentialUpdate(identitySession, user, credential);
         }
      }
   }

   public void postCredentialUpdate(User user, Credential credential)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof IdentityTypeEventListener)
         {
            ((IdentityTypeEventListener)el).postCredentialUpdate(identitySession, user, credential);
         }
      }
   }

   public void preUserAssociationCreate(Group parent, User childs)
   {
     for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RelationshipEventListener)
         {
            ((RelationshipEventListener)el).preUserAssociationCreate(identitySession, parent, childs);
         }
      }
   }

   public void preGroupAssociationCreate(Group parent, Group childs)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RelationshipEventListener)
         {
            ((RelationshipEventListener)el).preGroupAssociationCreate(identitySession, parent, childs);
         }
      }
   }

   public void postUserAssociationCreate(Group parent, User childs)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RelationshipEventListener)
         {
            ((RelationshipEventListener)el).postUserAssociationCreate(identitySession, parent, childs);
         }
      }
   }

   public void postGroupAssociationCreate(Group parent, Group childs)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RelationshipEventListener)
         {
            ((RelationshipEventListener)el).postGroupAssociationCreate(identitySession, parent, childs);
         }
      }
   }

   public void preUserAssociationRemove(Group parent, User childs)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RelationshipEventListener)
         {
            ((RelationshipEventListener)el).preUserAssociationRemove(identitySession, parent, childs);
         }
      }
   }

   public void preGroupAssociationRemove(Group parent, Group childs)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RelationshipEventListener)
         {
            ((RelationshipEventListener)el).preGroupAssociationRemove(identitySession, parent, childs);
         }
      }
   }

   public void postUserAssociationRemove(Group parent, User childs)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RelationshipEventListener)
         {
            ((RelationshipEventListener)el).postUserAssociationRemove(identitySession, parent, childs);
         }
      }
   }

   public void postGroupAssociationRemove(Group parent, Group childs)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RelationshipEventListener)
         {
            ((RelationshipEventListener)el).postGroupAssociationRemove(identitySession, parent, childs);
         }
      }
   }

   public void preCreate(RoleType roleType)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleTypeEventListener)
         {
            ((RoleTypeEventListener)el).preCreate(identitySession, roleType);
         }
      }
   }

   public void postCreate(RoleType roleType)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleTypeEventListener)
         {
            ((RoleTypeEventListener)el).postCreate(identitySession, roleType);
         }
      }
   }

   public void preRemove(RoleType roleType)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleTypeEventListener)
         {
            ((RoleTypeEventListener)el).preRemove(identitySession, roleType);
         }
      }
   }

   public void postRemove(RoleType roleType)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleTypeEventListener)
         {
            ((RoleTypeEventListener)el).postRemove(identitySession, roleType);
         }
      }
   }

   public void prePropertiesSet(RoleType roleType, Map<String, String> properties)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleTypeEventListener)
         {
            ((RoleTypeEventListener)el).prePropertiesSet(identitySession, roleType, properties);
         }
      }
   }

   public void postPropertiesSet(RoleType roleType, Map<String, String> properties)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleTypeEventListener)
         {
            ((RoleTypeEventListener)el).postPropertiesSet(identitySession, roleType, properties);
         }
      }
   }
   
   public void preCreate(Role role)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleEventListener)
         {
            ((RoleEventListener)el).preCreate(identitySession, role);
         }
      }
   }

   public void postCreate(Role role)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleEventListener)
         {
            ((RoleEventListener)el).postCreate(identitySession, role);
         }
      }
   }

   public void preRemove(Role role)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleEventListener)
         {
            ((RoleEventListener)el).preRemove(identitySession, role);
         }
      }
   }

   public void postRemove(Role role)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleEventListener)
         {
            ((RoleEventListener)el).postRemove(identitySession, role);
         }
      }
   }

   public void prePropertiesSet(Role role, Map<String, String> properties)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleEventListener)
         {
            ((RoleEventListener)el).prePropertiesSet(identitySession, role, properties);
         }
      }
   }

   public void postPropertiesSet(Role role, Map<String, String> properties)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleEventListener)
         {
            ((RoleEventListener)el).postPropertiesSet(identitySession, role, properties);
         }
      }
   }

   public void prePropertiesRemove(RoleType roleType, Collection<String> names)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleTypeEventListener)
         {
            ((RoleTypeEventListener)el).prePropertiesRemove(identitySession, roleType, names);
         }
      }
   }

   public void postPropertiesRemove(RoleType roleType, Collection<String> names)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleTypeEventListener)
         {
            ((RoleTypeEventListener)el).postPropertiesRemove(identitySession, roleType, names);
         }
      }
   }

   public void prePropertiesRemove(Role role, Collection<String> names)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleEventListener)
         {
            ((RoleEventListener)el).postPropertiesRemove(identitySession, role, names);
         }
      }
   }

   public void postPropertiesRemove(Role role, Collection<String> names)
   {
      for (EventListener el : identitySession.getListeners())
      {
         if (el instanceof RoleEventListener)
         {
            ((RoleEventListener)el).postPropertiesRemove(identitySession, role, names);
         }
      }
   }
}
