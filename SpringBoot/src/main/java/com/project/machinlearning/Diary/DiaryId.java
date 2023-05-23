package com.project.machinlearning.Diary;

import com.project.machinlearning.User.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DiaryId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private UserEntity user;

    @Temporal(TemporalType.DATE)
    private Date writeDate;

}