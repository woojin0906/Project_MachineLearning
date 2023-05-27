package com.project.machinlearning.Diary;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.machinlearning.Comment.CommentEntity;
import com.project.machinlearning.Diary.DTO.DiaryRequestDTO;
import com.project.machinlearning.Recommend.RecommendEntity;
import com.project.machinlearning.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@ToString(exclude = {"user", "recommends", "comments"})
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "diary")
public class DiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private UserEntity user;

    @Temporal(TemporalType.DATE)
    private Date writeDate;

    @Column(name="content", length = 254)
    private String content;

    @Column(name="emotion", length = 5)
    private String emotion;

    @Column(name = "views", columnDefinition = "int default 0")
    private int view;

    @Column(name="photo") // 이미지 경로
    private String photo;

    private String imgName; // 이미지 파일명

    private String imgUrl; // 원본 파일명

    @Column(name="recommend",columnDefinition = "int default 0")
    private int recommend;

    @JsonIgnore
    @BatchSize(size = 500)
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendEntity> recommends;

    @JsonIgnore
    @BatchSize(size = 500)
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

    public DiaryEntity(UserEntity user, Date writeDate, String content, String emotion, String photo, String imgName, String imgUrl) {
        this.user = user;
        this.writeDate = writeDate;
        this.content = content;
        this.emotion = emotion;
        this.photo = photo;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public void updateDiary(Date writeDate, String emotion, DiaryRequestDTO diaryRequestDTO) {
        this.writeDate = writeDate;
        this.content = diaryRequestDTO.getContent();
        this.emotion = emotion;
        this.view = diaryRequestDTO.getViews();
        this.photo = diaryRequestDTO.getPhoto();
    }
}

