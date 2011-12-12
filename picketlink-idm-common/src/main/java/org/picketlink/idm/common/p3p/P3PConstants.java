/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.picketlink.idm.common.p3p;

/**
 * Class taken from JBoss Portal common module. 
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 5451 $
 */
public final class P3PConstants
{
   public enum TelecomType
   {
      TELEPHONE(TELECOM_TELEPHONE),
      FAX(TELECOM_FAX),
      MOBILE(TELECOM_MOBILE),
      PAGER(TELECOM_PAGER);

      private final String prefix;

      TelecomType(String prefix)
      {
         this.prefix = prefix;
      }

      public String getPrefix()
      {
         return prefix;
      }
   }

   public enum TelecomInfo
   {
      INTCODE(TELECOM_INTCODE),
      LOCCODE(TELECOM_LOCCODE),
      NUMBER(TELECOM_NUMBER),
      EXT(TELECOM_EXT),
      COMMENT(TELECOM_COMMENT);

      private final String name;

      TelecomInfo(String name)
      {
         this.name = name;
      }

      public String getName()
      {
         return name;
      }
   }

   public enum PostalInfo
   {
      NAME(POSTAL_NAME),
      STREET(POSTAL_STREET),
      CITY(POSTAL_CITY),
      STATEPROV(POSTAL_STATEPROV),
      POSTALCODE(POSTAL_POSTALCODE),
      COUNTRY(POSTAL_COUNTRY),
      ORGANIZATION(POSTAL_ORGANIZATION);

      private final String name;

      PostalInfo(String name)
      {
         this.name = name;
      }

      public String getName()
      {
         return name;
      }
   }

   public enum OnlineInfo
   {
      EMAIL(ONLINE_EMAIL),
      URI(ONLINE_URI);

      private final String name;

      OnlineInfo(String name)
      {
         this.name = name;
      }

      public String getName()
      {
         return name;
      }
   }


   // Postal
   private static final String POSTAL_NAME = "postal.name";
   private static final String POSTAL_STREET = "postal.street";
   private static final String POSTAL_CITY = "postal.city";
   private static final String POSTAL_STATEPROV = "postal.stateprov";
   private static final String POSTAL_POSTALCODE = "postal.postalcode";
   private static final String POSTAL_COUNTRY = "postal.country";
   private static final String POSTAL_ORGANIZATION = "postal.organization";

   // Telecom
   private static final String TELECOM_TELEPHONE = "telecom.telephone.";
   private static final String TELECOM_FAX = "telecom.fax.";
   private static final String TELECOM_MOBILE = "telecom.mobile.";
   private static final String TELECOM_PAGER = "telecom.pager.";
   private static final String TELECOM_INTCODE = "intcode";
   private static final String TELECOM_LOCCODE = "loccode";
   private static final String TELECOM_NUMBER = "number";
   private static final String TELECOM_EXT = "ext";
   private static final String TELECOM_COMMENT = "comment";

   // Online
   private static final String ONLINE_URI = "online.uri";
   private static final String ONLINE_EMAIL = "online.email";

   /*
   * User information attribute names (PLT.D in the portlet spec) that are defined in P3P spec.
   */

   //
   public static final String INFO_USER_BDATE = "user.bdate";
   public static final String INFO_USER_GENDER = "user.gender";
   public static final String INFO_USER_EMPLOYER = "user.employer";
   public static final String INFO_USER_DEPARTMENT = "user.department";
   public static final String INFO_USER_JOB_TITLE = "user.jobtitle";
   public static final String INFO_USER_NAME_PREFIX = "user.name.prefix";
   public static final String INFO_USER_NAME_GIVEN = "user.name.given";
   public static final String INFO_USER_NAME_FAMILY = "user.name.family";
   public static final String INFO_USER_NAME_MIDDLE = "user.name.middle";
   public static final String INFO_USER_NAME_SUFFIX = "user.name.suffix";
   public static final String INFO_USER_NAME_NICKNAME = "user.name.nickName";

