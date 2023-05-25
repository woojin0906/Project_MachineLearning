package com.project.machinlearning.Recommend;

import com.project.machinlearning.Recommend.DTO.RecommendRequestDTO;
import org.springframework.web.bind.annotation.*;

/**
 *    다이어리 좋아요 숫자 조절, 좋아요 기록 저장
 *
 *   @version          1.00 / 2023.05.23
 *   @author           한승완
 */
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {


    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @PostMapping("/save") // 좋아요 누르기
    public Long saveRecommend(@RequestBody RecommendRequestDTO recommendRequestDTO){
        return recommendService.saveRecommend(recommendRequestDTO);
    }

}
