<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Login</title>
  </head>
  <body>
    <form method="POST" action="<c:url value="/" />">
      <div>
        <label>Email:</label>
        <input type="text" name="email" />
      </div>
      <div>
        <label>Password:</label>
        <input type="password" name="password" />
      </div>
      <div>
        <button type="submit">Login</button>
      </div>
    </form>
      <div style="color: red">${error}</div>
  </body>
</html>
