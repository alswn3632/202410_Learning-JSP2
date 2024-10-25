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
	<h1>Board List Page</h1>
	<table>
		<thead>
			<tr>
				<th>.</th>
				<th>제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th>조회수</th>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach items="${list }" var="bvo">
				<tr>
					<td>${bvo.bno }</td>
					<td><a href="/brd/detail?bno=${bvo.bno }">
						<img alt="" src="/_fileUpload/_th_${bvo.imageFile }" style="width: 75px; height: 75px;">
						${bvo.title }
					</a></td>
					<td>${bvo.writer }</td>
					<td>${bvo.regdate }</td>
					<td>${bvo.readCount }</td>
				</tr>
			</c:forEach>			
		</tbody>
	</table>
</body>
</html>