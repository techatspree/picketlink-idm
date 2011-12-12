<%@ page import="java.security.Principal" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="org.picketlink.idm.api.IdentitySessionFactory" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="org.picketlink.idm.api.Group" %>
<%@ page import="java.util.Collection" %>
<%@ page import="org.picketlink.idm.api.IdentitySession" %>
<%@ page import="javax.transaction.UserTransaction" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.security.Principal" %>
<html>
<body>
<h2>Protected page!</h2>
</br>
Secured content. Logged in user:
<%
    Principal principal = request.getUserPrincipal();
    if (principal != null)
    {
        out.println(principal.getName());
    }
%>

</br>
</br>
<%
    Context ctx = new InitialContext();
    try
    {
        IdentitySessionFactory ids = (IdentitySessionFactory)ctx.lookup("java:/IdentitySessionFactory");
        IdentitySession is = ids.getCurrentIdentitySession("realm://JBossIdentity");
        
        UserTransaction tx = (UserTransaction)ctx.lookup("UserTransaction");
        tx.begin();
        
         Collection<Group> groups = is.getRelationshipManager().findAssociatedGroups(principal.getName(), "GROUP", null);

         out.println("Groups associated with user: ");

         for (Group group : groups)
         {
             out.println(group.getName());
         }

	     tx.commit();

    }
    catch (Exception e)
    {
        out.println("Failed to obtain IdentitySessionFactory: ");
        e.printStackTrace();
    }

%>

</br>
</br>
<a href="../index.jsp?logout=true">Logout!</a>
</body>
</html>