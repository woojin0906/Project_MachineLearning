package com.project.machinlearning.Diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<DiaryEntity,Long> {

    Optional<DiaryEntity> findByDiaryId(DiaryId diaryId);

}
