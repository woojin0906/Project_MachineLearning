package com.project.machinlearning.Recommend;

import com.project.machinlearning.Diary.DiaryEntity;
import com.project.machinlearning.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@ToString(exclude = {"user", "diary"})
@IdClass(RecommendId.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recommend")
public class RecommendEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private UserEntity user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numId")
    private DiaryEntity diary;

}
