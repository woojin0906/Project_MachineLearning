package com.project.machinlearning.Comment;

import com.project.machinlearning.Diary.DiaryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numId")
    private DiaryEntity diary;

    @Temporal(TemporalType.DATE)
    private Date writeDate;

    @Column(name="content", length = 100)
    private String content;

    @Column(name="emotion", length = 5)
    private String emotion;

    @Column(name="pw", length = 20)
    private String pw;

    public CommentEntity(DiaryEntity diary,Date writeDate ,String content, String emotion, String pw) {
        this.diary = diary;
        this.writeDate = writeDate;
        this.content = content;
        this.emotion = emotion;
        this.pw = pw;
    }
}
