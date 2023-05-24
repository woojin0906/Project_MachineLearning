package com.project.machinlearning.Diary;


import com.project.machinlearning.Diary.DTO.DiaryRequestDTO;
import org.springframework.web.bind.annotation.*;

//@Controller
@RestController
@RequestMapping("/diary")
public class DiaryController {


    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @GetMapping("/test")
    public String test(){
        return "테스트";
    }

    @PostMapping("/save")
    public int saveDiary(@RequestBody DiaryRequestDTO diaryRequestDTo){
        return diaryService.saveDiary(diaryRequestDTo);
    }

//    @PutMapping("/modify/{diaryNum}")
//    public int modifyDiary(@RequestBody DiaryRequestDTO diaryRequestDTo) {
//        return diaryService.modifyDiary(diaryRequestDTo);
//    }
}
