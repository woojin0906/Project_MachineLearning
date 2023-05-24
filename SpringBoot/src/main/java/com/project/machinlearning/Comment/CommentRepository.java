package com.project.machinlearning.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    Optional<CommentEntity> findByCid(Long cid);
}
