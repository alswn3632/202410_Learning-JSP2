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
	<h1>Project Main</h1>
	<!-- JSP의 모든 경로는 controller로 이동되게 해야함 -->
	
	<h4><a href="/mem/join">member 회원가입 페이지로 이동</a></h4>
	<div>
		<c:if test="${ses.id eq null }">
			<form action="/mem/login" method="post">
				id : <input type="text" name="id"> pwd : <input type="password" name="pwd">
				<button type="submit">로그인</button>
			</form>
		</c:if>
	</div>
	
	<div>
		<!-- 로그인 이후 나와야 하는 정보 : ses 객체가 있는지 없는지 확인 -->
		<!-- eq:equals ne:not equals -->
			<c:if test="${ses.id ne null }">
				<img alt="" src="/_fileUpload/${ses.imageFile }" style="width: auto; height: 150px;"> <br>
				${ses.id }님이 로그인하셨습니다!<br>
				email : ${ses.email } / phone : ${ses.phone } <br>
				계정 생성일 : ${ses.regdate } / 마지막 접속일 : ${ses.lastlogin }<br>
				<a href="/mem/detail?id=${ses.id }"><button type="button">회원정보수정</button></a>
				<c:if test="${ses.id eq 'admin' }">
					<a href="/mem/list"><button type="button">회원리스트</button></a>
				</c:if>
				<a href="/mem/logout"><button type="button">로그아웃</button></a>
				<h3>
					<a href="/brd/register">board 글쓰기 페이지로 이동</a>
				</h3>
			</c:if>
	</div>
	<h3>
		<a href="/brd/list">board 리스트 페이지로 이동</a>
	</h3>
	
	<script type="text/javascript">
		const msg_login = `<c:out value="${msg_login}" />`;
		if(msg_login == -1){
			alert("로그인 정보가 일치하지 않습니다.");
		}
	</script>
</body>
</html>