/**
 * 
 */
package org.picketlink.idm.integration.jboss5;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.cfg.IdentityConfiguration;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.common.transaction.TransactionManagerProvider;
import org.picketlink.idm.common.transaction.Transactions;
import org.picketlink.idm.impl.configuration.IdentityConfigurationImpl;
import org.picketlink.idm.impl.configuration.jaxb2.JAXB2IdentityConfiguration;
import org.picketlink.idm.spi.configuration.metadata.IdentityConfigurationMetaData;
import org.picketlink.idm.spi.configuration.metadata.IdentityStoreConfigurationMetaData;


/**
 * Start the {@link IdentitySessionFactory}, and register it in the JNDI.
 * 
 * @author  Jeff Yu
 * @author  Boleslaw Dawidowicz
 * @author  Julien Viet  
 */

public class IDMService {
	
	public final static String DEFAULT_JNDI = "java:/IdentitySessionFactory";
	
	private static final String HIBERNATE_CONFIGFILE = "hibernateConfiguration";
	
	private static Logger logger = Logger.getLogger(IDMService.class.getName());

   /** Hibernate config - if specified will be used to create SessionFactory*/
   private String hibernateConfigLocation;

   /** If hibernateConfigLocation option is present created SessionFactory will be registered in IdentityConfigurationRegistry
    * with this name*/
   private String hibernateSessionFactoryRegistryName;

   /** If hibernateConfigLocation option is present created SessionFactory will be registered in JNDI with this name*/
   private String hibernateSessionFactoryJNDIName;

   /** If true checks the schema existence on start and create it if necessary. */
   private boolean doChecking;

	private String idmConfigFile;
	
	private String idmSessionFactoryJNDI;
	
	private String SQLScript;
		
	private IdentitySessionFactory identitySessionFactory;

   private SessionFactory hibernateSessionFactory;

	private String exitSQL;
	
	private String datasource;
	
	private IdentityConfiguration identityConfiguration;
	
	private TransactionManager transactionManager;

   /** The hibernate configuration object. */
   protected Configuration config;


   public IDMService(String idmConfigFile) {
		this.idmConfigFile = idmConfigFile;
	}
	
	public String getIdmConfigFile() {
		return this.idmConfigFile;
	}

	public String getIdmSessionFactoryJNDI() {		
		return idmSessionFactoryJNDI;
	}

	public String getSQLScript() {
		return this.SQLScript;
	}

	public void setIdmSessionFactoryJNDI(String idmSessionFactoryJNDI) {
		this.idmSessionFactoryJNDI = idmSessionFactoryJNDI;
	}

	public void setSQLScript(String script) {
		SQLScript = script;
	}

   public String getHibernateConfigLocation()
   {
      return hibernateConfigLocation;
   }

   public void setHibernateConfigLocation(String hibernateConfigLocation)
   {
      this.hibernateConfigLocation = hibernateConfigLocation;
   }

   public boolean isDoChecking()
   {
      return doChecking;
   }

   public void setDoChecking(boolean doChecking)
   {
      this.doChecking = doChecking;
   }

   public String getHibernateSessionFactoryRegistryName()
   {
      return hibernateSessionFactoryRegistryName;
   }

   public void setHibernateSessionFactoryRegistryName(String hibernateSessionFactoryRegistryName)
   {
      this.hibernateSessionFactoryRegistryName = hibernateSessionFactoryRegistryName;
   }

   public String getHibernateSessionFactoryJNDIName()
   {
      return hibernateSessionFactoryJNDIName;
   }

   public void setHibernateSessionFactoryJNDIName(String hibernateSessionFactoryJNDIName)
   {
      this.hibernateSessionFactoryJNDIName = hibernateSessionFactoryJNDIName;
   }

   public TransactionManager getTransactionManager() {
	   return transactionManager;
   }

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

