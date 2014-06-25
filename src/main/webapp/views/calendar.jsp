<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
    <title>日历记</title>
    <link rel="shortcut icon" type="image/png" href="<c:url value="/images/fav.png" />" />
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/calendiary.css" />" />
  </head>
  <body>
    <table class="calendiary">
      <thead>
        <tr>
          <th>周日</th>
          <th>周一</th>
          <th>周二</th>
          <th>周三</th>
          <th>周四</th>
          <th>周五</th>
          <th>周六</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="week" items="${calendar}">
          <tr>
            <c:forEach var="day" items="${week}">
              <td>
                <div><a href="<c:url value="/${day.toString()}" />">${day.formattedDate}</a></div>
                <ul>
                  <c:forEach var="article" items="${day.articles}">
                    <li><a href="<c:url value="/${day.toString()}#${article.id}" />">${article.title}</a></li>
                  </c:forEach>
                </ul>
              </td>
            </c:forEach>
          </tr>
        </c:forEach>
      </tbody>
      <tfoot>
        <tr>
          <td colspan="2" class="calendiary-button-set">
            <a href="<c:url value="/" />"><button>今天</button></a>
            <a href="<c:url value="/${lastMonth}" />"><button>上月</button></a>
            <a href="<c:url value="/${nextMonth}" />"><button>下月</button></a>
            <a><button>日记</button></a>
          </td>
          <td colspan="3" class="calendiary-caption">${currentMonth}</td>
          <td colspan="2" class="calendiary-search">
            <form method="post" action="xxx">
              <input type="text" />
              <button>回顾</button>
            </form>
          </td>
        </tr>
      </tfoot>
    </table>
  </body>
</html>
