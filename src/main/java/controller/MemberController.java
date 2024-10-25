package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.MemberVO;
import handler.FileRemoveHandler;
import net.coobird.thumbnailator.Thumbnails;
import service.MemberService;
import service.MemberServiceImpl;

@WebServlet("/mem/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 로그인은 동기 방식
	// 로그 객체
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	// 동기 방식 : requestDispatcher : 요청에 대한 응답 데이터를 JSP(화면)으로 전송하는 역할
	private RequestDispatcher rdp;
	// 목적지 주소
	private String destPage;
	private int isOk;
  
	private MemberService msv;
	private String savePath;
	
    public MemberController() {
        msv = new MemberServiceImpl();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 기본 설정
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		
		// 요청 주소 추출
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/")+1);
		log.info(">>>> member path > {}", path);
		
		switch(path) {
		case "join" :
			destPage = "/member/join.jsp";
			break;
		case "register" :
			// JSP에서 온 파라미터 받기
			log.info("member register case in!");
			try {
//				String id = request.getParameter("id");
//				String pwd = request.getParameter("pwd");
//				String email = request.getParameter("email");
//				String phone = request.getParameter("phone");
//				MemberVO mvo = new MemberVO(id, pwd, email, phone);
//				log.info(">>>> mvo 객체 >>>> {}", mvo);
				// member 객체 생성 후 service에게 등록 요청
				
				// 프로필 사진 업로드
				savePath = getServletContext().getRealPath("_fileUpload");
				log.info(savePath);
				
				File fileDir = new File(savePath);
				DiskFileItemFactory fif = new DiskFileItemFactory();
				fif.setSizeThreshold(1024*1034*3);
				fif.setRepository(fileDir);
				
				MemberVO mvo = new MemberVO();
				
				ServletFileUpload ful = new ServletFileUpload(fif);
				List<FileItem> itemList = ful.parseRequest(request);
				
				for(FileItem item : itemList) {
					log.info(">>> fileItem > {}", item.toString());
					switch(item.getFieldName()) {
					case "id" :
						mvo.setId(item.getString("utf-8"));
						break;
					case "pwd" :
						mvo.setPwd(item.getString("utf-8"));
						break;
					case "email" :
						mvo.setEmail(item.getString("utf-8"));
						break;
					case "phone" :
						mvo.setPhone(item.getString("utf-8"));
						break;
					case "imageFile" :
						if(item.getSize() > 0) {
							String fileName = item.getName();
							fileName = System.currentTimeMillis() + "_" + fileName;
							File uploadFilePath = new File(fileDir + File.separator + fileName);
							log.info(">>>> uploadFilePath > {}", uploadFilePath.toString());
							
							try {
								item.write(uploadFilePath); // 객체를 디스크에 쓰기
								mvo.setImageFile(fileName); //

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
				
				isOk = msv.register(mvo);
				log.info(">>>> join {}", (isOk>0? "성공" : "실패"));
				destPage="/index.jsp";	
			} catch (Exception e) {
				log.info("member register case error!");
				e.printStackTrace();
			}
			break;
			
		case "login" :
			try {
				// 로그인은 id와 password를 파라미터로 받아서 DB에 해당 id가 있는지 확인, pw가 일치하는지 확인
				// 정보를 가져와서 session 객체에 저장
				// session : 모든 JSP에 공유되는 객체 / 브라우저가 종료되면 삭제됨
				// ${변수명}
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");
				
				MemberVO mvo = new MemberVO(id, pwd);
				MemberVO loginMvo = msv.login(mvo);
				// 로그인 정보가 잘못되면 loginMvo는 null
				log.info(">>>> loginMvo > {}", loginMvo);
				if(loginMvo != null) {
					// 로그인이 성공
					// 세션에 저장
					HttpSession ses = request.getSession();
					ses.setAttribute("ses", loginMvo);
					// 로그인 유지 시간 초 단위로 설정
					ses.setMaxInactiveInterval(10*60); // 10분
				}else {
					// 로그인이 실패
					request.setAttribute("msg_login", -1);
					// index.jsp로 메세지 전송
				}
				destPage = "/index.jsp";
			} catch (Exception e) {
				log.info("member login case error!");
				e.printStackTrace();
			}
			break;
			
		case "logout" :
			try {
				// session에 값이 있다면 해당 세션을 끊어주어야함!
				HttpSession ses = request.getSession();
				MemberVO mvo = (MemberVO)ses.getAttribute("ses");
				log.info(">>>> ses에서 추출한 mvo > {}", mvo);
				
				// laselogin update
				isOk = msv.lastLogin(mvo.getId());
				log.info(">>>> lastLogin update > {}", (isOk>0? "성공":"실패"));
				ses.invalidate(); // 세션을 무효화 시킴(세션 끊기)
				destPage = "/index.jsp";
			} catch (Exception e) {
				log.info("member logout case error!");
				e.printStackTrace();			
			}
			break;
		
		case "detail" :
			try {
				String id = request.getParameter("id");
				MemberVO mvo = msv.getDetail(id);
				request.setAttribute("mvo", mvo);
				
				destPage = "/member/modify.jsp";
				
			} catch (Exception e) {
				log.info("member detail case error!");
				e.printStackTrace();	
			}
			break;
			
		case "update" :
			try {
//				String id = request.getParameter("id");
//				String pwd = request.getParameter("pwd");
//				String email = request.getParameter("email");
//				String phone = request.getParameter("phone");
//				MemberVO mvo = new MemberVO(id, pwd, email, phone);
//				log.info(">>>> mvo 객체 >>>> {}", mvo);
				
				// 첨부 파일 처리 방식
				savePath = getServletContext().getRealPath("_fileUpload");
				File fileDir = new File(savePath);
				
				DiskFileItemFactory fif = new DiskFileItemFactory();
				fif.setSizeThreshold(1024*1024*3);
				fif.setRepository(fileDir);
				
				MemberVO mvo = new MemberVO();
				
				ServletFileUpload ful = new ServletFileUpload(fif);
				List<FileItem> itemList = ful.parseRequest(request);
				
				String old_file = null;
				String id = "";
				for(FileItem item : itemList) {
					switch(item.getFieldName()) {
					case "id" :
						mvo.setId(item.getString("utf-8"));
						id = item.getString("utf-8");
						break;
					case "pwd" :
						mvo.setPwd(item.getString("utf-8"));
						break;
					case "email" :
						mvo.setEmail(item.getString("utf-8"));
						break;
					case "phone" :
						mvo.setPhone(item.getString("utf-8"));
						break;
					case "newFile" :
						if(item.getSize()>0) {
							if(old_file != null) {
								FileRemoveHandler fh = new FileRemoveHandler();
								isOk = fh.deleteFile(savePath, old_file);
							}
							String fileName = item.getName();
							fileName = System.currentTimeMillis()+"_"+fileName;
							
							File uploadFilePath = new File(fileDir+File.separator+fileName);
							try {
								item.write(uploadFilePath);
								mvo.setImageFile(fileName);
								Thumbnails.of(uploadFilePath)
									.size(75, 75)
									.toFile(new File(fileDir +File.separator+"_th_"+fileName));
							}catch(Exception e) {
								log.info(">>>> file writer update error");
								e.printStackTrace();	
							}
						}else {
							mvo.setImageFile(old_file);
						}
						
					
					}
				}
				
				// DB 데이터 변경
				isOk = msv.update(mvo);
				log.info(">>>> update {}", (isOk>0? "성공" : "실패"));
				
				// 세션 데이터 변경 - 변경된 데이터 반영
				MemberVO mvo2 = msv.getDetail(id);
				HttpSession ses = request.getSession();
				ses.setAttribute("ses", mvo2);
				
				// 메인으로 이동
				destPage = "/index.jsp";
				
			} catch (Exception e) {
				log.info("member update case error!");
				e.printStackTrace();			
			}
			break;
			
		case "delete" :
			try {
				String id = request.getParameter("id");
				
				// 회원 탈퇴
				isOk = msv.delete(id);
				log.info(">>>> lastLogin delete > {}", (isOk>0? "성공":"실패"));
				
				// 세션 초기화
				HttpSession ses = request.getSession();
				ses.invalidate(); 
				
				// 메인으로 이동
				destPage = "/index.jsp";
				
			} catch (Exception e) {
				log.info("member delete case error!");
				e.printStackTrace();				
			}
			break;
			
		case "list" : 
			try {
				List<MemberVO> list = new ArrayList<>();
				list = msv.getList();
				log.info(">>>> user list >>>> {}", list);

				request.setAttribute("list", list);
				
				destPage = "/member/list.jsp";
				
			} catch (Exception e) {
				log.info("member list case error!");
				e.printStackTrace();				
			}
			break;
			
		case "duplTest" :
			try {
				String id = request.getParameter("id");
				MemberVO mvo = msv.testId(id);
				log.info(">>>> mvo 객체 >>>> {}", mvo);
				
				int result = (mvo != null)? -1 : 1;
				log.info(">>>> 결과 >>>> {}", result);
				PrintWriter out = response.getWriter();
				out.print(result);
				
				return;
				
			} catch (Exception e) {
				log.info("member duplTest case error!");
				e.printStackTrace();			
			}
		}
		
		// rdp 전송 
		rdp = request.getRequestDispatcher(destPage);
		rdp.forward(request, response);	
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}
}
