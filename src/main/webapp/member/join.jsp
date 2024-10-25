<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>User Join Page</h1>
	
	<form action="/mem/register" method="post" enctype="multipart/form-data">
		id : <br>
		<input type="text" id="id" name="id" placeholder="id.."> <button type="button" id="duplTest">중복검사</button><br>
		pw : <br>
		<input type="password" name="pwd" placeholder="pw.."><br>
		email : <br>
		<input type="text" name="email" placeholder="email.."><br>
		phone : <br>
		<input type="text" name="phone" placeholder="phone.."><br>
		<input type="file" name="imageFile" accept="image/jpg, image/gif, image/png, image/jpeg"> <br>
		<button type="submit">join</button>
	</form>
	
	<script type="text/javascript" src="/resources/member_join.js"></script>
</body>
</html>