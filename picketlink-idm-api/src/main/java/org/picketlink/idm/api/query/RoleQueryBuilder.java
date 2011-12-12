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

package org.picketlink.idm.api.query;

import org.picketlink.idm.api.User;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.RoleType;
import org.picketlink.idm.api.IdentityType;

/**
 * Expose operations to set conditions for Role search and create RoleQuery
 *
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface RoleQueryBuilder extends QueryBuilder
{
   /**
    * Create RoleQuery based on specified conditions
    * @return
    */
   RoleQuery createQuery();

   /**
    * Reset query conditions
    * @return
    */
   RoleQueryBuilder reset();

   /**
    * Search for roles connected with a given user
    *
    * @param user
    * @return
    */
   RoleQueryBuilder setUser(User user);

   /**
    * Search for roles connected with a given user
    *
    * @param id
    * @return
    */
   RoleQueryBuilder setUser(String id);

   /**
    * Search for roles connected with a given group
    *
    * @param group
    * @return
    */
   RoleQueryBuilder setGroup(Group group);

   /**
    * Search for roles connected with a given group
    *
    * @param key
    * @return
    */
   RoleQueryBuilder setGroup(String key);

   /**
    * Search for roles with a given RoleType
    *
    * @param roleType
    * @return
    */
   RoleQueryBuilder setRoleType(RoleType roleType);

   /**
    * Search for roles with a given RoleType name
    *
    * @param string
    * @return
    */
   RoleQueryBuilder setRoleType(String string);

   /**
    * Search for roles connected with a given IdentityType object
    *
    * @param identityType
    * @return
    */
   RoleQueryBuilder setIdentityType(IdentityType identityType);

   /**
    * Search for roles connected with a given IdentityType object 
    *
    * @param key
    * @return
    */
   RoleQueryBuilder setIdentityTypeKey(String key);

}
