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

package org.picketlink.idm.auth;

/**
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @version $Revision: 5907 $
 */

public class UserStatus {

	public static UserStatus OK      = new UserStatus(0);
	public static UserStatus DISABLE = new UserStatus(1);
   public static UserStatus UNEXISTING = new UserStatus(2);
   public static UserStatus NOTASSIGNEDTOROLE = new UserStatus(3);
   public static UserStatus WRONGPASSWORD = new UserStatus(4);

	private int value;

	private UserStatus(int value)
	{
		this.value = value;
	}

}