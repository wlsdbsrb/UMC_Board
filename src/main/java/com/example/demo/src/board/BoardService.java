package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.src.board.model.DeleteBoardReq;
import com.example.demo.src.board.model.PatchBoardReq;
import com.example.demo.src.board.model.PostBoardReq;
import com.example.demo.src.board.model.PostBoardRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BoardService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BoardDao boardDao;
    private final JwtService jwtService;

    @Autowired
    public BoardService(BoardDao boardDao, JwtService jwtService) {
        this.boardDao = boardDao;
        this.jwtService= jwtService;

    }

    @Transactional
    public PostBoardRes createBoard(PostBoardReq postBoardReq) throws BaseException{
        try {
            int boardIdx = boardDao.creatBoard(postBoardReq);
            return new PostBoardRes(boardIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional
    public void modifyContent(PatchBoardReq patchBoardReq) throws BaseException{
        try {
            int result = boardDao.modifyContent(patchBoardReq);
            System.out.println(result);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_CONTENT);
            }
        }
        catch (Exception exception){
            throw  new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional
    public void deleteBoard(DeleteBoardReq deleteBoardReq) throws BaseException{
        try {
            int result = boardDao.deleteBoard(deleteBoardReq);
            if(result==0){
                throw new BaseException(DELETE_FAIL_BOARD);
            }
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }




}
