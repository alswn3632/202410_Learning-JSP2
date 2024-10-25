package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.MemberVO;
import orm.DatabaseBuilder;

public class MemberDAOImpl implements MemberDAO {
	private static final Logger log = LoggerFactory.getLogger(MemberDAOImpl.class);

	// DB의 연결을 위한 객체
	private SqlSession sql;
	
	public MemberDAOImpl() {
		new DatabaseBuilder();
		sql = DatabaseBuilder.getFactory().openSession();
	}

	@Override
	public int register(MemberVO mvo) {
		log.info(">>>> dao register in!");
		int isOk = sql.insert("MemberMapper.register", mvo);
		if(isOk>0) sql.commit();
		return isOk;
	}

	@Override
	public MemberVO login(MemberVO mvo) {
		log.info(">>>> dao login in!");
		return sql.selectOne("MemberMapper.login", mvo);
	}

	@Override
	public int lastLogin(String id) {
		log.info(">>>> dao lastLogin in!");
		int isOk = sql.update("MemberMapper.lastLogin", id);
		if(isOk>0) sql.commit();
		return isOk;
	}

	@Override
	public MemberVO getDetail(String id) {
		log.info(">>>> dao modify in!");
		return sql.selectOne("MemberMapper.modify", id);
	}

	@Override
	public int update(MemberVO mvo) {
		log.info(">>>> dao update in!");
		int isOk = sql.update("MemberMapper.update", mvo);
		if(isOk>0) sql.commit();
		return isOk;
	}

	@Override
	public int delete(String id) {
		log.info(">>>> dao delete in!");
		int isOk = sql.delete("MemberMapper.del", id);
		if(isOk>0) sql.commit();
		return isOk;
	}

	@Override
	public List<MemberVO> getList() {
		return sql.selectList("MemberMapper.getList");
	}

	@Override
	public MemberVO testId(String id) {
		return sql.selectOne("MemberMapper.testId", id);
	}
	
	
}
