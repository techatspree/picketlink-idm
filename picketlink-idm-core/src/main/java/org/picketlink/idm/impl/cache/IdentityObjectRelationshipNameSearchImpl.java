/*
* JBoss, a division of Red Hat
* Copyright 2009, Red Hat Middleware, LLC, and individual contributors as indicated
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


package org.picketlink.idm.impl.cache;

import org.picketlink.idm.spi.cache.IdentityObjectRelationshipNameSearch;

public class IdentityObjectRelationshipNameSearchImpl extends AbstractSPISearchImpl implements IdentityObjectRelationshipNameSearch
{

   private String ioName;

   private String ioType;

   public String getIoName()
   {
      return ioName;
   }

   public void setIoName(String ioName)
   {
      this.ioName = ioName;
   }

   public String getIoType()
   {
      return ioType;
   }

   public void setIoType(String ioType)
   {
      this.ioType = ioType;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o == null || getClass() != o.getClass())
      {
         return false;
      }

      IdentityObjectRelationshipNameSearchImpl that = (IdentityObjectRelationshipNameSearchImpl)o;

      if (ioName != null ? !ioName.equals(that.ioName) : that.ioName != null)
      {
         return false;
      }
      if (ioType != null ? !ioType.equals(that.ioType) : that.ioType != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = ioName != null ? ioName.hashCode() : 0;
      result = 31 * result + (ioType != null ? ioType.hashCode() : 0);
      return result;
   }
}
