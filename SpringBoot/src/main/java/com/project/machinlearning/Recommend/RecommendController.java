package com.project.machinlearning.Recommend;

import com.project.machinlearning.Recommend.DTO.RecommendRequestDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {


    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @PostMapping("/save")
    public Long saveRecommend(@RequestBody RecommendRequestDTO recommendRequestDTO){
        return recommendService.saveRecommend(recommendRequestDTO);
    }

}
