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
import org.hibernate.Query;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;


/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class HibernateModelTestCase extends IdentityTestPOJO
{
   public HibernateModelTestCase()
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

   public void testPersistence() {


      begin();
      Session session = getSessionFactory().getCurrentSession();


      HibernateRealm defaultRealm = new HibernateRealm("default");
      session.persist(defaultRealm);

      Map<String, String> props = new HashMap<String, String>();
      props.put("test", "testValue");
      defaultRealm.setProperties(props);

      defaultRealm = (HibernateRealm)session.get(HibernateRealm.class, defaultRealm.getId());

      assertEquals(defaultRealm.getProperties().keySet().size(), 1);

      //

      HibernateIdentityObjectType iot = new HibernateIdentityObjectType();
      iot.setName("User");

      session.persist(iot);
      assertTrue(session.contains(iot));


      HibernateIdentityObject io = new HibernateIdentityObject();
      io.setName("John Kowalski");
      io.setRealm(defaultRealm);
      io.setIdentityType(iot);

      session.persist(io);
      assertTrue(session.contains(io));

      session.delete(io);
      assertFalse(session.contains(io));

      //



      //

      HibernateIdentityObjectRelationshipType iort = new HibernateIdentityObjectRelationshipType();
      iort.setName("Member");

      session.persist(iort);
      assertTrue(session.contains(iort));

      assertTrue(session.contains(iort));

      session.delete(iort);
      assertFalse(session.contains(iort));

      commit();


   }

   public void testRelationships()
   {
      begin();


      Session session = getSessionFactory().getCurrentSession();


      HibernateRealm realm = new HibernateRealm("default");
      session.persist(realm);

      HibernateIdentityObjectType groupType = new HibernateIdentityObjectType("Group");
      session.persist(groupType);
      HibernateIdentityObjectType userType = new HibernateIdentityObjectType("User");
      session.persist(userType);

      HibernateIdentityObject user1 = new HibernateIdentityObject("user1", userType, realm);
      session.persist(user1);
      HibernateIdentityObject user2 = new HibernateIdentityObject("user2", userType, realm);
      session.persist(user2);
      HibernateIdentityObject user3 = new HibernateIdentityObject("user3", userType, realm);
      session.persist(user3);

      HibernateIdentityObject group1 = new HibernateIdentityObject("group1", groupType, realm);
      session.persist(group1);
      HibernateIdentityObject group2 = new HibernateIdentityObject("group2", groupType, realm);
      session.persist(group2);
      HibernateIdentityObject group3 = new HibernateIdentityObject("group3", groupType, realm);
      session.persist(group3);

      HibernateIdentityObjectRelationshipType memberType = new HibernateIdentityObjectRelationshipType("member");
      session.persist(memberType);

      HibernateIdentityObjectRelationship relationship = new HibernateIdentityObjectRelationship(memberType, group1, user1);
      session.persist(relationship);
      relationship = new HibernateIdentityObjectRelationship(memberType, group1, user2);
      session.persist(relationship);
      relationship = new HibernateIdentityObjectRelationship(memberType, group2, user3);
      session.persist(relationship);
      relationship = new HibernateIdentityObjectRelationship(memberType, group2, group1);
      session.persist(relationship);

      commit();

      //


      begin();

      session = getSessionFactory().getCurrentSession();

      Query ioQuery = session.createQuery("select o from HibernateIdentityObject o where o.name like :name");

      group1 = (HibernateIdentityObject)ioQuery.setParameter("name", "group1").uniqueResult();
      assertEquals(2, group1.getFromRelationships().size());
      assertEquals(1, group1.getToRelationships().size());
      group2 = (HibernateIdentityObject)ioQuery.setParameter("name", "group2").uniqueResult();
      assertEquals(2, group2.getFromRelationships().size());
      assertEquals(0, group2.getToRelationships().size());

      user3 = (HibernateIdentityObject)ioQuery.setParameter("name", "user3").uniqueResult();
      assertEquals(1, user3.getToRelationships().size());
      assertEquals(0, user3.getFromRelationships().size());
      assertEquals("group2", user3.getToRelationships().iterator().next().getFromIdentityObject().getName());
      commit();

   }

   public void testNameTypeConstraint() throws Exception
   {

      begin();
      Session session = getSessionFactory().getCurrentSession();



      HibernateRealm realm = new HibernateRealm("default");
      session.persist(realm);

      HibernateIdentityObjectType groupType = new HibernateIdentityObjectType("Group");
      session.persist(groupType);
      HibernateIdentityObjectType userType = new HibernateIdentityObjectType("User");
      session.persist(userType);

      HibernateIdentityObject user1 = new HibernateIdentityObject("user1", userType, realm);
      session.persist(user1);
      HibernateIdentityObject user2 = new HibernateIdentityObject("user2", userType, realm);
      session.persist(user2);

      commit();



      begin();

      session = getSessionFactory().getCurrentSession();

      try
      {

         HibernateIdentityObject user3 = new HibernateIdentityObject("user2", userType, realm);
         session.persist(user3);

         assertFalse(getHibernateSupport().commitTransaction());

      }
      catch (Exception e)
      {
         // may be expected
         getHibernateSupport().rollbackTransaction();
      }



      begin();

      session = getSessionFactory().getCurrentSession();


      HibernateIdentityObject user4 = new HibernateIdentityObject("group1", userType, realm);
      session.persist(user4);


      HibernateIdentityObject group1 = new HibernateIdentityObject("group1", groupType, realm);
      session.persist(group1);
      HibernateIdentityObject group2 = new HibernateIdentityObject("group2", groupType, realm);
      session.persist(group2);

      //session.flush();

      try
      {

         HibernateIdentityObject group3 = new HibernateIdentityObject("group2", groupType, realm);
         session.persist(group3);

         // Should fail
         assertFalse(getHibernateSupport().commitTransaction());
      }
      catch (Exception e)
      {
         //expected
         getHibernateSupport().rollbackTransaction();
      }



      begin();

      session = getSessionFactory().getCurrentSession();

      HibernateIdentityObject group4 = new HibernateIdentityObject("user1", groupType, realm);
      session.persist(group4);

      session.flush();

      commit();





   }

   public void testAttributes() throws Exception
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
      attr = new HibernateIdentityObjectAttribute(user1, "simple2", HibernateIdentityObjectAttribute.TYPE_TEXT);
      attr.setTextValues(values1);
      user1.getAttributes().add(attr);







      commit();



      begin();

      session = getSessionFactory().getCurrentSession();

      user1 = (HibernateIdentityObject)session.get(HibernateIdentityObject.class, new Long(user1.getId()));
      assertEquals(2, user1.getAttributes().size() );
//      assertNotNull(user1.getProfileAttributes().get("simple1"));
//      assertEquals(3, user1.getProfileAttributes().get("simple1").length);


      commit();

   }

}