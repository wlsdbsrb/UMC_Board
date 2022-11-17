package com.example.demo.src.board;

import com.example.demo.src.board.model.DeleteBoardReq;
import com.example.demo.src.board.model.GetBoardRes;
import com.example.demo.src.board.model.PatchBoardReq;
import com.example.demo.src.board.model.PostBoardReq;
import com.example.demo.src.user.model.PatchUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BoardDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource); }

    public List<GetBoardRes> getBoards(){
        String getBoardsQuery = "select * from Board";
        return this.jdbcTemplate.query(getBoardsQuery,
                (rs,rowNum) -> new GetBoardRes(
                        rs.getInt("boardIdx"),
                        rs.getString("nickname"),
                        rs.getString("title"),
                        rs.getString("content"))
        );
    }
    public List<GetBoardRes> getBoardsByTitle(String title){
        String getBoardsByTitleQuery = "select * from Board where title LIKE ?";
        String getBoardsByTitleParams = title;
        return this.jdbcTemplate.query(getBoardsByTitleQuery,
                (rs, rowNum) -> new GetBoardRes(
                        rs.getInt("boardIdx"),
                        rs.getString("nickname"),
                        rs.getString("title"),
                        rs.getString("content")),
                '%'+getBoardsByTitleParams+'%');
    }
    public GetBoardRes getBoard(int boardIdx){
        String getBoardQuery = "select * from Board where boardIdx = ?";
        int getBoardParams = boardIdx;
        return this.jdbcTemplate.queryForObject(getBoardQuery,
                (rs, rowNum) -> new GetBoardRes(
                        rs.getInt("boardIdx"),
                        rs.getString("nickname"),
                        rs.getString("title"),
                        rs.getString("content")),
                getBoardParams);
    }

    public int checkBoard(int boardIdx){
        String checkBoardQuery = "select exists(select boardIdx from Board where boardIdx = ?)";
        System.out.println("z");
        return this.jdbcTemplate.queryForObject(checkBoardQuery,
                int.class,
                boardIdx);
    }

    public int creatBoard(PostBoardReq postBoardReq){
        String createBoardQuery = "insert into Board (nickname, title, content) VALUES(?,?,?)";
        Object[] creatBoardParams = new Object[]{
                postBoardReq.getNickname(), postBoardReq.getTitle(), postBoardReq.getContent()
        };
        this.jdbcTemplate.update(createBoardQuery,creatBoardParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
    public int modifyContent(PatchBoardReq patchBoardReq){
        String modifyContentQuery = "update Board set content= ? where boardIdx = ? ";
        Object[] modifyContentParams = new Object[]{patchBoardReq.getContent(), patchBoardReq.getBoardIdx()};

        return this.jdbcTemplate.update(modifyContentQuery,modifyContentParams);
    }
    public int deleteBoard(DeleteBoardReq deleteBoardReq){
        String deleteBoardQuery = "delete from Board where boardIdx = ? ";
        int boardIdx = deleteBoardReq.getBoardIdx();

        return this.jdbcTemplate.update(deleteBoardQuery,boardIdx);
    }

}
