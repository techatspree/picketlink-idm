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

import org.picketlink.idm.common.exception.IdentityException;

import java.util.Set;
import java.util.Map;

/**
 * All operations on attributes
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface AttributesManager
{

   /**
    * @return Session associated with this object instance
    */
   IdentitySession getIdentitySession();

   /**
    * Get AttributeDesciption object for single attribute supported with a given IdentityType
    *
    * @param identityType
    * @param name
    * @return
    */
   AttributeDescription getAttributeDescription(IdentityType identityType, String name);

   /**
    * Get AttributeDesciption object for single attribute supported with a given IdentityType id
    *
    * @param id
    * @return
    */
   AttributeDescription getAttributeDescription(String id, String attributeName);

   /**
    * Get AttributeDesciption objects for all attributes supported with a given IdentityType
    *
    * @param identityType
    * @return
    */
   Map<String, AttributeDescription> getSupportedAttributesDescriptions(IdentityType identityType);

   /**
    * Get AttributeDesciption objects for all attributes supported with an IdentityType object with a given IdentityType id
    *
    * @param id
    * @return
    */
   Map<String, AttributeDescription> getSupportedAttributesDescriptions(String id);

   /**
    * @param identityType
    * @return names of supported attributes
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Set<String> getSupportedAttributeNames(IdentityType identityType)
   throws IdentityException;

   /**
    * @param id - IdentityType id
    * @return names of supported attributes
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Set<String> getSupportedAttributeNames(String id)
   throws IdentityException;

   /**
    * Get attributes for the given IdentityType
    *
    * @param identity
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Map<String, Attribute> getAttributes(IdentityType identity) throws IdentityException;

   /**
    * Get attributes for the given IdentityType id
    *
    * @param id
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Map<String, Attribute> getAttributes(String id) throws IdentityException;

   /**
    * Get attribute values for the given IdentityType
    *
    * @param identity
    * @param attributeName
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Attribute getAttribute(IdentityType identity, String attributeName) throws IdentityException;

   /**
    * Get attribute values for the given IdentityType id
    *
    * @param id
    * @param attributeName
    * @return
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   Attribute getAttribute(String id, String attributeName) throws IdentityException;

   /**
    * Update attributes with new values - previous values will be overwritten. All the other attributes are not changed -
    * method doesn't overwrite whole attribute set connected with a given IdentityType object.
    *
    * @param identity
    * @param attributes
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void updateAttributes(IdentityType identity, Attribute[] attributes)
   throws IdentityException;

   /**
    * Update attributes with new values - previous values will be overwritten. All the other attributes are not changed -
    * method doesn't overwrite whole attribute set connected with a given IdentityType id.
    *
    * @param id
    * @param attributes
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void updateAttributes(String id, Attribute[] attributes)
   throws IdentityException;

   /**
    * Add new attributes - if attribute with given name already exists the values
    * will be appended
    *
    * @param identity
    * @param attributes
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void addAttributes(IdentityType identity, Attribute[] attributes)
   throws IdentityException;

   /**
    * Add new attributes - if attribute with given name already exists the values
    * will be appended
    *
    * @param id
    * @param attributes
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void addAttributes(String id, Attribute[] attributes)
   throws IdentityException;

   /**
    * Add new attribute - if attribute with given name already exists the values
    * will be appended
    *
    * @param identity
    * @param attributeName
    * @param values
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void addAttribute(IdentityType identity, String attributeName, Object[] values)
   throws IdentityException;

   /**
    * Add new attribute - if attribute with given name already exists the values
    * will be appended
    *
    * @param id
    * @param attributeName
    * @param values
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void addAttribute(String id, String attributeName, Object[] values)
   throws IdentityException;

   /**
    * Add new attribute - if attribute with given name already exists the values
    * will be appended
    *
    * @param identity
    * @param attributeName
    * @param value
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void addAttribute(IdentityType identity, String attributeName, Object value)
   throws IdentityException;

   /**
    * Add new attribute - if attribute with given name already exists the values
    * will be appended
    *
    * @param id
    * @param attributeName
    * @param value
    * @throws org.picketlink.idm.common.exception.IdentityException
    */
   void addAttribute(String id, String attributeName, Object value)
   throws IdentityException;

   /**
    * Remove attributes
    *
    * @param identity
    * @param attributeNames
    */
   void removeAttributes(IdentityType identity, String[] attributeNames)
   throws IdentityException;

   /**
    * Remove attributes
    *
    * @param id
    * @param attributeNames
    */
   void removeAttributes(String id, String[] attributeNames)
   throws IdentityException;

   /**
    * Check if given identity can be protected with text password
    *
    * @param user
    * @return
    * @throws IdentityException
    */
   boolean hasPassword(User user) throws IdentityException;

   /**
    * Validate text password for a given identity
    *
    * @param user
    * @param password
    * @return
    * @throws IdentityException
    */
   boolean validatePassword(User user, String password) throws IdentityException;

   /**
    * Update text password for a given identity
    *
    * @param user
    * @param password
    * @throws IdentityException
    */
   void updatePassword(User user, String password) throws IdentityException;

   /**
    * Check if user can be protected with a given credential type
    *
    * @param credentialType
    */
   boolean isCredentialTypeSupported(CredentialType credentialType) throws IdentityException;

   /**
    * Validate credential for a given identity
    *
    * @param user
    * @param credentials
    * @return
    */
   boolean validateCredentials(User user, Credential[] credentials) throws IdentityException;

   /**
    * Update credential for a given identity
    * 
    * @param user
    * @param credential
    */
   void updateCredential(User user, Credential credential) throws IdentityException;

   /**
    * Obtains user by its unique attribute value
    *
    * @param attributeName
    * @param value
    * @return
    * @throws IdentityException
    */
   User findUserByUniqueAttribute(String attributeName, Object value) throws IdentityException;

   /**
    * Obtains group by its unique attribute value
    * @param groupType
    * @param attributeName
    * @param value
    * @return
    * @throws IdentityException
    */
   Group findGroupByUniqueAttribute(String groupType, String attributeName, Object value) throws IdentityException;

}
