/**
 * 
 */
package org.picketlink.idm.integration.jboss5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

/**
 * 
 * Using the Hibernate built-in SchemaExport.
 * 
 * @author  Jeff Yu
 * @author  Boleslaw Dawidowicz
 * @author  Julien Viet
 *
 */
public class HibernatePopulator {
	
	private static Logger logger = Logger.getLogger(HibernatePopulator.class.getName());
	
	/** doCheck result - schema ok */
	private static final int RESULT_NONE = 0;

	/** doCheck result - schema need updates */
	private static final int RESULT_UPDATE = 1;

	/** doCheck result - schema not exist */
    private static final int RESULT_CREATE = 2;
	
	private boolean doChecking;
	
	private Configuration config;
	
	private SessionFactory sessionFactory;
	
	public HibernatePopulator(boolean doChecking, Configuration config) {
		this.doChecking  = doChecking;
		this.config = config;		
	}
	
	
	public void populateSchema()  {
		sessionFactory = config.buildSessionFactory();
		
	     if (doChecking)
	     {
	        //check the schema
	        int check = doCheck();
	        switch (check)
	        {
	           case RESULT_NONE:
	              break;
	           case RESULT_UPDATE:
	              updateSchema();
	              break;
	           case RESULT_CREATE:
	              createSchema();
	              break;
	        }
	     } else {
	    	 createSchema();
	     }
	     
	     sessionFactory.close();
   }
	
	
    private int doCheck()
    {
      Session session = null;
      int numOfChecks = 0;
      int bad = 0;
      try
      {
         session = sessionFactory.openSession();
         Collection<ClassMetadata> values = sessionFactory.getAllClassMetadata().values();
         numOfChecks = values.size();
         for (Iterator<ClassMetadata> i = values.iterator(); i.hasNext();)
         {
            ClassMetadata cmd = (ClassMetadata)i.next();
            Query query = session.createQuery("from " + cmd.getEntityName());
            query.setFirstResult(0);
            query.setMaxResults(0);
            try
            {
               query.list();
            }
            catch (SQLGrammarException e)
            {
               // We consider that exception means that the schema does not exist
               bad++;
            }
         }
      }
      finally
      {
         sessionFactory.close();
      }
      // There was no sql grammar exception - schema is ok!
      if (bad == 0)
      {
         logger.fine("The schema was checked as valid");
         //do nothing
         return RESULT_NONE;
      }
      // There is no existing valid schema;
      else if (bad == numOfChecks)
      {
         logger.fine("The schema was checked as not exists");
         // Totaly invalid schema
         return RESULT_CREATE;
      }
      // Schema is partialy corrupted
      else if (bad < numOfChecks)
      {
         // Schema needs updates;
         logger.fine("The schema was checked as need updates");
         return RESULT_UPDATE;
      }

      // If here something gone wrong...
      logger.fine("The schema was checked as need to be created");
      return RESULT_CREATE;
   }
	
    private void createSchema()
    {
      logger.fine("Creating database schema");
      SchemaExport export = new SchemaExport(config);
      export.create(false, true);
    }
   
    private void destroySchema()
    {
       logger.fine("Destroying database schema");
       SchemaExport export = new SchemaExport(config);
       export.drop(false, true);
    }
    
    private void updateSchema()
    {
       logger.fine("Updating database schema");
       SchemaUpdate update = new SchemaUpdate(config);
       update.execute(false, true);
    }

     private String getString(InputStream in) {
    	try { 
	    	ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
	        byte[] buffer = new byte[512];
	        while (true)
	        {
	           int i = in.read(buffer);
	           if (i == 0)
	           {
	              continue;
	           }
	           if (i == -1)
	           {
	              break;
	           }
	           out.write(buffer, 0, i);
	        }
	        return out.toString("UTF-8");
    	} catch (IOException e) {
        	logger.log(Level.SEVERE, "error in converting inputstream into string", e);
        	return null;
        } finally {
        	if (in != null) {
        		try {
					in.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "error in closing inputstream", e);
				}
        	}
        }    	
     }
     
}
