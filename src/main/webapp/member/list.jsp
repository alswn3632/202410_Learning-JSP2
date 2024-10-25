<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>User List Page</h1>
		<table>
		<thead>
			<tr>
				<th>prifile</th>
				<th>id</th>
				<th>pwd</th>
				<th>email</th>
				<th>phone</th>
				<th>regdate</th>
				<th>lastlogin</th>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach items="${list }" var="mvo">
				<tr>
					<td><img alt="" src="/_fileUpload/_th_${mvo.imageFile }" style="width: auto; height: 75px;"></td>
					<td>${mvo.id }</td>
					<td>${mvo.pwd }</td>
					<td>${mvo.email }</td>
					<td>${mvo.phone }</td>
					<td>${mvo.regdate }</td>
					<td>${mvo.lastlogin }</td>
				</tr>
			</c:forEach>			
		</tbody>
	</table>
</body>
</html>