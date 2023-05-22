package com.project.machinlearning.User;

import com.project.machinlearning.Diary.DiaryEntity;
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
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name="pw", length = 20)
    private String pw;

    @Column(name="nickName", length = 10)
    private String nickName;

    @OneToMany(mappedBy = "diaryId.user", orphanRemoval = true)
    private List<DiaryEntity> diaries;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<RecommendEntity> recommends;

}
