package com.project.machinlearning.Diary.DTO;

import com.project.machinlearning.Comment.DTO.CommentResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class DiarySpecificationResponseDTO {

    private Long numId; // 다이어리 아이디
    private Long uid; // 작성자 닉네임
    private Date writeDate;  // 작성 날짜
    private String content; // 내용
    private String emotion; // 감정
    private int view; // 조회수
    private String photo; // 사진
    private int recommend; // 총 추천 개수
    private boolean isrecommend;

    List<CommentResponseDTO> comments = new ArrayList<>(); // 댓글 리스트


    public DiarySpecificationResponseDTO(Long numId, Long uid, Date writeDate, String content, String emotion, int view, String photo, int recommend, List<CommentResponseDTO> comments) {
        this.numId = numId;
        this.uid = uid;
        this.writeDate = writeDate;
        this.content = content;
        this.emotion = emotion;
        this.view = view;
        this.photo = photo;
        this.recommend = recommend;
        this.comments = comments;
    }

    public DiarySpecificationResponseDTO(Long numId, Long uid, Date writeDate, String content, String emotion, int view, String photo, int recommend, boolean isrecommend, List<CommentResponseDTO> comments) {
        this.numId = numId;
        this.uid = uid;
        this.writeDate = writeDate;
        this.content = content;
        this.emotion = emotion;
        this.view = view;
        this.photo = photo;
        this.recommend = recommend;
        this.isrecommend = isrecommend;
        this.comments = comments;
    }
}

