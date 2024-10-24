package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import handler.FileRemoveHandler;
import net.coobird.thumbnailator.Thumbnails;
import service.BoardService;
import service.BoardServiceImpl;

@WebServlet("/brd/*") // 아까 수정한 경로 여기에 표시됨!
public class BoardController extends HttpServlet {
	//멤버변수 
	private static final long serialVersionUID = 1L; // 건들지 말 것!
	// 로그 객체 생성
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
	// JSP에서 받은 요청을 처리하고, 그 결과를 다른 JSP로 보내는 역할을 하는 객체
	private RequestDispatcher rdp;
	// 응답할 JSP의 주소를 저장하는 변수
	private String destPage;
	// DB 구문 체크 값 저장 변수
	private int isOk;
	// 일단 만들고! interface로 생성하고 연결 >> 이게 뭐냐! 컨트롤러는 서비스와 연결이잖냐 그거임ㅇㅇ
	private BoardService bsv;
	// 파일 저장 경로
	private String savePath; 
       
	//생성자
    public BoardController() {
    	//service 인터페이스 만들고 넘어와서
    	bsv = new BoardServiceImpl(); 
    	// 도 일단 만들고! 이번엔 class로 생성 (service pakage에 ) bsv 구현체 연결
    }

    //메서드
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 실제 get, post 요청 처리하는 곳
		log.info("log 객체 test");
		
		// request, response 객체의 인코딩 설정
		request.setCharacterEncoding("utf-8"); // 요청 객체
		response.setCharacterEncoding("utf-8"); // 응답 객체
		// response는 jsp로 갈 응답 객체 >> 화면을 생성해서 응답 >> jsp 형식으로 응답 
		// text/html; charset=UTF-8
		response.setContentType("text/html; charset=UTF-8"); //html5 형식으로 보내라!
		
		// http://localhost:8088/brd/register
		// 경로 가져오기 /brd/register
		String uri = request.getRequestURI();
		log.info(uri); 
		// 여기서 register 부분을 꺼내서 쓸꺼임
		String path = uri.substring(uri.lastIndexOf("/")+1);
		log.info(path);
		
		switch(path) {
		case "register" : 
			// 정보가 필요하다면 정보를 DB에서 요청하여 request를 객체에 담아 보내기
			destPage = "/board/register.jsp";
			break;
			
		case "insert" :
			try {
				log.info("insert case in!!!");
				// jsp 화면에서 보내온 파라미터 값을 저장해서 DB로 전송
				// 첨부파일이 없을 경우 처리
//				String title = request.getParameter("title");
//				String writer = request.getParameter("writer");
//				String content = request.getParameter("content");
//				BoardVO bvo = new BoardVO(title, writer, content);
//				log.info(">>>>>>> bvo insert 객체 >>>>>> {}", bvo);
				
				// 첨부파일이 있는 경우 처리 
				// bvo를 구성하여 DB로 전송하는건 똑같고 
				// file을 저장하는 작업 = 파일 이름만 imageFile에 저장
				// 첨부파일 형식으로 들어오게 되면 모든 파라미터는 바이트 단위로 분해되서 전송
				// 바이트 단위로 전송된 파라미터의 값을 String으로 조합해야함.
				
				// 1. 파일을 업로드할 물리적인 경로 설정
				savePath = getServletContext().getRealPath("/_fileUpload");
				log.info(savePath); // D:\aws_chaminjoo\jsp_workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\jsp_study\_fileUpload
				
				File fileDir = new File(savePath);
				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				// 파일을 가져오기 위한 임시 메모리 설정 
				fileItemFactory.setSizeThreshold(1024*1024*3); // 3MB
				// 파일의 저장위치를 담은 file 객체
				fileItemFactory.setRepository(fileDir);
				
				BoardVO bvo = new BoardVO(); // 빈 객체 만들어 setter 사용
				
				// multipart/form-data 형식으로 넘어온 request 객체를 다루기 쉽게 변환해주는 객체
				ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
				// request 객체를 FileItem 형식의 리스트로 리턴
				List<FileItem> itemList = fileUpload.parseRequest(request);
				// title, writer, content => text(String) / imageFile => image
				for(FileItem item : itemList) {
					log.info("fileItem >> {}", item.toString());
					switch(item.getFieldName()) {
					case "title" :
						bvo.setTitle(item.getString("utf-8"));
						break;
					case "writer" :
						bvo.setWriter(item.getString("utf-8"));
						break;
					case "content" : 
						bvo.setContent(item.getString("utf-8"));
						break;
					case "imageFile" : 
						// 이미지는 파일 여부를 체크하여 
						if(item.getSize() > 0) {
							// 파일 이름 추출
							String fileName = item.getName();
							// 경로 + ~~~ / dog.jpg 라면? 처리
							// 경로 빼고 이름만 가져오기
							// String fileName2 = item.getName().substring(item.getName().lastIndexOf(File.separator) + 1);
							// File.separator : 파일 경로 기호 -> 운영체제마다 다를 수 있어 자동 변환
							
							// 시스템의 시간을 이용하여 파일을 구분 / 시간_dog.jpg
							// UUID를 사용하여 구분하는 방법도 있음 ( >> 더 많이 사용)
							fileName = System.currentTimeMillis() + "_" + fileName;
							
							// 아까 경로 만들어 놓은 파일 fileDir + / + fileName
							File uploadFilePath = new File(fileDir + File.separator + fileName);
							log.info(">>>> uploadFilePath > {}", uploadFilePath.toString());
							
							// 저장
							try {
								item.write(uploadFilePath); // 객체를 디스크에 쓰기
								bvo.setImageFile(fileName); // bvo에 저장할 값 (DB에 들어가는 값)
								
								// 썸네일 작업 : 리스트 페이지에서 트래픽 과다 사용 방지
								Thumbnails.of(uploadFilePath)
									.size(75, 75)
									.toFile(new File(fileDir + File.separator + "_th_" + fileName));
							} catch (Exception e) {
								log.info(">>>> file writer on disk error");
								e.printStackTrace();
							}
						}
						break;
					}
				}
				
				isOk = bsv.register(bvo);
				log.info(">>>> bvo insert " + (isOk>0? "성공":"실패"));
				// 처리 후 목적지
				destPage = "/index.jsp";
			}catch(Exception e) {
				log.info("insert error!");
				e.printStackTrace();
			}			
			break;
			
		case "list" : 
			try {
				// 전체 리스트를 가지고 list.jsp로 전달
				List<BoardVO> list = bsv.getList();
				
				log.info(">>>> list >>>> {}", list);
				// request 객체에 파라미터로 값을 보내는 방법
				request.setAttribute("list", list);
				// 처리 후 목적지
				destPage = "/board/list.jsp";

			} catch (Exception e) {
				log.info("list error!");
				e.printStackTrace();
			}
			break;
			
		case "detail" : case "modify" :
			//? 달린 쿼리스트링은 같이 오지 않음
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				BoardVO bvo = bsv.getDetail(bno);
				log.info(">>>> bvo >>>> {}", bvo);
				request.setAttribute("bvo", bvo);
				
				if(path.equals("detail")) {
					int isOk = bsv.countUp(bno);				
				}
				
//				//방법1
//				if(path.equals("detail")) {
//					destPage="/board/detail.jsp";					
//				}else {
//					destPage="/board/modify.jsp";
//				}
//				
				//방법2 : path명과 jsp 이름이 같다면 사용할 수 있음
				destPage = "/board/" + path + ".jsp";
				
			} catch (Exception e) {
				log.info("detail error!");
				e.printStackTrace();
			}
			break;
			
