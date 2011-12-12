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

import org.picketlink.idm.spi.cache.IdentityObjectSearch;

public class IdentityObjectSearchImpl extends AbstractSPISearchImpl implements IdentityObjectSearch
{
   private String id;

   private String name;

   private String type;

   private String relationshipType;

   private String relatedIOName;

   private String relatedIOType;

   private boolean parent;

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   public String getRelatedIOName()
   {
      return relatedIOName;
   }

   public void setRelatedIOName(String relatedIOName)
   {
      this.relatedIOName = relatedIOName;
   }

   public String getRelatedIOType()
   {
      return relatedIOType;
   }

   public void setRelatedIOType(String relatedIOType)
   {
      this.relatedIOType = relatedIOType;
   }

   public boolean isParent()
   {
      return parent;
   }

   public void setParent(boolean parent)
   {
      this.parent = parent;
   }

   public String getRelationshipType()
   {
      return relationshipType;
   }

   public void setRelationshipType(String relationshipType)
   {
      this.relationshipType = relationshipType;
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

      IdentityObjectSearchImpl that = (IdentityObjectSearchImpl)o;

      if (parent != that.parent)
      {
         return false;
      }
      if (id != null ? !id.equals(that.id) : that.id != null)
      {
         return false;
      }
      if (name != null ? !name.equals(that.name) : that.name != null)
      {
         return false;
      }
      if (relatedIOName != null ? !relatedIOName.equals(that.relatedIOName) : that.relatedIOName != null)
      {
         return false;
      }
      if (relatedIOType != null ? !relatedIOType.equals(that.relatedIOType) : that.relatedIOType != null)
      {
         return false;
      }
      if (relationshipType != null ? !relationshipType.equals(that.relationshipType) : that.relationshipType != null)
      {
         return false;
      }
      if (type != null ? !type.equals(that.type) : that.type != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = id != null ? id.hashCode() : 0;
      result = 31 * result + (name != null ? name.hashCode() : 0);
      result = 31 * result + (type != null ? type.hashCode() : 0);
      result = 31 * result + (relationshipType != null ? relationshipType.hashCode() : 0);
      result = 31 * result + (relatedIOName != null ? relatedIOName.hashCode() : 0);
      result = 31 * result + (relatedIOType != null ? relatedIOType.hashCode() : 0);
      result = 31 * result + (parent ? 1 : 0);
      return result;
   }
}
