<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
		<h1>Board Modify Page</h1>
		<hr>
		<form action="/brd/update" method="post" enctype="multipart/form-data">
			<input type="hidden" name="bno" value="${bvo.bno }" />
			<h3>${bvo.bno }. <input type="text" name="title" value="${bvo.title }"></h3>	
			<h4>${bvo.writer } | ${bvo.regdate }</h4>
			<img alt="" src="/_fileUpload/${bvo.imageFile }" style="width: auto; height: 300px;"> <br>
			<textarea rows="10" cols="50" name="content" >${bvo.content }</textarea> <br>
			<!-- 파일 변경 -->
			file :
			<input type="hidden" name="imageFile" value="${bvo.imageFile }">
			<input type="file" name="newFile" accept="image/jpg, image/gif, image/png, image/jpeg"> <br>
			<!-- 완료 버튼을 누르면 내가 수정한 내용을 가지고 컨트롤러로 이동할 것 >> form 태그 안에 있어야함 -->
			<button type="submit">완료</button>
			<!-- 목록 버튼을 누르면 리스트 페이지로 이동 즉, 둘의 버튼이 하는 일이 다름 >> 위치 상관x type이 버튼이어야함 -->
			<a href="/brd/list"><button type="button">목록</button></a>
		</form>
</body>
</html>