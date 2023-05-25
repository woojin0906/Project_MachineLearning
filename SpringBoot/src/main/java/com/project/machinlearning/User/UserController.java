package com.project.machinlearning.User;

import com.project.machinlearning.User.DTO.UserFormDto;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *    다이어리 등록, 수정, 조회 기능을 하는 컨트롤러 계층
 *
 *   @version          1.00 / 2023.05.23
 *   @author           한승완, 전우진
 */
@Controller
@Log4j2
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/new")
    public String userForm(Model model) {
        model.addAttribute("userFormDto", new UserFormDto());
        return "user/userForm";
    }

    @PostMapping("/new")
    public String memberForm(@Valid UserFormDto userFormDto,
                             BindingResult bindingResult, Model model) {
        // annotation에서 에러가 있으면 다시 userForm으로 돌아간다 -> 바인딩 에러 시 처리
        if(bindingResult.hasErrors()) {
            return "user/userForm";
        }
        try { // 회원가입을 처리하는 구문
            UserEntity userEntity = UserEntity.createUser(userFormDto, passwordEncoder);
            userService.saveUser(userEntity);
        } catch (IllegalStateException e) { //회원가입 처리 시 문제가 생기면 에러메세지 띄우기
            model.addAttribute("errorMessage", e.getMessage());
            // 문제가 있으면 회원가입으로 돌아감
            return "user/userForm";
        }

        return "user/userLogin";
    }

    @GetMapping("/login")
    public String login() {
        return "user/userLogin";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "닉네임 또는 패스워드가 잘못되었습니다.");

        return "user/userLogin"; //에러가 발생하면서 다시 로그인 폼으로 돌아간다
    }

}
