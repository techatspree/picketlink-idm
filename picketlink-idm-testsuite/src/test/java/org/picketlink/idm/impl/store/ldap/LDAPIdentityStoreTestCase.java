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

package org.picketlink.idm.impl.store.ldap;

import org.picketlink.idm.impl.LDAPTestPOJO;
import org.picketlink.idm.impl.configuration.IdentityConfigurationImpl;
import org.picketlink.idm.impl.configuration.IdentityStoreConfigurationContextImpl;
import org.picketlink.idm.impl.configuration.jaxb2.JAXB2IdentityConfiguration;
import org.picketlink.idm.impl.store.CommonIdentityStoreTest;
import org.picketlink.idm.impl.store.IdentityStoreTestContext;
import org.picketlink.idm.spi.configuration.IdentityConfigurationContextRegistry;
import org.picketlink.idm.spi.configuration.IdentityStoreConfigurationContext;
import org.picketlink.idm.spi.configuration.metadata.IdentityConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityStoreConfigurationMetaData;
import org.picketlink.idm.spi.store.IdentityStore;
import org.picketlink.idm.spi.store.IdentityStoreInvocationContext;
import org.picketlink.idm.spi.store.IdentityStoreSession;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;


/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class LDAPIdentityStoreTestCase extends LDAPTestPOJO implements IdentityStoreTestContext
{

   CommonIdentityStoreTest commonTest;

   IdentityStoreInvocationContext ctx;

   IdentityStore store;

   public LDAPIdentityStoreTestCase()
   {

   }

   public void setUp() throws Exception
   {
      super.start();

//      setIdentityConfig("store-test-config.xml");

      commonTest = new CommonIdentityStoreTest(this);

      IdentityConfigurationMetaData configurationMD = JAXB2IdentityConfiguration
         .createConfigurationMetaData(getIdentityConfig());

      IdentityConfigurationContextRegistry registry = (IdentityConfigurationContextRegistry) new IdentityConfigurationImpl().
         configure(configurationMD);

      IdentityStoreConfigurationMetaData storeMD = null;

      for (IdentityStoreConfigurationMetaData metaData : configurationMD.getIdentityStores())
      {
         if (metaData.getId().equals("LDAP Identity Store"))
         {
            storeMD = metaData;
            break;
         }
      }

      IdentityStoreConfigurationContext context = new IdentityStoreConfigurationContextImpl(configurationMD, registry, storeMD);



      //populate();

      store = new LDAPIdentityStoreImpl("LDAP Identity Store");

      store.bootstrap(context);

      final IdentityStoreSession storeSession = store.createIdentityStoreSession();

      ctx = new IdentityStoreInvocationContext()
      {
         public IdentityStoreSession getIdentityStoreSession()
         {
            return storeSession;
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

   public void tearDown() throws Exception
   {
      super.stop();
   }


   public void flush() throws Exception
   {
      //nothing
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

//       Just test if OpenDS is running and was populated...
   public void testSimple() throws Exception
   {
      populateClean();

      Hashtable<String,String> env = new Hashtable<String,String>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, directoryConfig.getContextFactory());
      env.put(Context.PROVIDER_URL, directoryConfig.getDescription());
      env.put(Context.SECURITY_AUTHENTICATION, "simple");
      env.put(Context.SECURITY_PRINCIPAL, directoryConfig.getAdminDN());
      env.put(Context.SECURITY_CREDENTIALS, directoryConfig.getAdminPassword());

      LdapContext ldapCtx = null;
      try
      {
         ldapCtx = new InitialLdapContext(env, null);

//          Do something ...
         System.out.println("Attributes: " + ldapCtx.getAttributes(directoryConfig.getCleanUpDN()));

      }
      catch (NamingException e)
      {
         e.printStackTrace();
      }
      finally
      {
         try
         {
            if (ldapCtx != null)
            {
               ldapCtx.close();
            }
         }
         catch (NamingException e)
         {
            e.printStackTrace();
         }
      }
   }

//   @Test
//   public void testIdentityObjectCount() throws Exception
//   {
//      populate();
//
//      assertEquals(7, store.getIdentityObjectsCount(ctx, IdentityTypeEnum.USER));
//      assertEquals(5, store.getIdentityObjectsCount(ctx, IdentityTypeEnum.ROLE));
//      assertEquals(2, store.getIdentityObjectsCount(ctx, IdentityTypeEnum.GROUP));
//   }
//
//   @Test
//   public void testFindCreateRemove() throws Exception
//   {
//      populate();
//
//      assertEquals(7, store.getIdentityObjectsCount(ctx, IdentityTypeEnum.USER));
//
//      IdentityObject io = store.findIdentityObject(ctx, "admin", IdentityTypeEnum.USER);
//      assertEquals("admin", io.getName());
//      assertEquals("uid=admin,ou=People,o=test,dc=portal,dc=example,dc=com", io.getKey().toString());
//
//      //
//
//      store.removeIdentityObject(ctx, io);
//
//      assertEquals(6, store.getIdentityObjectsCount(ctx, IdentityTypeEnum.USER));
//
//      store.createIdentityObject(ctx, "newUserA", IdentityTypeEnum.USER);
//
//      assertEquals(7, store.getIdentityObjectsCount(ctx, IdentityTypeEnum.USER));
//
//      //
//
//      assertEquals(2, store.getIdentityObjectsCount(ctx, IdentityTypeEnum.GROUP));
//
//      store.createIdentityObject(ctx, "newGroupA", IdentityTypeEnum.GROUP);
//
//      assertEquals(3, store.getIdentityObjectsCount(ctx, IdentityTypeEnum.GROUP));
//
//      //
//
//      io = store.findIdentityObject(ctx, "cn=newGroupA,ou=Groups,o=test,dc=portal,dc=example,dc=com");
//      assertEquals("newGroupA", io.getName());
//
//   }

//   @Test
//   public void testAttributes() throws Exception{
//
//      populate();
//
//      IdentityObject user1 = store.createIdentityObject(ctx, "Adam", IdentityTypeEnum.USER);
//      IdentityObject user2 = store.createIdentityObject(ctx, "Eva", IdentityTypeEnum.USER);
//
//      //
//
//      IdentityObjectAttribute[] attrs = new IdentityObjectAttribute[]{
//         new SimpleAttribute("phone", new String[]{"val1"}),
//         new SimpleAttribute("description", new String[]{"val1", "val2", "val3", "val4"}),
//
//      };
//
//      store.addAttributes(ctx, user1, attrs);
//
//      //
//
//      Map<String, IdentityObjectAttribute> persistedAttrs = store.getAttributes(ctx, user1);
//
//      assertEquals(2, persistedAttrs.keySet().size());
//
//      assertTrue(persistedAttrs.containsKey("phone"));
//      assertEquals(1, persistedAttrs.get("phone").getSize());
//
//      assertTrue(persistedAttrs.containsKey("description"));
//      assertEquals(4, persistedAttrs.get("description").getSize());
//
//      //
//
//      attrs = new IdentityObjectAttribute[]{
//         new SimpleAttribute("carLicense", new String[]{"val1"})
//      };
//
//      store.addAttributes(ctx, user1, attrs);
//
//      //
//
//      persistedAttrs = store.getAttributes(ctx, user1);
//
//      assertEquals(3, persistedAttrs.keySet().size());
//
//      assertTrue(persistedAttrs.containsKey("phone"));
//      assertEquals(1, persistedAttrs.get("phone").getSize());
//
//      assertTrue(persistedAttrs.containsKey("description"));
//      assertEquals(4, persistedAttrs.get("description").getSize());
//
//      assertTrue(persistedAttrs.containsKey("carLicense"));
//      assertEquals(1, persistedAttrs.get("carLicense").getSize());
//
//      attrs = new IdentityObjectAttribute[]{
//         new SimpleAttribute("carLicense", new String[]{"val2"})
//      };
//
//      store.addAttributes(ctx, user1, attrs);
//
//      //
//
//      persistedAttrs = store.getAttributes(ctx, user1);
//
//      assertEquals(3, persistedAttrs.keySet().size());
//
//      assertTrue(persistedAttrs.containsKey("carLicense"));
//      assertEquals(2, persistedAttrs.get("carLicense").getSize());
//
//      //
//
//      store.updateAttributes(ctx, user1, attrs);
//
//      //
//
//      persistedAttrs = store.getAttributes(ctx, user1);
//
//      assertEquals(3, persistedAttrs.keySet().size());
//
//      assertTrue(persistedAttrs.containsKey("carLicense"));
//      assertEquals(1, persistedAttrs.get("carLicense").getSize());
//
//      //
//
//      String[] names = new String[]{"carLicense"};
//      store.removeAttributes(ctx, user1, names);
//
//      //
//
//      persistedAttrs = store.getAttributes(ctx, user1);
//
//      assertEquals(2, persistedAttrs.keySet().size());
//
//   }

   public void testRelationships() throws Exception
   {
      populateClean();

      commonTest.testRelationships();

   }

   public void testStorePersistence() throws Exception
   {
      populateClean();

      commonTest.testStorePersistence();

   }

   public void testFindMethods() throws Exception
   {
     populateClean();

     commonTest.testFindMethods();

   }

   public void testCriteria() throws Exception
   {
      populateClean();

      commonTest.testCriteria();
   }


   public void testCredentials() throws Exception
   {
      populateClean();

      commonTest.testPasswordCredential();
   }

}
