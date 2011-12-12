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

import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectAttribute;
import org.picketlink.idm.spi.store.IdentityStoreInvocationContext;
import org.picketlink.idm.spi.store.IdentityStore;
import org.picketlink.idm.spi.store.IdentityStoreSession;
import org.picketlink.idm.spi.configuration.metadata.IdentityStoreConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityConfigurationMetaData;
import org.picketlink.idm.spi.configuration.IdentityStoreConfigurationContext;
import org.picketlink.idm.spi.configuration.IdentityConfigurationContextRegistry;
import org.picketlink.idm.impl.store.hibernate.HibernateIdentityStoreImpl;
import org.picketlink.idm.impl.store.IdentityTypeEnum;
import org.picketlink.idm.impl.store.CommonIdentityStoreTest;
import org.picketlink.idm.impl.store.IdentityStoreTestContext;
import org.picketlink.idm.impl.configuration.jaxb2.JAXB2IdentityConfiguration;
import org.picketlink.idm.impl.configuration.IdentityStoreConfigurationContextImpl;
import org.picketlink.idm.impl.configuration.IdentityConfigurationImpl;
import org.picketlink.idm.impl.api.SimpleAttribute;
import org.picketlink.idm.impl.IdentityTestPOJO;
import org.picketlink.idm.common.exception.IdentityException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Map;
import java.util.Random;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class HibernateIdentityStoreTestCase extends IdentityTestPOJO implements IdentityStoreTestContext
{

   protected HibernateIdentityStoreImpl store;

   protected IdentityStoreInvocationContext ctx;

   protected CommonIdentityStoreTest commonTest;

   public HibernateIdentityStoreTestCase()
   {

   }

   public void setUp() throws Exception
   {
      super.start();

      setIdentityConfig("store-test-config.xml");

      commonTest = new CommonIdentityStoreTest(this);

      IdentityConfigurationMetaData configurationMD = JAXB2IdentityConfiguration
         .createConfigurationMetaData(getIdentityConfig());

      IdentityConfigurationContextRegistry identityConfiguration = (IdentityConfigurationContextRegistry)
         new IdentityConfigurationImpl().configure(configurationMD);

      IdentityStoreConfigurationMetaData storeMD = null;

      for (IdentityStoreConfigurationMetaData metaData : configurationMD.getIdentityStores())
      {
         if (metaData.getId().equals("HibernateTestStore"))
         {
            storeMD = metaData;
            break;
         }
      }

      IdentityStoreConfigurationContext context =
         new IdentityStoreConfigurationContextImpl(configurationMD, identityConfiguration, storeMD);

      store = new HibernateIdentityStoreImpl("HibernateTestStore")
      {

         @Override
         protected Session getHibernateSession(IdentityStoreInvocationContext ctx)
         {
            return getHibernateSupport().getSessionFactory().getCurrentSession();
         }

         @Override
         protected SessionFactory bootstrapHibernateSessionFactory(IdentityStoreConfigurationContext configurationContext) throws IdentityException
         {
            return getHibernateSupport().getSessionFactory();
         }

      };

      store.bootstrap(context);


      ctx = new IdentityStoreInvocationContext()
      {
         public IdentityStoreSession getIdentityStoreSession()
         {
            return null;
         }

         public String getRealmId()
         {
            return "testRealm";
         }

         public String getSessionId()
         {
            return "";
         }
      };


   }

   @Override
   protected void tearDown() throws Exception
   {
      super.stop();
   }

   public void begin()
   {
      getHibernateSupport().openSession();
   }

   public void commit()
   {
      assertTrue(getHibernateSupport().commitTransaction());
   }

   public void flush() throws Exception
   {

     getHibernateSupport().getSessionFactory().getCurrentSession().flush();
   }

   public IdentityStore getStore()
   {
      return store;
   }

   public IdentityStoreInvocationContext getCtx()
   {
      return ctx;
   }

   // Tests

   public void testStorePersistence() throws Exception
   {

      commonTest.testStorePersistence();

   }

   public void testFindMethods() throws Exception
   {

     commonTest.testFindMethods();

   }

   public void testBinaryAttributes() throws Exception
   {
      begin();

      IdentityObject user1 = store.createIdentityObject(ctx, "Adam", IdentityTypeEnum.USER);
      IdentityObject user2 = store.createIdentityObject(ctx, "Eva", IdentityTypeEnum.USER);

      flush();

      IdentityObjectAttribute[] attrs = new IdentityObjectAttribute[]{
         new SimpleAttribute("key1", new String[]{"val1", "val2", "val3"}),
         new SimpleAttribute("key2", new String[]{"val1", "val2", "val3", "val4"})
      };

      store.addAttributes(ctx, user1, attrs);

      Random random = new Random();

      // 900 kilobytes
      byte[] picture1 = new byte[921600];
      random.nextBytes(picture1);
      byte[] picture2 = new byte[921600];
      random.nextBytes(picture2);

      attrs = new IdentityObjectAttribute[]{
         new SimpleAttribute("key1", new byte[][]{picture1}),
      };

      store.addAttributes(ctx, user1, attrs);

      attrs = new IdentityObjectAttribute[]{
         new SimpleAttribute("key2", new byte[][]{picture1, picture2}),
      };

      store.addAttributes(ctx, user1, attrs);

      attrs = new IdentityObjectAttribute[]{
         new SimpleAttribute("key1", new byte[][]{picture2}),
      };

      store.updateAttributes(ctx, user1, attrs);

      store.removeAttributes(ctx, user1, new String[]{"key1", "key2"});

      assertNull(store.findIdentityObjectByUniqueAttribute(ctx, IdentityTypeEnum.USER, new SimpleAttribute("key1", new String[]{"toto"})));


      commit();
   }

   public void testAttributes() throws Exception
   {

      begin();

      IdentityObject user1 = store.createIdentityObject(ctx, "Adam", IdentityTypeEnum.USER);
      IdentityObject user2 = store.createIdentityObject(ctx, "Eva", IdentityTypeEnum.USER);

      flush();

      IdentityObjectAttribute[] attrs = new IdentityObjectAttribute[]{
         new SimpleAttribute("key1", new String[]{"val1", "val2", "val3"}),
         new SimpleAttribute("key2", new String[]{"val1", "val2", "val3", "val4"})
      };

      store.addAttributes(ctx, user1, attrs);

      flush();

      Map<String, IdentityObjectAttribute> persistedAttrs = store.getAttributes(ctx, user1);

      assertEquals(2, persistedAttrs.keySet().size());

      assertTrue(persistedAttrs.containsKey("key1"));
      assertEquals(3, persistedAttrs.get("key1").getSize());

      assertTrue(persistedAttrs.containsKey("key2"));
      assertEquals(4, persistedAttrs.get("key2").getSize());

      flush();

      attrs = new IdentityObjectAttribute[]{
         new SimpleAttribute("key3", new String[]{"val1"})
      };


      store.addAttributes(ctx, user1, attrs);

      flush();

      persistedAttrs = store.getAttributes(ctx, user1);

      assertEquals(3, persistedAttrs.keySet().size());

      assertTrue(persistedAttrs.containsKey("key1"));
      assertEquals(3, persistedAttrs.get("key1").getSize());

      assertTrue(persistedAttrs.containsKey("key2"));
      assertEquals(4, persistedAttrs.get("key2").getSize());

      assertTrue(persistedAttrs.containsKey("key3"));
      assertEquals(1, persistedAttrs.get("key3").getSize());

      flush();

      attrs = new IdentityObjectAttribute[]{
         new SimpleAttribute("key3", new String[]{"val2"})
      };

      store.addAttributes(ctx, user1, attrs);

      flush();

      persistedAttrs = store.getAttributes(ctx, user1);

      assertEquals(3, persistedAttrs.keySet().size());

      assertTrue(persistedAttrs.containsKey("key3"));
      assertEquals(2, persistedAttrs.get("key3").getSize());

      flush();

      store.updateAttributes(ctx, user1, attrs);

      flush();

      persistedAttrs = store.getAttributes(ctx, user1);

      assertEquals(3, persistedAttrs.keySet().size());

      assertTrue(persistedAttrs.containsKey("key3"));
      assertEquals(1, persistedAttrs.get("key3").getSize());

      flush();

       attrs = new IdentityObjectAttribute[]{
         new SimpleAttribute("key4", new String[]{"val2"})
      };

      store.updateAttributes(ctx, user1, attrs);

      flush();

      persistedAttrs = store.getAttributes(ctx, user1);

      assertEquals(4, persistedAttrs.keySet().size());
      assertEquals("val2", persistedAttrs.get("key4").getValue().toString());


      store.removeAttributes(ctx, user1, new String[] {"key3"});

      flush();

      persistedAttrs = store.getAttributes(ctx, user1);

      assertEquals(3, persistedAttrs.keySet().size());


      commit();

   }

   public void testRelationships() throws Exception
   {
      commonTest.testRelationships();
   }

   public void testPasswordCredentials() throws Exception
   {
      commonTest.testPasswordCredential();
   }

   public void testBinaryCredentials() throws Exception
   {
      commonTest.testBinaryCredential();
   }

   public void testCriteria() throws Exception
   {
      commonTest.testCriteria();
   }

}