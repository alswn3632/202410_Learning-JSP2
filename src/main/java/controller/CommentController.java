package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.CommentVO;
import service.CommentService;
import service.CommentServiceImpl;

@WebServlet("/cmt/*")
public class CommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(CommentController.class);
	// board 와 다른 점
	// 동기 통신이 아닌 비동기 통신(데이터만 요청한 곳으로 보내는 방식)
	// RequestDispatcher, destPage가 필요하지 않음
	private CommentService csv;
       
    public CommentController() {
        super();
        csv = new CommentServiceImpl();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 실제로 처리하는 곳
		// 인코딩
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		// 비동기는 setContentType 설정할 필요 없음
		
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/")+1);
		log.info(">>>> cmt path > {}", path);
		
		switch(path) {
		case "post" :
			try {
				// JS에서 보낸 데이터를 읽어들이는 작업
				// JS('Object') -> controller(string) -> CommentVO로 변환해야함!
				// {bno: '7', writer: 'ㅁㅁ', content: 'ㄴㄴ'}
				StringBuffer sb = new StringBuffer();
				String line = "";
				BufferedReader br = request.getReader(); // request의 bod 값을 전송
				while((line = br.readLine()) != null) {
					sb.append(line);
				}

				log.info(">>>> sb > {}", sb.toString());
				
				// CommentVO 객체로 생성하기 // json-simple 라이브러리에서 사용
				JSONParser parser = new JSONParser();
				JSONObject jsonObj = (JSONObject)parser.parse(sb.toString());
				
				log.info(">>>> jsonObhj > {}", jsonObj);
				// key : value 형태로 jsonObj 구성
				int bno = Integer.parseInt(jsonObj.get("bno").toString());
				String writer = jsonObj.get("writer").toString();
				String content = jsonObj.get("content").toString();
				
				CommentVO cvo = new CommentVO(bno, content, writer);
				int isOk = csv.post(cvo);
				log.info(">>>> post > {}", (isOk>0? "성공" : "실패"));
				
				// 결과 데이털르 전송 - 화면으로 전송 (respnse 객체에 body에 기록)
				PrintWriter out = response.getWriter();
				out.print(isOk);
			
				
			} catch (Exception e) {
				log.info("comment post error!!");
				e.printStackTrace();
			}
			break;
		
		case "list" : 
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				List<CommentVO> list = csv.getList(bno);
				log.info(">>>> list > {}", list);
				// list를 json 형태로 변환하여 보내야함
				// 가져온 list를 [{...},{...}] 형식으로 바꿔야함 >> JSONArray
				// {...} :  JSONObject
				JSONArray jsonArray = new JSONArray();
//				JSONObject[] jsonObjArr = new JSONObject[list.size()];
				
				for(int i=0; i<list.size(); i++) {					
//					jsonObjArr[i] = new JSONObject();
//					jsonObjArr[i].put("cno", list.get(i).getCno());
					
					JSONObject json = new JSONObject();
					json.put("cno", list.get(i).getCno());
					json.put("bno", list.get(i).getBno());
					json.put("writer", list.get(i).getWriter());
					json.put("content", list.get(i).getContent());
					json.put("regdate", list.get(i).getRegdate());
					
//					jsonObjArr[i] = json;
					
					jsonArray.add(json);
					
				}
				// [{...},{...},{...}] -> jsonArrat를 String으로 변환하여 전송하기
				String jsonData = jsonArray.toJSONString();
				
				// print
				PrintWriter out = response.getWriter();
				out.print(jsonData);
			} catch (Exception e) {
				log.info("comment list error!!");
				e.printStackTrace();			
			}
			break;
		case "modify" :
			try {
				// JS에서 보낸 데이터 가져오기
				StringBuffer sb = new StringBuffer();
				String line = "";
				BufferedReader br = request.getReader(); // req.body
				while((line = br.readLine()) != null ) {
					sb.append(line); // 한줄씩 추가
				}
				log.info(">>>> sb > {}", sb.toString());
				
				// 객체로 파싱하기
				JSONParser p = new JSONParser();
				JSONObject j = (JSONObject)p.parse(sb.toString());
				
				log.info(">>>> JSONObject > {}", j);
				
				// 댓글 객체 생성하기
				int cno = Integer.parseInt(j.get("cno").toString());
				String content = j.get("content").toString();
				CommentVO cvo = new CommentVO(cno, content);
				int isOk = csv.modify(cvo);
				log.info(">>>> modify > {}",(isOk>0? "성공":"실패"));
				
				// 결과를 JS로 전송
				PrintWriter w = response.getWriter();
				w.print(isOk);

						
				
			} catch (Exception e) {
				log.info("comment modify error!!");
				e.printStackTrace();				
			}
			break;
			
		case "delete" :
			try {
				int cno = Integer.parseInt(request.getParameter("cno"));
				int isOk = csv.delete(cno);
				log.info(">>>> delete > {}",(isOk>0? "성공": "실패"));
				
				PrintWriter w = response.getWriter();
				w.print(isOk);
				
			} catch (Exception e) {
				log.info("comment delete error!!");
				e.printStackTrace();				
			}
			break;
		}
		
		// 목적지를 가지고 작업할 필요가 없지!
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
		service(request, response);
	}

}
