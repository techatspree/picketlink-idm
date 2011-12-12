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

package org.picketlink.idm.spi.model;

import org.picketlink.idm.common.exception.PolicyValidationException;

/**
 * @author <a href="mailto:boleslaw dot dawidowicz at redhat anotherdot com">Boleslaw Dawidowicz</a>
 * @author Anil.Saldhana@redhat.com
 * @version : 0.1 $
 */
public interface IdentityObject
{

   /**
    * @return id of this identity object. String representation of natural store id associated with identity entry.
    * Could be FQDN in LDAP or surrogate key for DB. Can be null
    */
   String getId();

   /**
    * <p>Return the name of the identity. Name must be unique value in scope of a IdentityObjectType</p>
    * @return
    */
   String getName();

   /**
    * <p>Return type of this identity object</p>
    * @return
    */
   IdentityObjectType getIdentityType();

   /**
    * Validate configured Policies
    * @throws org.picketlink.idm.common.exception.PolicyValidationException
    */
   void validatePolicy() throws PolicyValidationException;
}
