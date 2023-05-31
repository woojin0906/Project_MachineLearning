package com.project.machinlearning.controller;



import com.project.machinlearning.Comment.DirtyComment.DirtyCommentEntity;
import com.project.machinlearning.Comment.DirtyComment.DirtyCommentRepository;
import com.project.machinlearning.User.Role;
import com.project.machinlearning.User.UserEntity;
import com.project.machinlearning.User.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class adminController {

    private final UserRepository userRepository;

    private final DirtyCommentRepository dirtyCommentRepository;

    public adminController(UserRepository userRepository, DirtyCommentRepository dirtyCommentRepository) {
        this.userRepository = userRepository;
        this.dirtyCommentRepository = dirtyCommentRepository;

    }

    @GetMapping("/list")
    public String admin(Model model, Principal principal){

        String nickName = principal.getName();
        model.addAttribute("nickName", nickName);

        List<UserEntity> lists = userRepository.findAllByOrderByCountDesc();
        model.addAttribute("lists", lists);

        return "user/admin";
    }

    @GetMapping("/list/{nickname}")
    public String checkDirtyComment(@PathVariable("nickname") String nickname, Model model, Principal principal){

        String nickName = principal.getName();
        model.addAttribute("nickName", nickName);

        List<DirtyCommentEntity> lists = dirtyCommentRepository.findByNickname(nickname);
        model.addAttribute("lists", lists);

        return "user/dirtyComment";
    }

    @PostMapping("/list/ban/{nickname}")
    public void banUser(@PathVariable("nickname") String nickname, Principal principal){
       UserEntity user =  userRepository.findByNickName(nickname);
       user.setRole(Role.BAN);
    }


}
