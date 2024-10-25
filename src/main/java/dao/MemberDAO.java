package dao;

import java.util.List;

import domain.MemberVO;

public interface MemberDAO {

	int register(MemberVO mvo);

	MemberVO login(MemberVO mvo);

	int lastLogin(String id);

	MemberVO getDetail(String id);

	int update(MemberVO mvo);

	int delete(String id);

	List<MemberVO> getList();

	MemberVO testId(String id);

}