		case "update" :
			try {
				// 첨부 파일 이전 처리 방식
//				int bno = Integer.parseInt(request.getParameter("bno"));
//				String title = request.getParameter("title");
//				String content = request.getParameter("content");
//				BoardVO bvo = new BoardVO(bno, title, content);
				
				// 첨부 파일 처리 포함 방식
				savePath = getServletContext().getRealPath("_fileUpload");
				File fileDir = new File(savePath);
				
				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				fileItemFactory.setSizeThreshold(1014*1024*3); // 3MB 정도 설정
				fileItemFactory.setRepository(fileDir);
				
				BoardVO bvo = new BoardVO();
				
				ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
				List<FileItem> itemList = fileUpload.parseRequest(request);
				
				// 원래 이미지 파일이 있었는데 변경하는 케이스
				// 없었는데 새롭게 추가하는 케이스
				String old_file = null;
				for(FileItem item : itemList) {
					switch(item.getFieldName()) {
					case "bno" : 
						bvo.setBno(Integer.parseInt(item.getString("utf-8")));
						break;
					case "title" :
						bvo.setTitle(item.getString("utf-8"));
						break;
					case "content" :
						bvo.setContent(item.getString("utf-8"));
						break;
					case "imageFile" :
						// 기존 파일 > 있을 수도 있고 없을 수도 있음
						old_file = item.getString("utf-8");
						break;
					case "newFile" :
						// 새로 추가된 파일 > 있을 수도 있고 없을 수도 있음.
						if(item.getSize()>0) {
							if(old_file != null) {
								// 기존 파일이 있다면...
								// 파일 삭제 작업 : 별도 핸들러로 작업
								FileRemoveHandler fileHandler = new FileRemoveHandler();
								isOk = fileHandler.deleteFile(savePath, old_file);
							}
							// 새로운 파일을 등록 작업
							String fileName = item.getName();
							fileName = System.currentTimeMillis()+"_"+fileName;
							
							File uploadFilePath = new File(fileDir+File.separator+fileName);
							try {
								item.write(uploadFilePath);
								bvo.setImageFile(fileName); // 파일 이름 바꾸기
								Thumbnails.of(uploadFilePath) // 기준 파일
									.size(75, 75) // 사이즈
									.toFile(new File(fileDir + File.separator + "_th_" + fileName));
							} catch (Exception e) {
								log.info(">>>> file writer update error");
								e.printStackTrace();							
							}
							
						}else {
							// 기존 파일은 있는데 새로운 이미지 파일이 없다면...
							// 기존 객체를 담기
							bvo.setImageFile(old_file);
						}
						break;
					}
				}
				
				
				int isOk = bsv.update(bvo);
				log.info(">>>> bvo insert " + (isOk>0? "성공":"실패"));
				// 컨트롤러 내부 케이스는 /brd/ 를 따로 적을 필요가 없음!
				destPage = "detail?bno=" + bvo.getBno(); 
			} catch (Exception e) {
				log.info("update error!");
				e.printStackTrace();
			}
			break;
			
		case "delete" :
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				int isOk = bsv.delete(bno);
				log.info(">>>> bvo insert " + (isOk>0? "성공":"실패"));
				destPage = "list"; 
			} catch (Exception e) {
				log.info("delete error!");
				e.printStackTrace();			}
			break;
			
		}
		
		// 목적지 주소(destPage)로 데이터를 전달(RequestDispatcher)
		rdp = request.getRequestDispatcher(destPage);
		// 요청에 필요한 객체를 가지고, destPage에 적힌 경로로 이동
		rdp.forward(request, response);
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get으로 들어오는 요청을 처리하는 메서드 > service를 호출하여 처리
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// post로 들어오는 요청을 처리하는 메서드 > service를 호출하여 처리
//		doGet(request, response);
		service(request, response);
	}

}
