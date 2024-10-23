package domain;

public class CommentVO {
	//멤버변수
	private int cno;
	private int bno;
	private String content;
	private String writer;
	private String regdate;
	
	//생성자
	public CommentVO() {
		
	}
	
	// 댓글 수정용
	public CommentVO(int cno, String content) {
		this.cno = cno;
		this.content = content;
	}
	
	// 댓글 등록용 
	public CommentVO(int bno, String content, String writer) {
		this.bno = bno;
		this.content = content;
		this.writer = writer;
	}
	
	// 댓글 리스트(전체)
	public CommentVO(int cno, int bno, String content, String writer, String regdate) {
		this.cno = cno;
		this.bno = bno;
		this.content = content;
		this.writer = writer;
		this.regdate = regdate;
	}
	
	//자동완성
	@Override
	public String toString() {
		return "CommentVO [cno=" + cno + ", bno=" + bno + ", content=" + content + ", writer=" + writer + ", regdate="
				+ regdate + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	public int getCno() {
		return cno;
	}

	public void setCno(int cno) {
		this.cno = cno;
	}

	public int getBno() {
		return bno;
	}

	public void setBno(int bno) {
		this.bno = bno;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	
	
	
}
