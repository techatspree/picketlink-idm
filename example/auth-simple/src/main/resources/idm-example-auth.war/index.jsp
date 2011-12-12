<%@ page import="java.security.Principal" %>
<html>
<body>
<h2>Hello World!</h2>
<p>
    Try to login with default users: user/user or admin/admin
</p>
<a href="./protected/">protected content</a>

<%
    String logout = request.getParameter("logout");
    if (logout != null && logout.equals("true"))
    {
        request.getSession().invalidate();
    }
%>
</br>
<%
    Principal principal = request.getUserPrincipal();
    if (principal != null)
    {
%>
Logged in user:
<%
        out.println(principal.getName());
    }
%>
</br>
<a href="./index.jsp?logout=true">Logout!</a>
</body>
</html>
