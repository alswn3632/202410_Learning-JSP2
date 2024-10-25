/**
 * 
 */

document.getElementById("duplTest").addEventListener('click', ()=>{
	const testId = document.getElementById("id").value;
	console.log(testId);
	
	getIdToServer(testId).then(result =>{
		console.log(result);
		if(result == -1){
			alert("사용할 수 없는 아이디입니다.");
			document.getElementById("id").value = "";
		}
	})
});

async function getIdToServer(id){
	try{
		const resp = await fetch ("/mem/duplTest?id=" + id);
		const result = await resp.text();
		return result;
	}catch(err){
		console.log(err)
	}
	
}