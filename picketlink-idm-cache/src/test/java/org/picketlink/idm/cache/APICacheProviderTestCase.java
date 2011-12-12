/*
* JBoss, a division of Red Hat
* Copyright 2009, Red Hat Middleware, LLC, and individual contributors as indicated
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

package org.picketlink.idm.cache;

import junit.framework.TestCase;

import java.util.Map;
import java.util.HashMap;

import org.picketlink.idm.impl.cache.JBossCacheAPICacheProviderImpl;
import org.picketlink.idm.impl.api.model.SimpleUser;
import org.picketlink.idm.api.User;


public class APICacheProviderTestCase extends TestCase
{

   public void testSimple() throws Exception
   {
      Map<String, String> props = new HashMap<String, String>();
      props.put(JBossCacheAPICacheProviderImpl.CONFIG_FILE_OPTION, "jboss-cache.xml");
      APICacheProvider cache = new JBossCacheAPICacheProviderImpl();
      cache.initialize(props, null);

      String ns = "toto";

      User u1 = new SimpleUser("u1");
      User u2 = new SimpleUser("u2");
      User u3 = new SimpleUser("u3");

      assertNull(cache.getUser(ns, "u1"));
      assertNull(cache.getUser(ns, "u2"));
      assertNull(cache.getUser(ns, "u3"));

      cache.putUser(ns, u1);
      cache.putUser(ns, u3);

      assertNotNull(cache.getUser(ns, "u1"));
      assertNull(cache.getUser(ns, "u2"));
      assertNotNull(cache.getUser(ns, "u3"));

      cache.invalidateUsers(ns);

      assertNull(cache.getUser(ns, "u1"));
      assertNull(cache.getUser(ns, "u2"));
      assertNull(cache.getUser(ns, "u3"));

   }

}
