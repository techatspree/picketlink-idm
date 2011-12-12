/**
 * 
 */
package org.picketlink.idm.integration.jboss5;

import javax.transaction.TransactionManager;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.vfs.spi.deployer.AbstractSimpleVFSRealDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.picketlink.idm.integration.jboss5.jaxb2.InitializerType;
import org.picketlink.idm.integration.jboss5.jaxb2.PicketlinkIDMDeployerType;


/**
 * 
 * Deploy the JBoss identity Management:
 * 
 * 1. Start the IdentitySessionFactory.
 * 2. bind it into the JNDI.
 * 
 * @author  Jeff Yu
 *
 */
public class IDMDeployer extends AbstractSimpleVFSRealDeployer<IDMMetadata> {
	
	private TransactionManager transactionManager;
	
	private IDMService service;
	
	public IDMDeployer() {
		super(IDMMetadata.class);
	}
	
	@Override
	public void deploy(VFSDeploymentUnit deploymentUnit, IDMMetadata metadata) throws DeploymentException {
		PicketlinkIDMDeployerType config = metadata.getDeploperType();
		service = new IDMService(config.getIdmConfigFile());
		if (transactionManager != null) {
			service.setTransactionManager(transactionManager);
		}
		if (config.getJNDIName() != null) {
			service.setIdmSessionFactoryJNDI(config.getJNDIName());
		}
		if (config.getHibernateDeployer() != null) {
			service.setHibernateConfigLocation(config.getHibernateDeployer().getHibernateConfiguration());
			service.setHibernateSessionFactoryJNDIName(config.getHibernateDeployer().getHibernateSessionFactoryJNDIName());
			service.setHibernateSessionFactoryRegistryName(config.getHibernateDeployer().getHibernateSessionFactoryRegistryName());
		}
		
		if (config.getInitializers() != null) {
			InitializerType initializers = config.getInitializers();
			if (initializers.getDatasource() != null) {
				service.setDatasource(initializers.getDatasource());
			}
			if (initializers.getSqlInitializer() != null) {
				service.setSQLScript(initializers.getSqlInitializer().getSqlFile());
				service.setExitSQL(initializers.getSqlInitializer().getExitSQL());
			}
			if (initializers.getHibernateInitializer() != null) {
				service.setDoChecking(initializers.getHibernateInitializer().isDoChecking());
			}
		}
		
		try {
			service.start();
		} catch (Exception e) {
			throw new DeploymentException("error in starting the service", e);
		}
		
	}
	
	
	@Override
	public void undeploy(VFSDeploymentUnit deploymentUnit, IDMMetadata metadata) {
		service.destroy();
	}
	

	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	
	

}
