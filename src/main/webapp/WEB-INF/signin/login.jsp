<%--
  Created by IntelliJ IDEA.
  User: nbarrett
  Date: 21/06/2016
  Time: 08:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title>Logon</title>
</head>
<body>

<form:form action="/global/logins" method="POST">

    <table>
        <tr>
            <td>User Name:</td>
        </tr>

        <tr>
            <td><form:input path="userName" /></td>
        </tr>

        <tr>
            <td>Email:</td>
        </tr>

        <tr>
            <td><form:input path="email" /></td>
        </tr>

        <tr>
            <td>Password:</td>
        </tr>

        <tr>
            <td><form:password path="password" /></td>
        </tr>

        <tr>
            <td><input type="submit" value="Submit" /></td>
        </tr>
    </table>

</form:form>

</body>
</html>
