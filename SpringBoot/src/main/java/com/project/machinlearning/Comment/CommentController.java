package com.project.machinlearning.Comment;

import com.project.machinlearning.Comment.DTO.CommentRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/save") //댓글 작성
    public String saveComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        return commentService.saveComment(commentRequestDTO);
    }

    @DeleteMapping("/delete") //댓글 삭제
    public String deleteComment(@RequestParam(value = "cid") Long cid, @RequestParam(value = "pw") String pw){
        return commentService.deleteComment(cid,pw);
    }


}
