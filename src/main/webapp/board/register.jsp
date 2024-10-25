<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Board Register Page</h1>
	<!-- 일반적으로 등록/수정은 post로 나머지는 get 방식 -->
	<!-- enctype="multipart/form-data" : 첨부파일을 가져갈 때 사용하는 타입 -->
	<!-- 파일종류 : 이미지 파일만, 1개만 가능하도록 -->
	<!-- 파일 종류 중 이미지 파일 : image/png, image/jpg, image/gif -->
	<form action="/brd/insert" method="post" enctype="multipart/form-data">
		title: <br>
		<input type="text" name="title" placeholder="제목"> <br>
		writer: <br>
		<input type="text" name="writer" value="${ses.id }" readonly="readonly"><br>
		content: <br>
		<textarea rows="10" cols="50" name="content" placeholder="내용"></textarea> <br>
		file :
		<input type="file" name="imageFile" accept="image/jpg, image/gif, image/png, image/jpeg"> <br>
		<button type="submit">등록</button>
	</form>
</body>
</html>