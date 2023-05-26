package com.project.machinlearning.Comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.machinlearning.Diary.DiaryEntity;
import com.project.machinlearning.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@ToString(exclude = "diary")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numId")
    private DiaryEntity diary;

    @Temporal(TemporalType.DATE)
    private Date writeDate;

    @Column(name="content", length = 100)
    private String content;

    @Column(name="emotion", length = 5)
    private String emotion;

    public CommentEntity(DiaryEntity diary,Date writeDate ,String content, String emotion, UserEntity user) {
        this.diary = diary;
        this.writeDate = writeDate;
        this.content = content;
        this.emotion = emotion;
        this.user = user;
    }
}