   // User home
   private static final String INFO_USER_HOME_PREFIX = "user.home-info.";
   public static final String INFO_USER_HOME_INFO_POSTAL_NAME = INFO_USER_HOME_PREFIX + POSTAL_NAME;
   public static final String INFO_USER_HOME_INFO_POSTAL_STREET = INFO_USER_HOME_PREFIX + POSTAL_STREET;
   public static final String INFO_USER_HOME_INFO_POSTAL_CITY = INFO_USER_HOME_PREFIX + POSTAL_CITY;
   public static final String INFO_USER_HOME_INFO_POSTAL_STATEPROV = INFO_USER_HOME_PREFIX + POSTAL_STATEPROV;
   public static final String INFO_USER_HOME_INFO_POSTAL_POSTALCODE = INFO_USER_HOME_PREFIX + POSTAL_POSTALCODE;
   public static final String INFO_USER_HOME_INFO_POSTAL_COUNTRY = INFO_USER_HOME_PREFIX + POSTAL_COUNTRY;
   public static final String INFO_USER_HOME_INFO_POSTAL_ORGANIZATION = INFO_USER_HOME_PREFIX + POSTAL_ORGANIZATION;
   public static final String INFO_USER_HOME_INFO_TELECOM_TELEPHONE_INTCODE = INFO_USER_HOME_PREFIX + TELECOM_TELEPHONE + TELECOM_INTCODE;
   public static final String INFO_USER_HOME_INFO_TELECOM_TELEPHONE_LOCCODE = INFO_USER_HOME_PREFIX + TELECOM_TELEPHONE + TELECOM_LOCCODE;
   public static final String INFO_USER_HOME_INFO_TELECOM_TELEPHONE_NUMBER = INFO_USER_HOME_PREFIX + TELECOM_TELEPHONE + TELECOM_NUMBER;
   public static final String INFO_USER_HOME_INFO_TELECOM_TELEPHONE_EXT = INFO_USER_HOME_PREFIX + TELECOM_TELEPHONE + TELECOM_EXT;
   public static final String INFO_USER_HOME_INFO_TELECOM_TELEPHONE_COMMENT = INFO_USER_HOME_PREFIX + TELECOM_TELEPHONE + TELECOM_COMMENT;
   public static final String INFO_USER_HOME_INFO_TELECOM_FAX_INTCODE = INFO_USER_HOME_PREFIX + TELECOM_FAX + TELECOM_INTCODE;
   public static final String INFO_USER_HOME_INFO_TELECOM_FAX_LOCCODE = INFO_USER_HOME_PREFIX + TELECOM_FAX + TELECOM_LOCCODE;
   public static final String INFO_USER_HOME_INFO_TELECOM_FAX_NUMBER = INFO_USER_HOME_PREFIX + TELECOM_FAX + TELECOM_NUMBER;
   public static final String INFO_USER_HOME_INFO_TELECOM_FAX_EXT = INFO_USER_HOME_PREFIX + TELECOM_FAX + TELECOM_EXT;
   public static final String INFO_USER_HOME_INFO_TELECOM_FAX_COMMENT = INFO_USER_HOME_PREFIX + TELECOM_FAX + TELECOM_COMMENT;
   public static final String INFO_USER_HOME_INFO_TELECOM_MOBILE_INTCODE = INFO_USER_HOME_PREFIX + TELECOM_MOBILE + TELECOM_INTCODE;
   public static final String INFO_USER_HOME_INFO_TELECOM_MOBILE_LOCCODE = INFO_USER_HOME_PREFIX + TELECOM_MOBILE + TELECOM_LOCCODE;
   public static final String INFO_USER_HOME_INFO_TELECOM_MOBILE_NUMBER = INFO_USER_HOME_PREFIX + TELECOM_MOBILE + TELECOM_NUMBER;
   public static final String INFO_USER_HOME_INFO_TELECOM_MOBILE_EXT = INFO_USER_HOME_PREFIX + TELECOM_MOBILE + TELECOM_EXT;
   public static final String INFO_USER_HOME_INFO_TELECOM_MOBILE_COMMENT = INFO_USER_HOME_PREFIX + TELECOM_MOBILE + TELECOM_COMMENT;
   public static final String INFO_USER_HOME_INFO_TELECOM_PAGER_INTCODE = INFO_USER_HOME_PREFIX + TELECOM_PAGER + TELECOM_INTCODE;
   public static final String INFO_USER_HOME_INFO_TELECOM_PAGER_LOCCODE = INFO_USER_HOME_PREFIX + TELECOM_PAGER + TELECOM_LOCCODE;
   public static final String INFO_USER_HOME_INFO_TELECOM_PAGER_NUMBER = INFO_USER_HOME_PREFIX + TELECOM_PAGER + TELECOM_NUMBER;
   public static final String INFO_USER_HOME_INFO_TELECOM_PAGER_EXT = INFO_USER_HOME_PREFIX + TELECOM_PAGER + TELECOM_EXT;
   public static final String INFO_USER_HOME_INFO_TELECOM_PAGER_COMMENT = INFO_USER_HOME_PREFIX + TELECOM_PAGER + TELECOM_COMMENT;
   public static final String INFO_USER_HOME_INFO_ONLINE_EMAIL = INFO_USER_HOME_PREFIX + ONLINE_EMAIL;
   public static final String INFO_USER_HOME_INFO_ONLINE_URI = INFO_USER_HOME_PREFIX + ONLINE_URI;

