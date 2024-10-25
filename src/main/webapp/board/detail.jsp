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
		<h1>Board Detail Page</h1>
		<hr>
		<h3>${bvo.bno }. ${bvo.title } (${bvo.readCount })</h3>	
		<h4>${bvo.writer } | ${bvo.regdate }</h4>
		<img alt="" src="/_fileUpload/${bvo.imageFile }" style="width: auto; height: 300px;">
		<h4>${bvo.content }</h4>
		<!-- 수정 버튼을 통해 내 정보를 가지고 이동 -->
		<a href="/brd/modify?bno=${bvo.bno }"><button type="button">수정</button></a>
		<a href="/brd/delete?bno=${bvo.bno }"><button type="button">삭제</button></a>
		
		<hr>
		<!-- 댓글 -->
		<div>
			<h3>Comment Line</h3>
			<!-- name/value는 해당하는 값을 동기로 가져갈 때 쓰는 것 -->
			<!-- js를 활용하기 위한 id를 이용 -->
			<input type="text" id="cmtWriter" placeholder="작성자" value="${ses.id }" readonly="readonly"><br>
			<input type="text" id="cmtText" placeholder="내용"><br>
			<button type="button" id="cmtAddBtn">등록</button> 
		</div>		
		<br>
		<hr>
		<!-- 댓글 출력 -->
		<div id="commentLine">
			<div>
				<div>
					cno, bno, writer, regdate
				</div>
				<div>
					<button>수정</button>
					<button>삭제</button><br>
					<input type="text" value="content">
				</div>
			</div>
		</div>
		
		<!-- 반드시 순서 지켜줘야함 -->
		<script type="text/javascript">
			const bnoVal = `<c:out value="${bvo.bno }" />`;
			console.log(bnoVal);
		</script>
		
		<script type="text/javascript" src="/resources/board_detail.js"></script>
		
		<!-- 순서지켜! -->
		<script type="text/javascript">
			printList(bnoVal);
		</script>
		
</body>
</html>