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

package org.picketlink.idm.spi.configuration.metadata;

import org.picketlink.idm.spi.configuration.metadata.IdentityObjectAttributeMetaData;

import java.util.List;
import java.util.Map;

/**
 * Configuration of IdentityObjectType within IdentityStore
 *
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public interface IdentityObjectTypeMetaData
{
   /**
    * @return name
    */
   String getName();

   /**
    * @return allowed relationships meta data
    */
   List<RelationshipMetaData> getRelationships();

   /**
    * @return allowed attributes meta data
    */
   List<IdentityObjectAttributeMetaData> getAttributes();

   /**
    * @return list of supported CredentialType names
    */
   List<String> getCredentials();

   /**
    * @return options
    */
   Map<String, List<String>> getOptions();

   /**
    * @param optionName
    * @return option values
    */
   List<String> getOption(String optionName);

   /**
    * @param optionName
    * @return single (first) value of a given option
    */
   String getOptionSingleValue(String optionName);
}
