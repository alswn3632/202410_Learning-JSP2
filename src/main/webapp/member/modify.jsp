<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>User Info-Modify Page</h1>
	
	<form action="/mem/update" method="post" enctype="multipart/form-data">
		<img alt="" src="/_fileUpload/${mvo.imageFile }" style="width: auto; height: 300px;"> <br>
		id : <br>
		<input type="text" name="id" value="${mvo.id }" readonly="readonly"><br>
		pw : <br>
		<input type="text" name="pwd" value="${mvo.pwd }"><br>
		email : <br>
		<input type="text" name="email" value="${mvo.email }"><br>
		phone : <br>
		<input type="text" name="phone" value="${mvo.phone }"><br>
		<input type="file" name="newFile" accept="image/jpg, image/gif, image/png, image/jpeg"> <br>
		<button type="submit">완료</button>
	</form>
	<a href="/mem/delete?id=${mvo.id }"><button type="button">회원탈퇴</button></a>
	
</body>
</html>