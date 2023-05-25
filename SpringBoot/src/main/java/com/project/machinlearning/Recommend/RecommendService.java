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


    /**
     * DB 에서 좋아요 상황을 확인한 후 좋아요 개수 수정 후 DB 등록
     * - 한승완 2023.05.24
     */
    public Long saveRecommend(RecommendRequestDTO recommendRequestDTO) {
        boolean existingRecommend = recommendRepository.existsByDiaryNumIdAndUserUid(
                recommendRequestDTO.getNum_id(), recommendRequestDTO.getUid());

        if (existingRecommend) {
            Optional<DiaryEntity> diary = diaryRepository.findByNumId(recommendRequestDTO.getNum_id());
            if (diary.isPresent()) {
                DiaryEntity existingDiary = diary.get();
                existingDiary.setRecommend(existingDiary.getRecommend() - 1);
            }

            recommendRepository.deleteByDiaryNumIdAndUserUid(
                    recommendRequestDTO.getNum_id(), recommendRequestDTO.getUid());

            return 2L; // 이미 추천이 존재하는 경우
        }

        Optional<UserEntity> user = userRepository.findById(recommendRequestDTO.getUid());
        Optional<DiaryEntity> diary = diaryRepository.findByNumId(recommendRequestDTO.getNum_id());

        if (user.isEmpty() || diary.isEmpty()) {
            return -1L; // 사용자 또는 다이어리가 존재하지 않는 경우
        }

        DiaryEntity existingDiary = diary.get();
        existingDiary.setRecommend(existingDiary.getRecommend() + 1);

        RecommendEntity newRecommend = new RecommendEntity(user.get(), existingDiary);
        recommendRepository.save(newRecommend);

        return 1L; // 추천 정보 저장 성공
    }




}
