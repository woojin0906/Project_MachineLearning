package com.project.machinlearning.Diary;

import com.project.machinlearning.User.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<DiaryEntity,Long> {

    Optional<DiaryEntity> findByUserAndWriteDate(UserEntity user, Date writeDate);

    Optional<DiaryEntity> findByNumId(Long numId);




}
