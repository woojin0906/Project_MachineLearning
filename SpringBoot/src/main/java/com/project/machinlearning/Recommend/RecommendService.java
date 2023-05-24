package com.project.machinlearning.Recommend;

import com.project.machinlearning.Diary.DiaryEntity;
import com.project.machinlearning.Diary.DiaryRepository;
import com.project.machinlearning.Recommend.DTO.RecommendRequestDTO;
import com.project.machinlearning.User.UserEntity;
import com.project.machinlearning.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {

    private final RecommendRepository recommendRepository;

    private final UserRepository userRepository;

    private final DiaryRepository diaryRepository;


    public Long saveRecommend(RecommendRequestDTO recommendRequestDTO) {
        Optional<RecommendEntity> existingRecommend = recommendRepository.findByDiaryNumIdAndUserUid(
                recommendRequestDTO.getNum_id(), recommendRequestDTO.getUid());
        if (existingRecommend.isPresent()) {
            // 이미 추천이 존재하는 경우, 데이터베이스에서 해당 추천 삭제
            recommendRepository.delete(existingRecommend.get());
            return 2L;
        }

        Optional<UserEntity> user = userRepository.findById(recommendRequestDTO.getUid());
        Optional<DiaryEntity> diary = diaryRepository.findByNumId(recommendRequestDTO.getNum_id());

        if (user.isEmpty() || diary.isEmpty()) {
            // 사용자 또는 다이어리가 존재하지 않는 경우
            return -1L;
        }

        RecommendEntity newRecommend = new RecommendEntity(user.get(), diary.get());
        recommendRepository.save(newRecommend);
        return 1L;
    }



}
