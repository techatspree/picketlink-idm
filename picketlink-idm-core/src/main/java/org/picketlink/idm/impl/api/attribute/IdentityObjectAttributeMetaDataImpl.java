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

package org.picketlink.idm.impl.api.attribute;

import org.picketlink.idm.spi.configuration.metadata.IdentityObjectAttributeMetaData;
import org.picketlink.idm.api.AttributeDescription;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class IdentityObjectAttributeMetaDataImpl implements IdentityObjectAttributeMetaData, AttributeDescription
{

   private final String name;

   private final String storeMapping;

   private final String type;

   private final boolean readonly;

   private final boolean multivalued;

   private final boolean required;

   private final boolean unique;

   public IdentityObjectAttributeMetaDataImpl(String name,
                                              String storeMapping,
                                              String type,
                                              boolean readonly,
                                              boolean multivalued,
                                              boolean required,
                                              boolean unique)
   {
      this.name = name;
      this.storeMapping = storeMapping;
      this.type = type;
      this.readonly = readonly;
      this.multivalued = multivalued;
      this.required = required;
      this.unique = unique;
   }

   public IdentityObjectAttributeMetaDataImpl(IdentityObjectAttributeMetaData attributeMD)
   {
      this.name = attributeMD.getName();
      this.type = attributeMD.getType();
      this.readonly = attributeMD.isReadonly();
      this.multivalued = attributeMD.isMultivalued();
      this.required = attributeMD.isRequired();
      this.storeMapping = attributeMD.getStoreMapping();
      this.unique = attributeMD.isUnique();
   }

   public String getName()
   {
      return name;
   }

   public String getStoreMapping()
   {
      return storeMapping;
   }

   public String getType()
   {
      return type;
   }

   public boolean isReadonly()
   {
      return readonly;
   }

   public boolean isMultivalued()
   {
      return multivalued;
   }

   public boolean isRequired()
   {
      return required;
   }

   public boolean isUnique()
   {
      return unique;
   }
}
