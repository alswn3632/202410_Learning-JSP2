package service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDAO;
import dao.BoardDAOImpl;
import domain.BoardVO;

public class BoardServiceImpl implements BoardService {
	private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class); // 얘는 클래스마다 다 있어야함. 이름은 내 클래스로 바꿔서
	// dao 연결
	private BoardDAO bdao;
	
	public BoardServiceImpl() {
		bdao = new BoardDAOImpl();
	}

	@Override
	public int register(BoardVO bvo) {
		log.info(">>>> register service in");
		return bdao.insert(bvo);
	}

	@Override
	public List<BoardVO> getList() {
		return bdao.getList();
	}

	@Override
	public BoardVO getDetail(int bno) {
		return bdao.getDetail(bno);
	}

	@Override
	public int update(BoardVO bvo) {
		return bdao.update(bvo);
	}

	@Override
	public int delete(int bno) {
		return bdao.delete(bno);
	}
}
