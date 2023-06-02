package com.project.machinlearning.Comment.DirtyComment;

import com.project.machinlearning.User.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dirty_comment")
public class DirtyCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long did;

    @Column(name = "content")
    private String content;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "state")
    private String state = "반영";

    public DirtyCommentEntity(String content, String nickname) {
        this.content = content;
        this.nickname = nickname;
    }
}
