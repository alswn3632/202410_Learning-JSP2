package domain;

public class MemberVO {
	//멤버변수
	private String id;
	private String pwd;
	private String email;
	private String phone;
	private String regdate;
	private String lastlogin;
	private String imageFile;
	
	//생성자
	public MemberVO() {
		
	}
	
	// 회원가입은 두 가지 입장에서 생각하여 만들어야함
	// 관리자 입장 / 사용자 입장
	// 회원 등급 : admin(사이트관리자), manager(각 파트의 최고관리자), user(사용자) 
	
	// 회원 가입 : id, pwd, email, phone
	// 회원정보 수정 : 회원가입과 동일 
	public MemberVO(String id, String pwd, String email, String phone, String imageFile) {
		this.id = id;
		this.pwd = pwd;
		this.email = email;
		this.phone = phone;
		this.imageFile = imageFile;
	}
	
	// 로그인 : id, pwd 
	public MemberVO(String id, String pwd) {
		this.id = id;
		this.pwd = pwd;
	}
	
	// 회원 리스트 : 전체 필드, 관리자만 접근 가능하도록 (우선은 id가 admin이면 으로)
	public MemberVO(String id, String pwd, String email, String phone, String regdate, String lastlogin, String imageFile) {
		this.id = id;
		this.pwd = pwd;
		this.email = email;
		this.phone = phone;
		this.regdate = regdate;
		this.lastlogin = lastlogin;
		this.imageFile = imageFile;
	}
	
	//자동완성
	@Override
	public String toString() {
		return "MemberVO [id=" + id + ", pwd=" + pwd + ", email=" + email + ", phone=" + phone + ", regdate=" + regdate
				+ ", lastlogin=" + lastlogin + ", imageFile=" + imageFile + "]";
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPwd() {
		return pwd;
	}
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getRegdate() {
		return regdate;
	}
	
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	
	public String getLastlogin() {
		return lastlogin;
	}
	
	public void setLastlogin(String lastlogin) {
		this.lastlogin = lastlogin;
	}

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	
	
}
