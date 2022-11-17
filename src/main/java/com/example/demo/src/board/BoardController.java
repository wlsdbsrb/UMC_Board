package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.board.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController

@RequestMapping("/app/board")
public class BoardController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BoardProvider boardProvider;
    @Autowired
    private final BoardService boardService;
    @Autowired
    private final JwtService jwtService;


    public BoardController(BoardProvider boardProvider, BoardService boardService, JwtService jwtService) {
        this.boardProvider = boardProvider;
        this.boardService = boardService;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetBoardRes>> getBoards(@RequestParam(required = false) String title){

        try{
            if(title == null){
                List<GetBoardRes> getBoardRes = boardProvider.getBoards();
                return new BaseResponse<>(getBoardRes);
            }
            List<GetBoardRes> getBoardRes = boardProvider.getBoardsByTitle(title);
            if(getBoardRes.isEmpty()){
                return new BaseResponse<>(RESULT_NULL_ERROR);
            }
            return new BaseResponse<>(getBoardRes);
        }
        catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @GetMapping("/{boardIdx}") //
    public BaseResponse<GetBoardRes> getBoard(@PathVariable("boardIdx") int boardIdx) {
        // Get Users
        try{

            if(boardProvider.checkBoard(boardIdx)!=1){
                return new BaseResponse<>(NOT_EXIST_BOARD);
            }

            GetBoardRes getBoardRes = boardProvider.getBoard(boardIdx);

            return new BaseResponse<>(getBoardRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostBoardRes> createBoard(@RequestBody PostBoardReq postBoardReq){
        if(postBoardReq.getNickname() == null){
            return new BaseResponse<>(POST_BOARD_EMPTY_NICKNAME);
        }
        if(postBoardReq.getContent() == null){
            return new BaseResponse<>(POST_BOARD_EMPTY_CONTENT);
        }
        if(postBoardReq.getTitle() == null){
            return new BaseResponse<>(POST_BOARD_EMPTY_TITLE);
        }

        try{
            PostBoardRes postBoardRes = boardService.createBoard(postBoardReq);
            return new BaseResponse<>(postBoardRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PatchMapping("/content/{boardIdx}")
    public BaseResponse<String> modifyContent(@PathVariable int boardIdx, @RequestBody GetBoardRes getBoardRes){
        try {

            int boardIdxByJwt = jwtService.getBoardIdx();

            if(boardIdx != boardIdxByJwt){
                return new BaseResponse<>(INVALID_BOARD_JWT);
            }

            PatchBoardReq patchBoardReq= new PatchBoardReq(boardIdx,getBoardRes.getContent());
            boardService.modifyContent(patchBoardReq);

            if(patchBoardReq.getContent().length() == 0){
                return new BaseResponse<>(POST_BOARD_EMPTY_CONTENT);
            }

            String result="수정 성공";
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }
    @ResponseBody
    @DeleteMapping("/{boardIdx}")
    public BaseResponse<String> deleteBoard(@PathVariable int boardIdx){
        try {

            int boardIdxByJwt = jwtService.getBoardIdx();

            if(boardIdx != boardIdxByJwt){
                return new BaseResponse<>(INVALID_BOARD_JWT);
            }

            DeleteBoardReq deleteBoardReq = new DeleteBoardReq(boardIdx);
            boardService.deleteBoard(deleteBoardReq);

            String result="삭제 성공";
            return new BaseResponse<>(result);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
