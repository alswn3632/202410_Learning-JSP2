package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.CommentVO;
import orm.DatabaseBuilder;

public class CommentDAOImpl implements CommentDAO {
	private static final Logger log = LoggerFactory.getLogger(CommentDAOImpl.class);

	// DB의 연결을 위한 객체
	private SqlSession sql;
	
	public CommentDAOImpl() {
		new DatabaseBuilder();
		sql = DatabaseBuilder.getFactory().openSession();
	}

	@Override
	public int post(CommentVO cvo) {
		log.info(">>>> comment dao post in!!");
		int isOk = sql.insert("CommentMapper.post", cvo);
		if(isOk > 0) sql.commit();
		return isOk;
	}

	@Override
	public List<CommentVO> getList(int bno) {
		log.info(">>>> comment dao getList in!!");
		return sql.selectList("CommentMapper.list", bno);
		
	}

	@Override
	public int modify(CommentVO cvo) {
		log.info(">>>> comment dao modify in!!");
		int isOk = sql.update("CommentMapper.modify", cvo);
		if(isOk > 0) sql.commit();
		return isOk;
	}

	@Override
	public int delete(int cno) {
		log.info(">>>> comment dao delete in!!");
		int isOk = sql.delete("CommentMapper.delete", cno);
		if(isOk > 0) sql.commit();
		return isOk;	
	}
}
