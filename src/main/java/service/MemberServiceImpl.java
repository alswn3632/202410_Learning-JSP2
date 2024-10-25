package service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.MemberDAO;
import dao.MemberDAOImpl;
import domain.MemberVO;

public class MemberServiceImpl implements MemberService {
	private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
	private MemberDAO mdao;
	
	public MemberServiceImpl() {
		mdao = new MemberDAOImpl();
	}

	@Override
	public int register(MemberVO mvo) {
		return mdao.register(mvo);
	}

	@Override
	public MemberVO login(MemberVO mvo) {
		return mdao.login(mvo);
	}

	@Override
	public int lastLogin(String id) {
		return mdao.lastLogin(id);
	}

	@Override
	public MemberVO getDetail(String id) {
		return mdao.getDetail(id);
	}

	@Override
	public int update(MemberVO mvo) {
		return mdao.update(mvo);
	}

	@Override
	public int delete(String id) {
		return mdao.delete(id);
	}

	@Override
	public List<MemberVO> getList() {
		return mdao.getList();
	}

	@Override
	public MemberVO testId(String id) {
		return mdao.testId(id);
	}
}