   public void start() throws Exception {
		
	   logger.info("Starting the PicketLink IDM Management Service");
	   
	   InitialContext context = new InitialContext();
	   IdentityConfigurationMetaData metadata = JAXB2IdentityConfiguration.createConfigurationMetaData(idmConfigFile);
	   identityConfiguration = new IdentityConfigurationImpl().configure(metadata);
	   
	   if (hibernateConfigLocation != null ) {
		   deployHibernateSessionFactory(context);
	   }
	   
	   if (needToInitializeDBFromScript()) {
		  SQLPopulator sqlPopulator = new SQLPopulator(datasource, SQLScript, exitSQL);
		  sqlPopulator.populateSchema();
		}

      if (doChecking == true)
      {
			if (hibernateConfigLocation == null) {
				for (IdentityStoreConfigurationMetaData store : metadata.getIdentityStores()) {
					hibernateConfigLocation = store.getOptionSingleValue(HIBERNATE_CONFIGFILE);
				}
			}
			if (hibernateConfigLocation == null) {
				throw new Exception ("Couldn't find the hibernate configuration file");
			}
			Configuration configuration = new Configuration().configure(hibernateConfigLocation);
			logger.fine("starting to populate the schema from file [" + hibernateConfigLocation + "]");
			HibernatePopulator hibernatePopulator = new HibernatePopulator(doChecking, configuration);
			hibernatePopulator.populateSchema();
      }

      try {
    	  if (transactionManager == null) {
    		  transactionManager = TransactionManagerProvider.JBOSS_PROVIDER.getTransactionManager();
    	  }
         
         Transactions.required(transactionManager, new Transactions.Runnable()
         {
            public Object run() throws Exception
            {
               identitySessionFactory = identityConfiguration.buildIdentitySessionFactory();
               return null;
            }
         });
      } catch (Exception e) {

        throw new Exception("Cannot create IdentitySessionFactory", e); 
      }

      context.bind(getIdmSessionFactoryJNDI(), identitySessionFactory);
      
	   logger.info("Started the PicketLink IDM Management Service");
	}

	private void deployHibernateSessionFactory(InitialContext context)
			throws NamingException, IdentityException {
	   hibernateSessionFactory = new Configuration().configure(hibernateConfigLocation).buildSessionFactory();
	   if (hibernateSessionFactoryJNDIName != null) {
		   context.bind(hibernateSessionFactoryJNDIName, hibernateSessionFactory);
		   logger.info("Bind the Hibernate Session Factory in JNDI of " + hibernateSessionFactoryJNDIName);
	   }
	   if (hibernateSessionFactoryRegistryName != null) {
		   identityConfiguration.getIdentityConfigurationRegistry().register(hibernateSessionFactory, hibernateSessionFactoryRegistryName);
		   logger.info("Registered the Hibernate Session Factory in Identity Registration of " + hibernateSessionFactoryRegistryName);
	   }
	}	
   
	public void destroy() {
		
	  logger.info("Stopping the PicketLink IDM Management Service");
	  
      InitialContext context = null;
      try
      {
    	 context = new InitialContext();
    	 
         if (identitySessionFactory != null)
         {
            identitySessionFactory.close();
            if (getIdmSessionFactoryJNDI() != null)
            {
               context.unbind(getIdmSessionFactoryJNDI());
            }
         }
      }
      catch (Exception e)
      {
         logger.log(Level.SEVERE, "error in closing identitySessionFactory", e);
      }

      if (hibernateSessionFactory != null)
      {
         try
         {
            if (getHibernateSessionFactoryJNDIName() != null)
            {
               context.unbind(getHibernateSessionFactoryJNDIName());
            }

            hibernateSessionFactory.close();
         }
         catch (Exception e)
         {
        	 logger.log(Level.SEVERE, "error in closing hibernateSessionFactory", e);    	 
         }
      }
      
	   logger.info("Stopped the PicketLink IDM Management Service");
	}



	public String getExitSQL() {
		return exitSQL;
	}
	
	public void setExitSQL(String exitSQL) {
		this.exitSQL = exitSQL;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	
	
	private boolean needToInitializeDBFromScript() {
		if (this.getDatasource() != null &&
				this.getSQLScript() != null &&
				this.getExitSQL() != null) {
			return true;
		}
		return false;
	}

}
