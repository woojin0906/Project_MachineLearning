package com.project.machinlearning.Recommend;

import com.project.machinlearning.Diary.DiaryEntity;
import com.project.machinlearning.User.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<RecommendEntity, RecommendId> {

    Optional<RecommendEntity> findByDiaryNumIdAndUserUid(Long numId, Long uid);

}
