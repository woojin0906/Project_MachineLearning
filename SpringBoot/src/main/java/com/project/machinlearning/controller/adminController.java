package com.project.machinlearning.controller;



import com.project.machinlearning.Comment.DirtyComment.DirtyCommentEntity;
import com.project.machinlearning.Comment.DirtyComment.DirtyCommentRepository;
import com.project.machinlearning.User.DTO.BanRequestDTO;
import com.project.machinlearning.User.UserEntity;
import com.project.machinlearning.User.UserRepository;
import com.project.machinlearning.User.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class adminController {

    private final UserRepository userRepository;

    private final DirtyCommentRepository dirtyCommentRepository;
    private final UserService userService;

    public adminController(UserRepository userRepository, DirtyCommentRepository dirtyCommentRepository, UserService userService) {
        this.userRepository = userRepository;
        this.dirtyCommentRepository = dirtyCommentRepository;
        this.userService = userService;
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

    @PostMapping(value = "/list/ban/{nickname}")
    public String banUser(@Valid BanRequestDTO banRequestDTO) throws IOException {
        userService.updateUser(banRequestDTO.getNickname());

        return "redirect:/admin/list";
    }
}
