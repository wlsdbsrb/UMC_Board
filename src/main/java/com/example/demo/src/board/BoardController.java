package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.board.model.*;
import com.example.demo.src.user.model.PostLoginReq;
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
    public BaseResponse<List<GetBoardRes>> getBoards(@RequestParam(value = "paging", defaultValue = "1")int paging){

        try{
            List<GetBoardRes> getBoardRes = boardProvider.getBoards(paging);

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
    @GetMapping("title")
    public BaseResponse<List<GetBoardRes>> getBoardsByTitle(@RequestParam String title, @RequestParam(value = "paging",defaultValue = "1")int paging){

        try{

            List<GetBoardRes> getBoardRes = boardProvider.getBoardsByTitle(title,paging);
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
    @PatchMapping("/content/{userIdx}")
    public BaseResponse<String> modifyContent(@RequestBody PatchBoardReq patchBoardReq,@PathVariable int userIdx){
        try {

            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

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
    @PatchMapping("/drop/{userIdx}/{boardIdx}")
    public BaseResponse<String> deleteBoard(@PathVariable int boardIdx ,@PathVariable int userIdx){
        try {

            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
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
