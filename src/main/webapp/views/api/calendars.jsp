<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
[<c:forEach var="calendar" varStatus="status" items="${calendars}"><c:if test="${status.index gt 0}">, </c:if>{
  "id": ${calendar.referId eq 0? calendar.id: calendar.referId},
  "pid": ${calendar.parentId},
  "name": "${calendar.name}",
  "color": "#${calendar.color}",
  "sort": ${calendar.sort}
}</c:forEach>]