   // User Business
   private static final String INFO_USER_BUSINESS_PREFIX = "user.business-info.";
   public static final String INFO_USER_BUSINESS_INFO_POSTAL_NAME = INFO_USER_BUSINESS_PREFIX + POSTAL_NAME;
   public static final String INFO_USER_BUSINESS_INFO_POSTAL_STREET = INFO_USER_BUSINESS_PREFIX + POSTAL_STREET;
   public static final String INFO_USER_BUSINESS_INFO_POSTAL_CITY = INFO_USER_BUSINESS_PREFIX + POSTAL_CITY;
   public static final String INFO_USER_BUSINESS_INFO_POSTAL_STATEPROV = INFO_USER_BUSINESS_PREFIX + POSTAL_STATEPROV;
   public static final String INFO_USER_BUSINESS_INFO_POSTAL_POSTALCODE = INFO_USER_BUSINESS_PREFIX + POSTAL_POSTALCODE;
   public static final String INFO_USER_BUSINESS_INFO_POSTAL_COUNTRY = INFO_USER_BUSINESS_PREFIX + POSTAL_COUNTRY;
   public static final String INFO_USER_BUSINESS_INFO_POSTAL_ORGANIZATION = INFO_USER_BUSINESS_PREFIX + POSTAL_ORGANIZATION;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_TELEPHONE_INTCODE = INFO_USER_BUSINESS_PREFIX + TELECOM_TELEPHONE + TELECOM_INTCODE;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_TELEPHONE_LOCCODE = INFO_USER_BUSINESS_PREFIX + TELECOM_TELEPHONE + TELECOM_LOCCODE;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_TELEPHONE_NUMBER = INFO_USER_BUSINESS_PREFIX + TELECOM_TELEPHONE + TELECOM_NUMBER;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_TELEPHONE_EXT = INFO_USER_BUSINESS_PREFIX + TELECOM_TELEPHONE + TELECOM_EXT;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_TELEPHONE_COMMENT = INFO_USER_BUSINESS_PREFIX + TELECOM_TELEPHONE + TELECOM_COMMENT;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_FAX_INTCODE = INFO_USER_BUSINESS_PREFIX + TELECOM_FAX + TELECOM_INTCODE;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_FAX_LOCCODE = INFO_USER_BUSINESS_PREFIX + TELECOM_FAX + TELECOM_LOCCODE;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_FAX_NUMBER = INFO_USER_BUSINESS_PREFIX + TELECOM_FAX + TELECOM_NUMBER;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_FAX_EXT = INFO_USER_BUSINESS_PREFIX + TELECOM_FAX + TELECOM_EXT;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_FAX_COMMENT = INFO_USER_BUSINESS_PREFIX + TELECOM_FAX + TELECOM_COMMENT;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_INTCODE = INFO_USER_BUSINESS_PREFIX + TELECOM_MOBILE + TELECOM_INTCODE;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_LOCCODE = INFO_USER_BUSINESS_PREFIX + TELECOM_MOBILE + TELECOM_LOCCODE;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_NUMBER = INFO_USER_BUSINESS_PREFIX + TELECOM_MOBILE + TELECOM_NUMBER;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_EXT = INFO_USER_BUSINESS_PREFIX + TELECOM_MOBILE + TELECOM_EXT;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_COMMENT = INFO_USER_BUSINESS_PREFIX + TELECOM_MOBILE + TELECOM_COMMENT;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_PAGER_INTCODE = INFO_USER_BUSINESS_PREFIX + TELECOM_PAGER + TELECOM_INTCODE;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_PAGER_LOCCODE = INFO_USER_BUSINESS_PREFIX + TELECOM_PAGER + TELECOM_LOCCODE;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_PAGER_NUMBER = INFO_USER_BUSINESS_PREFIX + TELECOM_PAGER + TELECOM_NUMBER;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_PAGER_EXT = INFO_USER_BUSINESS_PREFIX + TELECOM_PAGER + TELECOM_EXT;
   public static final String INFO_USER_BUSINESS_INFO_TELECOM_PAGER_COMMENT = INFO_USER_BUSINESS_PREFIX + TELECOM_PAGER + TELECOM_COMMENT;
   public static final String INFO_USER_BUSINESS_INFO_ONLINE_EMAIL = INFO_USER_BUSINESS_PREFIX + ONLINE_EMAIL;
   public static final String INFO_USER_BUSINESS_INFO_ONLINE_URI = INFO_USER_BUSINESS_PREFIX + ONLINE_URI;

   private P3PConstants()
   {
   }

   private static String getHomeOrBusinessPrefix(boolean isBusiness)
   {
      return isBusiness ? P3PConstants.INFO_USER_BUSINESS_PREFIX : P3PConstants.INFO_USER_HOME_PREFIX;
   }

   public static String getPostalUserInfoKey(PostalInfo info, boolean isBusiness)
   {
      return getHomeOrBusinessPrefix(isBusiness) + info.getName();
   }

   public static String getTelecomInfoKey(TelecomType type, TelecomInfo info, boolean isBusiness)
   {
      return getHomeOrBusinessPrefix(isBusiness) + type.getPrefix() + info.getName();
   }

   public static String getOnlineUserInfoKey(OnlineInfo info, boolean isBusiness)
   {
      return getHomeOrBusinessPrefix(isBusiness) + info.getName();
   }
}
