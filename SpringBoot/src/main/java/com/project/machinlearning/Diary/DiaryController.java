package com.project.machinlearning.Diary;


import com.project.machinlearning.Diary.DTO.DiaryRequestDTO;
import com.project.machinlearning.Diary.DTO.DiarySpecificationResponseDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

import com.project.machinlearning.User.UserEntity;
import com.project.machinlearning.User.UserRepository;

import jakarta.validation.Valid;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *    다이어리 등록, 수정, 조회, 검색
 *
 *   @version          1.00 / 2023.05.23
 *   @author           한승완, 전우진
 */
@Controller
//@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;
    private final UserRepository userRepository;

    public DiaryController(DiaryService diaryService, UserRepository userRepository) {
        this.diaryService = diaryService;
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public String test(){
        return "테스트";
    }

    @GetMapping("/save")
    public String save(Principal principal, Model model) {
        String nickName = principal.getName();
        model.addAttribute("nickName", nickName);

        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 E요일");
        String strNowDate = simpleDateFormat.format(today);


        model.addAttribute("strNowDate", strNowDate);
        model.addAttribute("diarySpecificationResponseDTO", new DiarySpecificationResponseDTO());
        return "diary/saveDiary";
    }

    @PostMapping(value="/save_proc")  // 다이어리 저장
    public String saveDiary(@Valid DiaryRequestDTO diaryRequestDTo, @RequestParam("itemImgFile") MultipartFile itemImgFileList, Model model, Principal principal, HttpServletResponse response, BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()) {
            return "diary/saveDiary";
        }
        try {
            String nickName = principal.getName();
            model.addAttribute("nickName", nickName);
            diaryRequestDTo.setNickName(nickName);
            diaryService.saveDiary(diaryRequestDTo, itemImgFileList);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "diary/saveDiary";
        }
        // 응답으로 성공 상태 코드 전송
        response.setStatus(HttpServletResponse.SC_OK);
        return "redirect:/api/diary/list/1";
    }

    @GetMapping("/post/{numId}")  // 다이어리 수정
    public String modifyDiary(@PathVariable Long numId, Model model, Principal principal) {

        String nickName = principal.getName();
        model.addAttribute("nickName", nickName);

        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 E요일");
        String strNowDate = simpleDateFormat.format(today);

        model.addAttribute("strNowDate", strNowDate);

        DiarySpecificationResponseDTO diarySpecificationResponseDTO = diaryService.getDiaryWithCommentsAndRecommendations(numId);
        model.addAttribute("diarySpecificationResponseDTO", diarySpecificationResponseDTO);
        return "diary/saveDiary";
    }

    @PutMapping("/post/{numId}")  // 다이어리 수정
    public int modifyDiary(@PathVariable Long numId, @RequestBody DiaryRequestDTO diaryRequestDTo) {
        return diaryService.modifyDiary(numId, diaryRequestDTo);
    }

    @DeleteMapping("/delete/{numId}")  // 다이어리 삭제
    public @ResponseBody ResponseEntity deleteDiary(@PathVariable Long numId){
        diaryService.deleteDiary(numId);
        return new ResponseEntity<Long>(numId, HttpStatus.OK);
    }

    @GetMapping({"/list","/list/{page}"})  // 다이어리 전체 조회
    public String listDiary(Model model, Principal principal, @PathVariable Optional<Integer> page) {
        int pageNumber = page.orElse(1);
        String nickName = principal.getName();

        UserEntity user = userRepository.findByNickName(nickName);
        List<DiarySpecificationResponseDTO> lists = diaryService.getAllDiariesWithComments(pageNumber,user);

        model.addAttribute("nickName", nickName);
        model.addAttribute("uid", user.getUid());
        model.addAttribute("lists", lists);

        return "main";
    }

    @GetMapping("/posts/{numId}")  // 단일 다이어리 조회
    public String SpecificationDiary(@PathVariable Long numId, Model model, Principal principal) {
        String nickName = principal.getName();

        UserEntity user = userRepository.findByNickName(nickName);
        model.addAttribute("nickName", nickName);
        model.addAttribute("uId", user.getUid());

        DiarySpecificationResponseDTO diarySpecificationResponseDTO = diaryService.getDiaryWithCommentsAndRecommendations(numId);
        model.addAttribute("diarySpecificationResponseDTO", diarySpecificationResponseDTO);
        return "diary/diary";
    }

    @GetMapping("/list/emotion/{emotion}") // 감정으로 다이어리 조회
    public String listDiaryByEmotion(@PathVariable("emotion") String emotion, Model model, Principal principal, @PathVariable Optional<Integer> page){
        int pageNumber = page.orElse(1);
        String nickName = principal.getName();

        UserEntity user = userRepository.findByNickName(nickName);
        List<DiarySpecificationResponseDTO> lists = diaryService.listDiaryByEmotion(emotion, pageNumber, user);

        model.addAttribute("nickName", nickName);
        model.addAttribute("uid", user.getUid());
        model.addAttribute("lists", lists);

        return "main";
    }

    @GetMapping("/list/user") // 닉네임으로 다이어리 조회
    public String listDiaryByNickName(Model model, Principal principal){
        String nickName = principal.getName();

        UserEntity user = userRepository.findByNickName(nickName);
        List<DiarySpecificationResponseDTO> lists = diaryService.listDiaryByNickName(nickName);

        model.addAttribute("nickName", nickName);
        model.addAttribute("lists", lists);
        model.addAttribute("uid", user.getUid());

        return "diary/mypage";

    }

}
