/**
 * 
 */
package org.picketlink.idm.integration.jboss5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.picketlink.idm.common.exception.IdentityConfigurationException;
import org.picketlink.idm.impl.helper.SecurityActions;
import org.picketlink.idm.integration.jboss5.jaxb2.PicketlinkIDMDeployerType;

/**
 * @author  Jeff Yu
 *
 */
public class JAXB2IdentityDeployerConfiguration {
	
	private static final Logger logger = Logger.getLogger(JAXB2IdentityDeployerConfiguration.class.getName());
	
	@SuppressWarnings({"unchecked"})
	public static PicketlinkIDMDeployerType createDeployerConfiguration(InputStream inputStream) throws Exception {
		if (inputStream == null) {
			throw new NullPointerException("deployer configuration is null");
		}
		
		JAXBElement<PicketlinkIDMDeployerType> deployerType = null;
		
		try {
	         JAXBContext jaxbContext = JAXBContext.newInstance("org.picketlink.idm.integration.jboss5.jaxb2");
	         Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
	         deployerType = (JAXBElement<PicketlinkIDMDeployerType>)unMarshaller.unmarshal(inputStream);
	         
		}catch (JAXBException e) {
			logger.severe(e.getMessage());
	         throw new IdentityConfigurationException("Cannot unmarshal idm deployer configuration: ", e);
	      }
		
		if (deployerType != null) {
			return deployerType.getValue();
		}
		return null;
	}
	
	
	public static PicketlinkIDMDeployerType createDeployerConfiguration(File file) throws Exception {
	      if (file == null)
	      {
	         throw new NullPointerException("Identity deployer file is null");
	      }
	      
	      InputStream inputStream;
	      try
	      {
	         inputStream = new FileInputStream(file);
	      } catch (FileNotFoundException e)
	      {
	         throw new IllegalArgumentException("Identity deployer file "+file.getAbsolutePath()+" does not exist");
	      }
	      return createDeployerConfiguration(inputStream);
	}
	
	
	public static PicketlinkIDMDeployerType createDeployerConfiguration(String file) throws Exception {
	      ClassLoader classLoader = SecurityActions.getContextClassLoader();
	      InputStream inputStream = classLoader.getResourceAsStream(file);
	      if (inputStream == null)
	      {
	         throw new NullPointerException("Resource "+file+" does not exist");
	      }
	      return createDeployerConfiguration(inputStream);
	}

}
