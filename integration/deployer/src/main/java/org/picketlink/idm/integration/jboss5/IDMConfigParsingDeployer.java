/**
 * 
 */
package org.picketlink.idm.integration.jboss5;

import java.util.logging.Logger;

import org.jboss.deployers.vfs.spi.deployer.AbstractVFSParsingDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.virtual.VirtualFile;
import org.picketlink.idm.integration.jboss5.jaxb2.PicketlinkIDMDeployerType;

/**
 * IDMConfigParsingDeployer is picking up the *-jboss-idm.xml files, produce the {@link IDMMetadata} Object, which will be used in the
 * IDMDeployer. This parsing deployer is in the 'Parse' phase of JBoss Deployment framework.
 * 
 * @author  Jeff Yu
 *
 */
public class IDMConfigParsingDeployer extends AbstractVFSParsingDeployer<IDMMetadata>{
	
	private static final Logger logger = Logger.getLogger(IDMConfigParsingDeployer.class.getName());
	
	public IDMConfigParsingDeployer() {
		super(IDMMetadata.class);
	}

	@Override
	protected IDMMetadata parse(VFSDeploymentUnit deploymentUnit, VirtualFile file, IDMMetadata metadata) throws Exception {
		logger.fine("Parsing IDM deployer file");
				
		IDMMetadata metaData = new IDMMetadata();
		metaData.setDeployerFileName(file.getPathName());
		
		PicketlinkIDMDeployerType deployerMetadata = JAXB2IdentityDeployerConfiguration.createDeployerConfiguration(file.openStream());
		metaData.setDeploperType(deployerMetadata);		

		logger.fine("the configuration file path name is: [" + file.getPathName() + "]");
		return metaData;
	}

}
