package com.project.machinlearning.Diary;

import com.project.machinlearning.Comment.CommentEntity;
import com.project.machinlearning.Diary.DTO.DiaryRequestDTO;
import com.project.machinlearning.Diary.DTO.DiarySearchDTO;
import com.project.machinlearning.Diary.DTO.DiarySpecificationResponseDTO;
import com.project.machinlearning.User.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<DiaryEntity,Long> {

    Optional<DiaryEntity> findByUserAndWriteDate(UserEntity user, Date writeDate);

    Optional<DiaryEntity> findByNumId(Long numId);

    List<DiaryEntity> findByEmotionOrderByNumIdDesc(String emotion);

    List<DiaryEntity> findByUserNickNameOrderByNumIdDesc(String nickName);

    @Query("SELECT d " +
            "FROM DiaryEntity d " +
            "WHERE d.numId = :numId")
    Optional<DiaryEntity> getDiary(@Param("numId") Long numId);


}
