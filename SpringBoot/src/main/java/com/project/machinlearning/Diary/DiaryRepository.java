package com.project.machinlearning.Diary;

import com.project.machinlearning.User.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<DiaryEntity,Long> {

    Optional<DiaryEntity> findByUser(UserEntity user);

}
