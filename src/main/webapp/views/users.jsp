<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User List</title>
  </head>
  <body>
    <h1>User list</h1>
    <ol>
      <c:forEach var="user" items="${users}">
        <li>
          <a href="<c:url value="/${user.name.toLowerCase()}" />">${user.name}</a>
        </li>
      </c:forEach>
    </ol>
  </body>
</html>
