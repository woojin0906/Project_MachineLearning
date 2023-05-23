package com.project.machinlearning.Diary;

import com.project.machinlearning.Diary.DTO.DiaryRequestDTO;
import com.project.machinlearning.User.UserEntity;
import com.project.machinlearning.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository;

    public int saveDiary(DiaryRequestDTO diaryRequestDTO){
        System.out.println(diaryRequestDTO.getNickName());
        UserEntity user = userRepository.findByNickName(diaryRequestDTO.getNickName());
        if(user != null){
            Date currentDate = new Date();
            DiaryId diaryid = new DiaryId(user, currentDate );
            Optional<DiaryEntity> isdiary = diaryRepository.findByDiaryId(diaryid);
            if(isdiary.isPresent()){
                return -1;
            }else{
                //플라스크 서버 통신 + 감정 분석 + 사진 처리
                DiaryEntity diary = new DiaryEntity(diaryid, diaryRequestDTO.getContent(),"분노",0,"사진");
                System.out.println("되나");
                diaryRepository.save(diary);
                return 1;
            }
        }
            return -2;
    }

}
