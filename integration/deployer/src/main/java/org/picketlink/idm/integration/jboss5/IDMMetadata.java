/**
 * 
 */
package org.picketlink.idm.integration.jboss5;

import org.picketlink.idm.integration.jboss5.jaxb2.PicketlinkIDMDeployerType;

import java.io.Serializable;

/**
 * 
 * 
 * @author  Jeff Yu
 *
 */
public class IDMMetadata implements Serializable{
	
	private static final long serialVersionUID = -3236524660920273677L;
	
	private PicketlinkIDMDeployerType deployerType;
	
	private String deployerFileName;

	public PicketlinkIDMDeployerType getDeploperType() {
		return deployerType;
	}

	public void setDeploperType(PicketlinkIDMDeployerType deployerType) {
		this.deployerType = deployerType;
	}

	public String getDeployerFileName() {
		return deployerFileName;
	}

	public void setDeployerFileName(String deployerFileName) {
		this.deployerFileName = deployerFileName;
	}
	
	


}
