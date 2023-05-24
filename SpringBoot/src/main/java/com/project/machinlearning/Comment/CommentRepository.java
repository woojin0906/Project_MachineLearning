package com.project.machinlearning.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    Optional<CommentEntity> findByCid(Long cid);

    @Query("SELECT c " +
            "FROM CommentEntity c " +
            "WHERE c.diary.numId = :numId")
    List<CommentEntity> getCommentsByDiaryId(@Param("numId") Long numId);
}
