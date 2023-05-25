package com.project.machinlearning.Diary;


import com.project.machinlearning.Diary.DTO.DiaryRequestDTO;
import com.project.machinlearning.Diary.DTO.DiaryResponseDTO;
import com.project.machinlearning.Diary.DTO.DiarySearchDTO;
import com.project.machinlearning.Diary.DTO.DiarySpecificationResponseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/save")  // 저장
    public int saveDiary(@RequestBody DiaryRequestDTO diaryRequestDTo){
        return diaryService.saveDiary(diaryRequestDTo);
    }

    @PutMapping("/post")  // 수정
    public int modifyDiary(@RequestParam(value = "numId") Long numId, @RequestBody DiaryRequestDTO diaryRequestDTo) {
        return diaryService.modifyDiary(numId, diaryRequestDTo);
    }

    @DeleteMapping("/post")  // 삭제
    public String deleteDiary(@RequestParam(value = "numId") Long numId){
        return diaryService.deleteDiary(numId);
    }

    @GetMapping("/list/{page}")  // 리스트 전체 조회
    public String listDiary(@PathVariable int page, Model model) {
        List<DiarySpecificationResponseDTO> lists = diaryService.getAllDiariesWithComments(page);
        model.addAttribute("lists", lists);
        return "main";
    }
    @GetMapping("/post/{numId}")  // 리스트 단일 조회
    public String SpecificationDiary(@PathVariable Long numId, Model model) {
        DiarySpecificationResponseDTO diarySpecificationResponseDTO = diaryService.getDiaryWithCommentsAndRecommendations(numId);
        model.addAttribute("diarySpecificationResponseDTO", diarySpecificationResponseDTO);
        return "diary/diary";
    }

    @GetMapping("/list/emotion/{emotion}") //감정으로 조회
    public List<DiarySpecificationResponseDTO> listDiaryByEmotion(@PathVariable("emotion") String emotion){
        return diaryService.listDiaryByEmotion(emotion);
    }

    @GetMapping("/list/user/{nickName}")
    public List<DiarySpecificationResponseDTO> listDiaryByNickName(@PathVariable("nickName") String nickName){
        return diaryService.listDiaryByNickName(nickName);
    }

}
