package com.project.machinlearning.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor // @Autowired로도 쓸 수 있음
public class MainController {


    @GetMapping("/")
    public String main() {
        return "main";
    }

}
