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

package org.picketlink.idm.impl;

import org.jboss.portal.test.framework.embedded.ConnectionManagerSupport;
import org.jboss.portal.test.framework.embedded.DataSourceSupport;
import org.jboss.portal.test.framework.embedded.HibernateSupport;
import org.jboss.portal.test.framework.embedded.JNDISupport;
import org.jboss.portal.test.framework.embedded.TransactionManagerSupport;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import org.hibernate.SessionFactory;


/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class IdentityTestPOJO extends TestCase
{

   protected String identityConfig = "test-identity-config.xml";

   private String realmName;

   private String dataSourceName = "hsqldb";

   private String hibernateConfig = "datasources/hibernates.xml";

   private String datasources = "datasources/datasources.xml";

   private JNDISupport jndiSupport;

   protected TransactionManagerSupport transactonManagerSupport;

   protected ConnectionManagerSupport connectionManagerSupport;

   protected DataSourceSupport dataSourceSupport;

   protected HibernateSupport hibernateSupport;



   public void start() throws Exception
   {
      overrideFromProperties();

      jndiSupport = new JNDISupport();
      jndiSupport.start();
      transactonManagerSupport = new TransactionManagerSupport();
      transactonManagerSupport.start();
      connectionManagerSupport = new ConnectionManagerSupport();
      connectionManagerSupport.setTransactionManager(transactonManagerSupport.getTransactionManager());
      connectionManagerSupport.start();

      

      DataSourceSupport.Config dataSourceConfig = DataSourceSupport.Config.obtainConfig(datasources, dataSourceName);

      HibernateSupport.Config hibernateSupportConfig = HibernateSupport.getConfig(dataSourceName, hibernateConfig);

      dataSourceSupport = new DataSourceSupport();
      dataSourceSupport.setTransactionManager(transactonManagerSupport.getTransactionManager());
      dataSourceSupport.setConnectionManagerReference(connectionManagerSupport.getConnectionManagerReference());
      dataSourceSupport.setConfig(dataSourceConfig);
      dataSourceSupport.start();

//      hibernateSupport = new HibernateAnnotationsSupport();
      hibernateSupport = new HibernateSupport();
      hibernateSupport.setConfig(hibernateSupportConfig);
      hibernateSupport.setJNDIName("java:/jbossidentity/HibernateStoreSessionFactory");

      String prefix = "mappings/";

      //Sybase support hack
      if (dataSourceName.startsWith("sybase-"))
      {
         prefix = "sybase-mappings/";
      }

      List<String> mappings = new LinkedList<String>();
      mappings.add(prefix + "HibernateIdentityObject.hbm.xml");
      mappings.add(prefix + "HibernateIdentityObjectCredentialBinaryValue.hbm.xml");
      mappings.add(prefix + "HibernateIdentityObjectAttributeBinaryValue.hbm.xml");
      mappings.add(prefix + "HibernateIdentityObjectAttribute.hbm.xml");
      mappings.add(prefix + "HibernateIdentityObjectCredential.hbm.xml");
      mappings.add(prefix + "HibernateIdentityObjectCredentialType.hbm.xml");
      mappings.add(prefix + "HibernateIdentityObjectRelationship.hbm.xml");
      mappings.add(prefix + "HibernateIdentityObjectRelationshipName.hbm.xml");
      mappings.add(prefix + "HibernateIdentityObjectRelationshipType.hbm.xml");
      mappings.add(prefix + "HibernateIdentityObjectType.hbm.xml");
      mappings.add(prefix + "HibernateRealm.hbm.xml");

      hibernateSupport.setMappings(mappings);

      hibernateSupport.start();

      
   }

   public void stop() throws Exception
   {
      hibernateSupport.getSessionFactory().getStatistics().logSummary();
      hibernateSupport.stop();
      dataSourceSupport.stop();
      connectionManagerSupport.stop();
      transactonManagerSupport.stop();
      jndiSupport.stop();


   }

   public void overrideFromProperties() throws Exception
   {
      String dsName = System.getProperties().getProperty("dataSourceName");

      if (dsName != null && !dsName.startsWith("$"))
      {
         setDataSourceName(dsName);
      }

   }

   public SessionFactory getSessionFactory()
   {
      return getHibernateSupport().getSessionFactory();
   }

   public void setDataSourceName(String dataSourceName)
   {
      this.dataSourceName = dataSourceName;
   }

   public void setHibernateConfig(String hibernateConfig)
   {
      this.hibernateConfig = hibernateConfig;
   }

   public void setDatasources(String datasources)
   {
      this.datasources = datasources;
   }

   public void setIdentityConfig(String identityConfig)
   {
      this.identityConfig = identityConfig;
   }

   public void setRealmName(String realmName)
   {
      this.realmName = realmName;
   }

   public String getDataSourceName()
   {
      return dataSourceName;
   }

   public String getHibernateConfig()
   {
      return hibernateConfig;
   }

   public String getDatasources()
   {
      return datasources;
   }

   public String getIdentityConfig()
   {
      return identityConfig;
   }

   public String getRealmName()
   {
      return realmName;
   }

   public HibernateSupport getHibernateSupport()
   {
      return hibernateSupport;
   }

   public void begin()
   {
      getHibernateSupport().openSession();
   }

   public void commit()
   {
      assertTrue(getHibernateSupport().commitTransaction());
   }




}
