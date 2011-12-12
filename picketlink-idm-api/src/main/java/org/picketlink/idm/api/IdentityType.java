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


/**
 * Parent interface for User and Group. Represents identity object
 *
 * @author Anil.Saldhana@redhat.com
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @since Jul 10, 2008
 */
public interface IdentityType
{

   /**
    * @return the pointer to the IdentityType. For User this will return same value as getId(). For Group key contains
    * encoded group type and name imformation. In default implementation it can look as follows:
    * "jbpid_group_id_._._GROUP_TYPE_._._GROUP_NAME". Still prefix and format of key can change in the future so
    * PersistenceManager.createGroupId(String groupName, String groupType) method should be used to create it for Group.
    */
   String getKey();

}