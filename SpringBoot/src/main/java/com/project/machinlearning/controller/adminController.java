package com.project.machinlearning.controller;



import com.project.machinlearning.Comment.DirtyComment.DeleteDirtyCommentDTO;
import com.project.machinlearning.Comment.DirtyComment.DirtyCommentEntity;
import com.project.machinlearning.Comment.DirtyComment.DirtyCommentRepository;
import com.project.machinlearning.User.DTO.BanRequestDTO;
import com.project.machinlearning.User.UserEntity;
import com.project.machinlearning.User.UserRepository;
import com.project.machinlearning.User.UserService;
import jakarta.validation.Valid;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
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


    @PostMapping(value = "/list/delete/{nickname}/{did}")
    public String deleteComment(@PathVariable("nickname") String nickname,
                              @PathVariable("did") Long did) throws NotFoundException {
        DirtyCommentEntity dirtyComment = dirtyCommentRepository.findById(did)
                .orElseThrow(() -> new NotFoundException("삭제할 댓글을 찾을 수 없습니다")); // 댓글을 찾을 수 없는 경우에 대한 예외 처리
        UserEntity user = userRepository.findByNickName(nickname);
            if(dirtyComment.getState().equals("반영")) {
                dirtyComment.setState("제외");
                user.setCount(user.getCount() - 1);
            }else{
                dirtyComment.setState("반영");
                user.setCount(user.getCount() + 1);

            }
            dirtyCommentRepository.save(dirtyComment);
            userRepository.save(user);

        return "redirect:/admin/list/"+nickname;
    }


}
