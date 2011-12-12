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

import org.picketlink.idm.spi.cache.IdentityObjectRelationshipSearch;

public class IdentityObjectRelationshipSearchImpl extends AbstractSPISearchImpl implements IdentityObjectRelationshipSearch
{

   private String fromIOName;

   private String fromIOType;

   private String toIOName;

   private String toIOType;

   private String relationshipType;

   private String ioName;

   private String ioType;

   private boolean parent;

   private boolean named;

   private String name;

   public String getFromIOName()
   {
      return fromIOName;
   }

   public void setFromIOName(String fromIOName)
   {
      this.fromIOName = fromIOName;
   }

   public String getFromIOType()
   {
      return fromIOType;
   }

   public void setFromIOType(String fromIOType)
   {
      this.fromIOType = fromIOType;
   }

   public String getToIOName()
   {
      return toIOName;
   }

   public void setToIOName(String toIOName)
   {
      this.toIOName = toIOName;
   }

   public String getToIOType()
   {
      return toIOType;
   }

   public void setToIOType(String toIOType)
   {
      this.toIOType = toIOType;
   }

   public String getRelationshipType()
   {
      return relationshipType;
   }

   public void setRelationshipType(String relationshipType)
   {
      this.relationshipType = relationshipType;
   }

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

   public boolean isParent()
   {
      return parent;
   }

   public void setParent(boolean parent)
   {
      this.parent = parent;
   }

   public boolean isNamed()
   {
      return named;
   }

   public void setNamed(boolean named)
   {
      this.named = named;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
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

      IdentityObjectRelationshipSearchImpl that = (IdentityObjectRelationshipSearchImpl)o;

      if (named != that.named)
      {
         return false;
      }
      if (parent != that.parent)
      {
         return false;
      }
      if (fromIOName != null ? !fromIOName.equals(that.fromIOName) : that.fromIOName != null)
      {
         return false;
      }
      if (fromIOType != null ? !fromIOType.equals(that.fromIOType) : that.fromIOType != null)
      {
         return false;
      }
      if (ioName != null ? !ioName.equals(that.ioName) : that.ioName != null)
      {
         return false;
      }
      if (ioType != null ? !ioType.equals(that.ioType) : that.ioType != null)
      {
         return false;
      }
      if (name != null ? !name.equals(that.name) : that.name != null)
      {
         return false;
      }
      if (relationshipType != null ? !relationshipType.equals(that.relationshipType) : that.relationshipType != null)
      {
         return false;
      }
      if (toIOName != null ? !toIOName.equals(that.toIOName) : that.toIOName != null)
      {
         return false;
      }
      if (toIOType != null ? !toIOType.equals(that.toIOType) : that.toIOType != null)
      {
         return false;
      }

      return true;
   }
}
