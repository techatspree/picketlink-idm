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

package org.picketlink.idm.auth;

import org.jboss.security.auth.spi.UsernamePasswordLoginModule;
import org.jboss.security.SimpleGroup;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.User;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.common.transaction.Transactions;
import org.picketlink.idm.common.transaction.TransactionManagerProvider;
import org.picketlink.idm.common.exception.NoSuchUserException;
import org.apache.log4j.Logger;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.security.auth.callback.CallbackHandler;
import javax.security.jacc.PolicyContext;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.TransactionManager;
import java.util.Map;
import java.util.Collection;
import java.security.acl.Group;
import java.security.Principal;

/**
 * @author <a href="mailto:boleslaw.dawidowicz at redhat.com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public class JBossIdentityIDMLoginModule extends UsernamePasswordLoginModule
{

   private static Logger log = Logger.getLogger(JBossIdentityIDMLoginModule.class.getName());


   protected String identitySessionFactoryJNDIName;

   protected String realmName;

   protected String roleGroupTypeName;

   protected String userEnabledAttributeName;

   protected String additionalRole;

   protected String associatedGroupType;

   protected String associatedGroupName;

   protected String validateUserNameCase;

   protected String userNameToLowerCase;

   protected String manageTransaction;

   private IdentitySessionFactory identitySessionFactory;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options)
   {
      super.initialize(subject, callbackHandler, sharedState, options);

      // Get data
      identitySessionFactoryJNDIName = (String) options.get("identitySessionFactoryJNDIName");
      realmName = (String) options.get("realmName");
      roleGroupTypeName = (String) options.get("roleGroupTypeName");
      userEnabledAttributeName = (String) options.get("userEnabledAttributeName");
      additionalRole = (String) options.get("additionalRole");
      associatedGroupType = (String) options.get("associatedGroupType");
      associatedGroupName = (String) options.get("associatedGroupName");
      validateUserNameCase = (String) options.get("validateUserNameCase");
      userNameToLowerCase = (String) options.get("userNameToLowerCase");
      manageTransaction = (String) options.get("transactionAware");

      // Some info
      if (log.isDebugEnabled())
      log.debug("identitySessionFactoryJNDIName = " + identitySessionFactoryJNDIName);
      log.debug("realmName = " + realmName);
      log.debug("groupTypeName = " + roleGroupTypeName);
      log.debug("userEnabledAttributeName = " + userEnabledAttributeName);
      log.debug("additionalRole = " + additionalRole);
      log.debug("havingRole = " + associatedGroupName);
      log.debug("validateUserNameCase = " + validateUserNameCase);
      log.debug("userNameToLowerCase = " + userNameToLowerCase);
      log.debug("transactionAware = " + manageTransaction);
   }

   protected String getUsersPassword() throws LoginException
   {
      return "";
   }

   protected boolean validatePassword(final String inputPassword, String expectedPassword)
   {

      HttpServletRequest request = null;
      try
      {
         request = (HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest");
      }
      catch(Exception e)
      {
//         log.error(this,e);
         throw new RuntimeException(e);
      }


      // If attribute ssoSuccess is set just let user in
      Object ssoSuccess = request.getAttribute("ssoSuccess");
      if(ssoSuccess != null)
      {
         return true;
      }

      if (inputPassword != null)
      {
         try
         {
            try
            {

               UserStatus userStatus = getUserStatus(inputPassword);

               // Set the user Status in the request so that the login page can show an error message accordingly
               request.setAttribute("org.picketlink.idm.userStatus", userStatus);

               if (userStatus == UserStatus.DISABLE)
               {
                  //request.setAttribute("org.picketlink.idm.loginError", "Your account is disabled");
                  return false;
               }
               else if (userStatus == UserStatus.NOTASSIGNEDTOROLE)
               {
                  //request.setAttribute("org.picketlink.idm.loginError", "The user doesn't have the correct role");
                  return false;
               }
               else if ((userStatus == UserStatus.UNEXISTING) || userStatus == UserStatus.WRONGPASSWORD)
               {
                  //request.setAttribute("org.picketlink.idm.loginError", "The user doesn't exist or the password is incorrect");
                  return false;
               }
               else if (userStatus == UserStatus.OK)
               {
                  return true;
               }
               else
               {
                  log.error("Unexpected error while logging in");
                  return false;
               }            }
            catch (Exception e)
            {
               log.error("Error when validating password: ",e);
            }
         }
         catch (Exception e)
         {
            log.error("Failed to validate password: ", e);
         }
      }
      return false;
   }

   protected UserStatus getUserStatus(final String inputPassword)
   {
      UserStatus result = null;

       try {
            TransactionManager tm = TransactionManagerProvider.JBOSS_PROVIDER.getTransactionManager();
            UserStatus tmp = (UserStatus)Transactions.required(tm, new Transactions.Runnable()
            {
               public Object run() throws Exception
               {
                  IdentitySession ids = getIdentitySessionFactory().getCurrentIdentitySession(realmName);
                              ids.beginTransaction();

                  if (manageTransaction != null && manageTransaction.equals("true"))
                  {
                     ids.beginTransaction();
                  }

                  UserStatus status = _getUserStatus(inputPassword);

                  if (manageTransaction != null && manageTransaction.equals("true"))
                  {
                     ids.getTransaction().commit();
                  }

                  return status;
               }
            });
            if (tmp != null)
            {
               result = tmp;
            }
         } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
         return result;


   }


   protected UserStatus _getUserStatus(final String inputPassword) throws Exception
   {
      try
      {
         IdentitySession ids = getIdentitySessionFactory().getCurrentIdentitySession(realmName);

         User user = ids.getPersistenceManager().findUser(getUsername());

         // in case module implementation doesn't throw proper
         // exception...
         if (user == null)
         {
            throw new NoSuchUserException("UserModule returned null user object");
         }

         //This is because LDAP binds can be non case sensitive
         if (validateUserNameCase != null && validateUserNameCase.equalsIgnoreCase("true")
            && !getUsername().equals(user.getKey()))
         {
            return UserStatus.UNEXISTING;
         }

         //Enabled
         if (userEnabledAttributeName != null)
         {

            boolean enabled = false;
            try {
               Object enabledS;
               enabledS = ids.getAttributesManager().getAttribute(user, userEnabledAttributeName);
               if (enabledS != null) {
                  enabled = new Boolean(enabledS.toString());
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
            if (!enabled) {
               return UserStatus.DISABLE;
            }
         }

         if (associatedGroupName != null && associatedGroupType != null)
         {
            boolean hasTheGroup = false;

            org.picketlink.idm.api.Group associatedGroup =
               ids.getPersistenceManager().findGroup(associatedGroupName, associatedGroupType);

            if (associatedGroup != null)
            {
               hasTheGroup = ids.getRelationshipManager().isAssociated(associatedGroup, user);
            }

            if (!hasTheGroup)
            {
               return UserStatus.NOTASSIGNEDTOROLE;
            }
         }

         if (!ids.getAttributesManager().validatePassword(user, inputPassword))
         {
            return UserStatus.WRONGPASSWORD;
         }

      }
      catch (NoSuchUserException e1)
      {
         return UserStatus.UNEXISTING;
      }
      catch (Exception e)
      {
         throw new LoginException(e.toString());
      }
      return UserStatus.OK;
   }

   protected Group[] getRoleSets() throws LoginException
   {
      try {
            TransactionManager tm = TransactionManagerProvider.JBOSS_PROVIDER.getTransactionManager();
            return (Group[]) Transactions.required(tm, new Transactions.Runnable()
            {
               public Object run() throws Exception
               {
                  IdentitySession ids = getIdentitySessionFactory().getCurrentIdentitySession(realmName);
                              ids.beginTransaction();

                  if (manageTransaction != null && manageTransaction.equals("true"))
                  {
                     ids.beginTransaction();
                  }

                  Group[] result = _getRoleSets();

                  if (manageTransaction != null && manageTransaction.equals("true"))
                  {
                     ids.getTransaction().commit();
                  }


                  return result;
               }
            });
         } catch (Exception e) {
            Throwable cause = e.getCause();
            throw new LoginException(cause.toString());
         }


   }

   protected Group[] _getRoleSets() throws Exception
   {
      Group rolesGroup = new SimpleGroup("Roles");

      //
      if (additionalRole != null) {
         rolesGroup.addMember(createIdentity(additionalRole));
      }

      try {

         IdentitySession ids = getIdentitySessionFactory().getCurrentIdentitySession(realmName);


         User user = ids.getPersistenceManager().findUser(getUsername());
         Collection<org.picketlink.idm.api.Group> userGroups =
            ids.getRelationshipManager().findAssociatedGroups(user, roleGroupTypeName);

         //
         for (org.picketlink.idm.api.Group userGroup : userGroups)
         {
            String roleName = userGroup.getName();

            try {
               Principal p = createIdentity(roleName);
               rolesGroup.addMember(p);
            } catch (Exception e) {
               log.info("Failed to create principal " + roleName, e);
            }

         }

      } catch (Exception e) {
         throw new LoginException(e.toString());
      }
      //
      return new Group[] { rolesGroup };

   }



   /** Subclass to use the PortalPrincipal to make the username easier to retrieve by the portal. */
   protected Principal createIdentity(String username) throws Exception
   {
      return new UserPrincipal(username);
   }

   protected String getUsername()
   {
      if (userNameToLowerCase != null && userNameToLowerCase.equalsIgnoreCase("true"))
      {
         return super.getUsername().toLowerCase();
      }
      return super.getUsername();
   }

   protected String[] getUsernameAndPassword() throws LoginException
   {
      String[] names =  super.getUsernameAndPassword();

      if (userNameToLowerCase != null && userNameToLowerCase.equalsIgnoreCase("true"))
      {
         if (names[0] != null)
         {
            names[0] = names[0].toLowerCase();
         }
      }
      return names;

   }
                                                
   protected IdentitySessionFactory getIdentitySessionFactory() throws NamingException
   {
      if (identitySessionFactory == null)
      {
         identitySessionFactory = (IdentitySessionFactory)new InitialContext().lookup(identitySessionFactoryJNDIName);
      }
      return identitySessionFactory;
   }
}
