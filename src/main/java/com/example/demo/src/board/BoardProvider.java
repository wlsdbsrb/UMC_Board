package com.example.demo.src.board;


import com.example.demo.config.BaseException;
import com.example.demo.src.board.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.RESULT_NULL_ERROR;

@Service
public class BoardProvider {

    private final BoardDao boardDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BoardProvider(BoardDao boardDao) {
        this.boardDao = boardDao;
    }
    public List<GetBoardRes> getBoards() throws BaseException {
        try {
            List<GetBoardRes> getBoardRes = boardDao.getBoards();
            return getBoardRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetBoardRes> getBoardsByTitle(String title) throws BaseException{
        try{
            List<GetBoardRes> getBoardRes = boardDao.getBoardsByTitle(title);
            return getBoardRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public GetBoardRes getBoard(int boardIdx) throws BaseException {
        try {
            GetBoardRes getBoardRes = boardDao.getBoard(boardIdx);
            return getBoardRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkBoard(int boardIdx) throws BaseException{
        try{
            return boardDao.checkBoard(boardIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }




}
