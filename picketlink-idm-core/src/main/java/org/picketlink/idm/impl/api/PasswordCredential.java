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

package org.picketlink.idm.impl.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class PasswordCredential extends AbstractCredential
{
   private final String value;

   public static final SimpleCredentialType TYPE  = new SimpleCredentialType("PASSWORD");

   public PasswordCredential(String value)
   {
      super(TYPE);
      this.value = value;
   }

   public String getValue()
   {
      return value;
   }

   public Object getEncodedValue()
   {
      if (value != null)
      {
         return md5AsHexString(getValue());
      }
      return null;
   }

   /**
    * Computes an md5 hash of a string.
    *
    * @param text the hashed string
    * @return the string hash
    * @throws NullPointerException if text is null
    */
   public static byte[] md5(String text)
   {
      // arguments check
      if (text == null)
      {
         throw new NullPointerException("null text");
      }

      try
      {
         MessageDigest md = MessageDigest.getInstance("MD5");
         md.update(text.getBytes());
         return md.digest();
      }
      catch (NoSuchAlgorithmException e)
      {
         
         throw new RuntimeException("Cannot find MD5 algorithm");
      }
   }

   /**
    * Computes an md5 hash and returns the result as a string in hexadecimal format.
    *
    * @param text the hashed string
    * @return the string hash
    * @throws NullPointerException if text is null
    */
   public static String md5AsHexString(String text)
   {
      return toHexString(md5(text));
   }

/**
    * Returns a string in the hexadecimal format.
    *
    * @param bytes the converted bytes
    * @return the hexadecimal string representing the bytes data
    * @throws IllegalArgumentException if the byte array is null
    */
   public static String toHexString(byte[] bytes)
   {
      if (bytes == null)
      {
         throw new IllegalArgumentException("byte array must not be null");
      }
      StringBuffer hex = new StringBuffer(bytes.length * 2);
      for (int i = 0; i < bytes.length; i++)
      {
         hex.append(Character.forDigit((bytes[i] & 0XF0) >> 4, 16));
         hex.append(Character.forDigit((bytes[i] & 0X0F), 16));
      }
      return hex.toString();
   }



}
