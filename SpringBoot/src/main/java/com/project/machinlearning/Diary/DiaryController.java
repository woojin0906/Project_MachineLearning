package com.project.machinlearning.Diary;


import com.project.machinlearning.Diary.DTO.DiaryRequestDTO;
import com.project.machinlearning.Diary.DTO.DiaryResponseDTO;
import com.project.machinlearning.Diary.DTO.DiarySearchDTO;
import com.project.machinlearning.Diary.DTO.DiarySpecificationResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @GetMapping("/test")
    public String test(){
        return "테스트";
    }

    @PostMapping("/save")  // 다이어리 저장
    public int saveDiary(@RequestBody DiaryRequestDTO diaryRequestDTo){
        return diaryService.saveDiary(diaryRequestDTo);
    }

    @PutMapping("/post")  // 다이어리 수정
    public int modifyDiary(@RequestParam(value = "numId") Long numId, @RequestBody DiaryRequestDTO diaryRequestDTo) {
        return diaryService.modifyDiary(numId, diaryRequestDTo);
    }

    @DeleteMapping("/post")  // 다이어리 삭제
    public String deleteDiary(@RequestParam(value = "numId") Long numId){
        return diaryService.deleteDiary(numId);
    }

    @GetMapping("/list/{page}")  // 다이어리 전체 조회
    public String listDiary(@PathVariable int page, Model model, Principal principal) { //, Authentication authentication
        String nickName = principal.getName();
        model.addAttribute("nickName", nickName);
        System.out.println(nickName);
//        String name = authentication.getName();
//        model.addAttribute("name", name);
        List<DiarySpecificationResponseDTO> lists = diaryService.getAllDiariesWithComments(page);
        model.addAttribute("lists", lists);
        return "main";
    }

    @GetMapping("/post/{numId}")  // 단일 다이어리 조회
    public String SpecificationDiary(@PathVariable Long numId, Model model) {
        DiarySpecificationResponseDTO diarySpecificationResponseDTO = diaryService.getDiaryWithCommentsAndRecommendations(numId);
        model.addAttribute("diarySpecificationResponseDTO", diarySpecificationResponseDTO);
        return "diary/diary";
    }

    @GetMapping("/list/emotion/{emotion}") // 감정으로 다이어리 조회
    public List<DiarySpecificationResponseDTO> listDiaryByEmotion(@PathVariable("emotion") String emotion){
        return diaryService.listDiaryByEmotion(emotion);
    }

    @GetMapping("/list/user/{nickName}") // 닉네임으로 다이어리 조회
    public List<DiarySpecificationResponseDTO> listDiaryByNickName(@PathVariable("nickName") String nickName){
        return diaryService.listDiaryByNickName(nickName);
    }

}
