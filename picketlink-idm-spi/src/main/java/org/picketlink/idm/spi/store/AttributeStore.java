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

package org.picketlink.idm.spi.store;

import org.picketlink.idm.spi.model.IdentityObjectType;
import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectAttribute;
import org.picketlink.idm.spi.configuration.metadata.IdentityObjectAttributeMetaData;
import org.picketlink.idm.common.exception.IdentityException;

import java.util.Set;
import java.util.Map;

/**
 * Store for identity related attributes. Its separate interface as there is possible need to store profiles in a
 * distributted way (part in LDAP part in DB). 
 * 
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface AttributeStore extends IdentityStoreSessionFactory
{

   /**
    * @return id of this identity store
    */
   String getId();

   /**
    * @param invocationContext
    * @param identityType @return names of supported attributes
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Set<String> getSupportedAttributeNames(IdentityStoreInvocationContext invocationContext, IdentityObjectType identityType)
   throws IdentityException;

   /**
    * 
    * @return
    */
   Map<String, IdentityObjectAttributeMetaData> getAttributesMetaData(IdentityStoreInvocationContext invocationContext, IdentityObjectType identityType);

   /**
    * Get attributes for the given identity
    *
    * @param invocationContext
    *@param identity @return @throws IdentityException
    */
   Map<String, IdentityObjectAttribute> getAttributes(IdentityStoreInvocationContext invocationContext, IdentityObject identity) throws IdentityException;

   /**
    * Get attribute for the given identity
    *
    * @param invocationContext
    *@param identity @return @throws IdentityException
    */
   IdentityObjectAttribute getAttribute(IdentityStoreInvocationContext invocationContext, IdentityObject identity, String name) throws IdentityException;

   /**
    * Update attributes with new values - previous values will be overwritten. Attributes not specified in the map are not changed.
    * @param invocationCtx
    * @param identity
    * @param attributes @throws IdentityException
    */
   void updateAttributes(IdentityStoreInvocationContext invocationCtx, IdentityObject identity, IdentityObjectAttribute[] attributes)
   throws IdentityException;

   /**
    * Add new attributes - if attribute with given name already exists the values
    * will be appended
    *
    * @param invocationCtx
    * @param identity
    * @param attributes @throws IdentityException
    */
   void addAttributes(IdentityStoreInvocationContext invocationCtx, IdentityObject identity, IdentityObjectAttribute[] attributes)
   throws IdentityException;

   /**
    * Remove attributes
    *
    * @param invocationCtx
    * @param identity
    * @param attributeNames
    */
   void removeAttributes(IdentityStoreInvocationContext invocationCtx, IdentityObject identity, String[] attributeNames)
   throws IdentityException;

   /**
    * Finds IdentityObject by its unique attribute value
    *
    * @param invocationCtx
    * @param identityObjectType
    * @param attribute
    * @return
    * @throws IdentityException
    */
   IdentityObject findIdentityObjectByUniqueAttribute(IdentityStoreInvocationContext invocationCtx,
                                                      IdentityObjectType identityObjectType,
                                                      IdentityObjectAttribute attribute) throws IdentityException;

}
