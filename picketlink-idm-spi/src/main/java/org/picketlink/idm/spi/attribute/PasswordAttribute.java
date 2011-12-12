///*
// * JBoss, Home of Professional Open Source.
// * Copyright 2008, Red Hat Middleware LLC, and individual contributors
// * as indicated by the @author tags. See the copyright.txt file in the
// * distribution for a full listing of individual contributors.
// *
// * This is free software; you can redistribute it and/or modify it
// * under the terms of the GNU Lesser General Public License as
// * published by the Free Software Foundation; either version 2.1 of
// * the License, or (at your option) any later version.
// *
// * This software is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// * Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public
// * License along with this software; if not, write to the Free
// * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
// * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
// */
//package org.picketlink.spi.attribute;
//
//import org.picketlink.spi.model.IdentityObjectType;
//import org.picketlink.spi.model.IdentityObjectAttribute;
//import org.picketlink.spi.policy.PasswordExpirationPolicy;
//
//import java.util.Date;
//
//
//
///**
// * Represents a password
// * @author Anil.Saldhana@redhat.com
// * @since Jul 13, 2008
// */
//public class PasswordAttribute<T extends IdentityObjectType>
//implements IdentityObjectAttribute
//{
//   /**
//    * Password policy governing this attribute.
//    * A null value indicates that there is no policy.
//    */
//   protected PasswordExpirationPolicy<T> passwordPolicy;
//
//   /**
//    * Get the date time when the password was last updated.
//    * The update can be based on password expiration.
//    */
//   protected Date lastUpdated = new Date();
//
//   public PasswordAttribute()
//   {
//   }
//
//   public PasswordAttribute(PasswordExpirationPolicy<T> aPasswordPolicy)
//   {
//      this.passwordPolicy = aPasswordPolicy;
//   }
//
//   public PasswordExpirationPolicy<T> getPasswordPolicy()
//   {
//      return passwordPolicy;
//   }
//
//   public void setPasswordPolicy(PasswordExpirationPolicy<T> passwordPolicy)
//   {
//      this.passwordPolicy = passwordPolicy;
//   }
//
//
//}