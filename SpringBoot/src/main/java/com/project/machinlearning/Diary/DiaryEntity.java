package com.project.machinlearning.Diary;


import com.project.machinlearning.Comment.CommentEntity;
import com.project.machinlearning.Recommend.RecommendEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "diary")
public class DiaryEntity {

    @EmbeddedId
    private DiaryId diaryId;

    @Column(name="content", length = 254)
    private String content;

    @Column(name="emotion", length = 5)
    private String emotion;

    @Column(name = "views", columnDefinition = "int default 0")
    private int view;

    @Column(name="photo")
    private String photo;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendEntity> recommends;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

}

