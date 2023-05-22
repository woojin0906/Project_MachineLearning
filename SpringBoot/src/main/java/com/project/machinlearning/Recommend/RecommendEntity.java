package com.project.machinlearning.Recommend;

import com.project.machinlearning.Diary.DiaryEntity;
import com.project.machinlearning.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@ToString
@IdClass(RecommendId.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recommend")
public class RecommendEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "uid")
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "did")
    @JoinColumn(name = "writeDate")
    private DiaryEntity diary;

}
