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

import org.picketlink.idm.impl.model.hibernate.HibernateIdentityObjectRelationshipType;
import org.picketlink.idm.impl.model.hibernate.HibernateIdentityObjectType;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class HibernateTestSupport
{
   private static Logger logger = Logger.getLogger(HibernateTestSupport.class.getName());

   protected SessionFactory sessionFactory;

   private Connection connection;

   private final String databaseName;

   private final String hibernateConfiguration;

   public HibernateTestSupport(String databaseName, String hibernateConfiguration)
   {
      this.databaseName = databaseName;
      this.hibernateConfiguration = hibernateConfiguration;
   }

   public SessionFactory getSessionFactory()
   {
      return sessionFactory;
   }


   public void start() throws Exception {
      try {
         logger.info("Starting in-memory HSQL database for unit tests");
         Class.forName("org.hsqldb.jdbcDriver");
         connection = DriverManager.getConnection("jdbc:hsqldb:mem:" + databaseName, "sa", "");
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
         logger.fine("Exception during HSQL database startup.");
         throw ex;
      }
      try
      {
         logger.info("Building JPA EntityManager for unit tests");
         sessionFactory = new Configuration().configure(hibernateConfiguration).buildSessionFactory();
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
         logger.fine("Exception during JPA EntityManager instanciation.");
         throw ex;
      }
   }

   public void stop() throws Exception {
      logger.info("Shuting down Hibernate JPA layer.");
      if (sessionFactory != null)
      {
         sessionFactory.close();
      }

      logger.info("Stopping in-memory HSQL database.");
      try
      {
         connection.createStatement().execute("SHUTDOWN");
      }
      catch (Exception ex) {
         throw ex;
      }
   }

   public void populateObjectTypes(String[] typeNames) throws Exception
   {

      sessionFactory.getCurrentSession().getTransaction().begin();

      for (String typeName : typeNames)
      {
         HibernateIdentityObjectType hibernateType = new HibernateIdentityObjectType(typeName);
         sessionFactory.getCurrentSession().persist(hibernateType);
      }

      sessionFactory.getCurrentSession().getTransaction().commit();

   }

   public void populateRelationshipTypes(String[] typeNames) throws Exception
   {

      sessionFactory.getCurrentSession().getTransaction().begin();

      for (String typeName : typeNames)
      {
         HibernateIdentityObjectRelationshipType hibernateType = new HibernateIdentityObjectRelationshipType(typeName);
         sessionFactory.getCurrentSession().persist(hibernateType);
      }

      sessionFactory.getCurrentSession().getTransaction().commit();
   }


}
