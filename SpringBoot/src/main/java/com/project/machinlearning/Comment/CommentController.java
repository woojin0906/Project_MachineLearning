package com.project.machinlearning.Comment;

import com.project.machinlearning.Comment.DTO.CommentRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;

/**
 *    댓글 작성, 삭제
 *
 *   @version          1.00 / 2023.05.23
 *   @author           한승완
 */
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

    @DeleteMapping("/delete/{cid}") //댓글 삭제
    public @ResponseBody ResponseEntity deleteComment(@PathVariable("cid") Long cid, Principal principal){
        if(!commentService.validateComment(cid, principal.getName())) {
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

       commentService.deleteComment(cid);
        return new ResponseEntity<Long>(cid, HttpStatus.OK);
    }


}
