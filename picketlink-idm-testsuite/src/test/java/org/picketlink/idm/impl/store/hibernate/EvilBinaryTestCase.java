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

package org.picketlink.idm.impl.store.hibernate;

import org.picketlink.idm.impl.model.hibernate.*;
import org.picketlink.idm.impl.IdentityTestPOJO;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import java.util.*;


/**
 * Simple test to show binary/blob issues in database
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class EvilBinaryTestCase extends IdentityTestPOJO
{
   public EvilBinaryTestCase()
   {
   }


   public void setUp() throws Exception
   {
      super.start();
   }

   public void tearDown() throws Exception
   {
      super.stop();
   }

   public void testBinary() throws Exception
   {
      begin();

      Session session = getSessionFactory().getCurrentSession();


      HibernateRealm realm = new HibernateRealm("default");
      session.persist(realm);

      HibernateIdentityObjectType userType = new HibernateIdentityObjectType("User");
      session.persist(userType);
      HibernateIdentityObject user1 = new HibernateIdentityObject("user1", userType, realm);
      session.persist(user1);

      Set<String> values1 = new HashSet<String>();
      values1.add("Val1");
      values1.add("Val2");
      values1.add("Val3");

      HibernateIdentityObjectAttribute attr = new HibernateIdentityObjectAttribute(user1, "simple1", HibernateIdentityObjectAttribute.TYPE_TEXT);
      attr.setTextValues(values1);
      user1.getAttributes().add(attr);


      commit();

      begin();

      session = getSessionFactory().getCurrentSession();

      user1 = (HibernateIdentityObject)session.get(HibernateIdentityObject.class, new Long(user1.getId()));

      Random random = new Random();

      // Small
      byte[] picture = new byte[512000];
      random.nextBytes(picture);

      attr = new HibernateIdentityObjectAttribute(user1, "simple2", HibernateIdentityObjectAttribute.TYPE_BINARY);
      attr.setBinaryValue(new HibernateIdentityObjectAttributeBinaryValue(picture));
      user1.addAttribute(attr);

      values1 = new HashSet<String>();
      values1.add("Val1");

      attr = new HibernateIdentityObjectAttribute(user1, "simple3", HibernateIdentityObjectAttribute.TYPE_TEXT);
      attr.setTextValues(values1);
      user1.getAttributes().add(attr);

      values1 = new HashSet<String>();
      values1.add("Val8");
      values1.add("Val15");

      attr = new HibernateIdentityObjectAttribute(user1, "simple4", HibernateIdentityObjectAttribute.TYPE_TEXT);
      attr.setTextValues(values1);
      user1.getAttributes().add(attr);

      commit();

      begin();

      session = getSessionFactory().getCurrentSession();

      user1 = (HibernateIdentityObject)session.get(HibernateIdentityObject.class, new Long(user1.getId()));

      // 900 kilobytes
      picture = new byte[921600];
//      picture = new byte[2];
      random.nextBytes(picture);


      attr = new HibernateIdentityObjectAttribute(user1, "simple5", HibernateIdentityObjectAttribute.TYPE_BINARY);
      attr.setBinaryValue(new HibernateIdentityObjectAttributeBinaryValue(picture));
      user1.getAttributes().add(attr);

      commit();

      begin();

      session = getSessionFactory().getCurrentSession();

      user1 = (HibernateIdentityObject)session.get(HibernateIdentityObject.class, new Long(user1.getId()));

      assertEquals(5, user1.getAttributes().size());

      int binaryCount = ((Integer)session.createCriteria(HibernateIdentityObjectAttributeBinaryValue.class).
         setProjection(Projections.rowCount()).uniqueResult()).intValue();

      assertEquals(2,binaryCount);

      int attrCount = ((Integer)session.createCriteria(HibernateIdentityObjectAttribute.class).
         setProjection(Projections.rowCount()).uniqueResult()).intValue();

      assertEquals(5,attrCount);

      session.delete(user1);

      attrCount = ((Integer)session.createCriteria(HibernateIdentityObjectAttribute.class).
         setProjection(Projections.rowCount()).uniqueResult()).intValue();

      assertEquals(0,attrCount);

      binaryCount = ((Integer)session.createCriteria(HibernateIdentityObjectAttributeBinaryValue.class).
         setProjection(Projections.rowCount()).uniqueResult()).intValue();

      assertEquals(0,binaryCount);


      commit();

   }

}