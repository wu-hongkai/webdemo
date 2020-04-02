<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>客户列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
<h3 align="center">客户列表</h3>
<table border="1" width="70%" align="center">
	<tr>
		<th>客户姓名</th>
		<th>性别</th>
		<th>生日</th>
		<th>手机</th>
		<th>邮箱</th>
		<th>描述</th>
		<th>操作</th>
	</tr>
	<%-- 循环显示，一整个tr表示一行记录，循环次数等于一次显示的行数 --%>
	<c:forEach items="${pageBean.beanList}" var="cstm">
	<tr>
		<td>${cstm.cname }</td>
		<td>${cstm.gender }</td>
		<td>${cstm.birthday }</td>
		<td>${cstm.cellphone }</td>
		<td>${cstm.email }</td>
		<td>${cstm.description }</td>
		<td>
			<a href="<c:url value='/CustomerServlet?method=preEdit&cid=${cstm.cid }'/>">编辑</a>
			<a href="<c:url value='/CustomerServlet?method=delete&cid=${cstm.cid}'/>">删除</a>
		</td>
	</tr>
	</c:forEach>

</table>
  <br/>
  <%--
   设计分页相关的链接
   --%>
  <center>
	  第${pageBean.pageCode}页/共${pageBean.totalPage}页
	  <a href="${pageBean.url}&pageCode=1">首页</a>
	  <c:if test="${pageBean.pageCode > 1}">
	  <a href="${pageBean.url}&pageCode=${pageBean.pageCode-1}">上一页</a>
	  </c:if>

	  <%--计算begin,end--%>
	  <c:choose>
		  <%--	如果总页数<=10，begin=1,end=最大页数	--%>
		  <c:when test="${pageBean.totalPage <= 10}">
			  <c:set var="begin" value="1"/>
			  <c:set var="end" value="${pageBean.totalPage}"/>
		  </c:when>
		  <%--	如果总页数>10 ,借助公式计算begin,end --%>
		  <c:otherwise>
			  <c:set var="begin" value="${pageBean.pageCode-5}"/>
			  <c:set var="end" value="${pageBean.pageCode+4}"/>
			  <%-- 头溢出 --%>
			  <c:if test="${begin<1}">
				  <c:set var="begin" value="1"/>
				  <c:set var="end" value="10"/>
			  </c:if>
			  <%--  尾溢出--%>
			  <c:if test="${end>pageBean.totalPage}">
				  <c:set var="begin" value="${pageBean.totalPage - 9}"/>
				  <c:set var="end" value="${pageBean.totalPage}"/>
			  </c:if>
		  </c:otherwise>
	  </c:choose>

	  <%-- 在页面上循环显示页码列表 --%>
	  <c:forEach var="i" begin="${begin}" end="${end}">
		  <%--	若i=pageCode，当前页码就只以普通文本显示 --%>
		  <c:choose>
			  <c:when test="${i eq pageBean.pageCode}">
				  ${i}
			  </c:when>
			  <c:otherwise>
				  <a href="${pageBean.url}&pageCode=${i } ">[${i }]</a>
			  </c:otherwise>
		  </c:choose>
	  </c:forEach>


	<c:if test="${pageBean.pageCode < pageBean.totalPage}">
	  <a href="${pageBean.url}&pageCode=${pageBean.pageCode+1}">下一页</a>
	</c:if>
		<a href="${pageBean.url}&pageCode=${pageBean.totalPage}">尾页</a>
  </center>

  </body>
</html>
