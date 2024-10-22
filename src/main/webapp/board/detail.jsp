<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
		<h1>Board Detail Page</h1>
		<hr>
		<h3>${bvo.title }</h3>	
		<h4>${bvo.writer } | ${bvo.regdate }</h4>
		<h4>${bvo.content }</h4>
		<button>수정</button>
		<button>삭제</button>
</body>
</html>