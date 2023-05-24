package com.project.machinlearning.Recommend.DTO;

import lombok.Data;

@Data
public class RecommendRequestDTO {

    private Long uid;
    private Long num_id;

    public RecommendRequestDTO(Long uid, Long num_id) {
        this.uid = uid;
        this.num_id = num_id;
    }

}
