package com.project.machinlearning.Comment;

import com.project.machinlearning.Comment.DTO.CommentRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
//@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/save") //댓글 작성
    public String saveComment(@RequestBody CommentRequestDTO commentRequestDTO) throws ParseException {
        return commentService.saveComment(commentRequestDTO);
    }



}
