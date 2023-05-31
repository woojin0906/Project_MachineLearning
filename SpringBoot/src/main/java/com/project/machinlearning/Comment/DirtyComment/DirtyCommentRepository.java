package com.project.machinlearning.Comment.DirtyComment;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirtyCommentRepository extends JpaRepository<DirtyCommentEntity, Long> {
    List<DirtyCommentEntity> findByNickname(String nickname);

}
