package com.project.machinlearning.Diary;


import com.project.machinlearning.Diary.DTO.DiaryRequestDTO;
import com.project.machinlearning.Diary.DTO.DiarySearchDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller
@RestController
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

    @GetMapping("/list")  // 리스트 전체 조회
    public List<DiaryEntity> listDiary() {
        return diaryService.listDiary();
    }

    @GetMapping("/post")  // 리스트 단일 조회
    public DiaryEntity postDiary(@RequestParam(value = "numId") Long numId) {
        return diaryService.postDiary(numId);
    }



}
