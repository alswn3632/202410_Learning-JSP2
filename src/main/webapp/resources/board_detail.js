/** 우왕 자바스크립트당!
 * 
 */

console.log("board_detail.js in!!");
console.log(bnoVal);

// 댓글 등록 
// 등록 버튼을 누르면 작성자와 댓글 내용의 값을 객체로 생성 => controller로 전송
document.getElementById('cmtAddBtn').addEventListener('click', ()=>{
	let cmtWriter = document.getElementById('cmtWriter').value;
	let cmtText = document.getElementById('cmtText').value;
	if(cmtText == null || cmtText == ''){
		alert("댓글을 입력해주세요.");
		return false;
	}
	
	// 댓글 객체 생성
	let cmtData = {
		bno : bnoVal,
		writer : cmtWriter,
		content : cmtText		
	}
	
	// 댓글을 비동기로 전송
	postCommentToServer(cmtData).then(result => {
		console.log(result); // isOk
		if(result == '1'){
			alert("댓글 등록 성공!");
		}else{
			alert("댓글 등록 실패!");
		}
		
		// 댓글 출력
		printList(bnoVal);
	});
	
});


function printList(bnoVal){
	getCommentListFromServer(bnoVal).then(result => {
		console.log(result);
		if(result.length > 0){
		printCommentList(result);			
		}else{
			let div = document.getElementById('commentLine');
			div.innerHTML = `<div>comment가 없습니다.</div>`
		}
	});
}

function printCommentList(result){
	let div = document.getElementById('commentLine');
	div.innerText = ''; // 기존에 값이 있다면 구조 지우기!
	for(let i=0; i<result.length; i++){
		let html = `<div>`;
		html += `<div>${result[i].cno}, ${result[i].bno}, ${result[i].writer}, ${result[i].regdate}</div>`;
		html += `<div>`;
		html += `<button type="button" data-cno="${result[i].cno}" class="cmtModBtn">수정</button>`;
		html += `<button type="button" data-cno="${result[i].cno}" class="cmtDelBtn">삭제</button><br>`;
		html += `<input type="text" class="cmtText" id="${result[i].cno}" value="${result[i].content}">`;
		html += `</div></div><hr>`;
		div.innerHTML += html; // 각 댓글 객체를 누적해서 담기
	}
}

/*
<div id="commentLine">

			<button>삭제</button><br>
			<input type="text" value="content">
		</div>
	</div>
</div>
*/

// 화면에서 데이터를 만들어서 보내는 방법 = post
// 데이터를 보낼 때 method = post, headers(Content-Type), body를 작성해서 전송
// 서버에서 데이터를 주는 방법 = get

// Content-Type : application/json; charset=utf-8

async function postCommentToServer(cmtData){
	try{
		console.log(cmtData);
		const url = "/cmt/post";
		const config = {
			method : 'post',
			headers : {
				'Content-Type' : 'application/json; charset=utf-8'
			},
			body : JSON.stringify(cmtData)
		};
		const resp = await fetch(url, config);
		const result = await resp.text(); // isOk 값을 text로 리턴
		return result;
	}catch(err){
		console.log(err);
	}
}

// list 가져오기 : 내 게시글에 달린 댓글만 가져와야 함 -> get! (생략가능) 
async function getCommentListFromServer(bno){
	try{
		// bno 하나만 가져가면 되니까 쿼리스트링으로!
		const resp = await fetch("/cmt/list?bno=" + bno);
		const result = await resp.json(); // 댓글 리스트 [{...},{...},{...}]
		return result;
	}catch(err){
		console.log(err);
	}
}

async function updateCommentToServer(cmtData){
	// 수정 : cno, content 객체를 보내서 isOk return => post
	try{
		const url = "/cmt/modify";
		const config = {
			method : 'post',
			headers : {
				'Content-Type' : 'application/json; charset=utf-8'
			},
			body : JSON.stringify(cmtData)
		}
		const resp = await fetch(url, config);
		const result = await resp.text();
		return result;
	}catch(err){
		console.log(err);
	}
	
}

// 수정/삭제
document.addEventListener('click', (e)=>{
	console.log(e.target);
	console.log(e.target.dataset.cno);
	
	// 수정
	if(e.target.classList.contains('cmtModBtn')){
		let cnoVal = e.target.dataset.cno;
		// 방법1. cno 값을 id로 사용할 경우
		let cmtText = document.getElementById(cnoVal).value;
		console.log(cmtText);
		
		// 방법2. 내 타켓을 기준으로 가장 가까운 div를 찾기 closest('div')
		/*let div = e.target.closest('div');
		console.log(div); // 내 댓글 객체 찾기 
		let cmtText2 = div.querySelector('.cmtText').value; // 찾은 댓글 div에서 cmtText 클래스명 갖고있는 것의 value
		console.log(cmtText2);*/
		
		let cmtData = {
			cno : cnoVal,
			content : cmtText
		}
		
		updateCommentToServer(cmtData).then(result =>{
			console.log(result);
		})
		
		

		
	}

	// 삭제
	if(e.target.classList.contains('cmtDelBtn')){
		let cnoVal = e.target.dataset.cno;
		
		deleteCommentToServer(cnoVal).then(result =>{
			console.log(result);
		})
	}
	
	
	printList(bnoVal);
});

// 삭제 
async function deleteCommentToServer(cno){
	try{
		const resp = await fetch("/cmt/delete?cno=" + cno);
		const result = await resp.text();
		return result;
	}catch(err){
		console.log(err);
	}
}

// 데이터 좀 먹어라!
