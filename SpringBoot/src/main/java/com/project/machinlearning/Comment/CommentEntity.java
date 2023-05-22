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
    @JoinColumns({
            @JoinColumn(name = "uid"),
            @JoinColumn(name = "writeDate")
    })
    private DiaryEntity diary;

    @Column(name = "writeDate", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date writeDate;

    @Column(name="content", length = 100)
    private String content;

    @Column(name="emotion", length = 5)
    private String emotion;

}