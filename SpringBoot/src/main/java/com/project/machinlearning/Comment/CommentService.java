package com.project.machinlearning.Comment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.machinlearning.Comment.DTO.CommentRequestDTO;
import com.project.machinlearning.Comment.DirtyComment.DirtyCommentEntity;
import com.project.machinlearning.Comment.DirtyComment.DirtyCommentRepository;
import com.project.machinlearning.Diary.DiaryEntity;
import com.project.machinlearning.Diary.DiaryRepository;
import com.project.machinlearning.User.UserEntity;
import com.project.machinlearning.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository;

    private final DirtyCommentRepository dirtyCommentRepository;

    /**
     * flask api server 접근하여 감정 분류 후 댓글 저장
     * - 한승완 2023.05.23
     */
    public Long saveComment(CommentRequestDTO commentRequestDTO){

        Optional<DiaryEntity> diary = diaryRepository.findByNumId(commentRequestDTO.getNumId());
        Optional<UserEntity> user = userRepository.findById(commentRequestDTO.getUid());
        if (diary.isPresent()) {
            Date currentDate = new Date();

            // 감정 분류 api 호출
            try {
                HttpClient httpClient = HttpClient.newHttpClient();
                String externalApiUrl = "http://127.0.0.1:5000/prediction2";  // API URL

                // JSON 변환
                String content = commentRequestDTO.getContent();
                ObjectMapper objectMapper = new ObjectMapper();
                String requestBody = objectMapper.writeValueAsString(Map.of("content", content));

                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(externalApiUrl))
                        .header("Content-Type", "application/json") // 요청 헤더에 Content-Type 설정 (JSON 형식)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // 요청 바디에 데이터 추가
                        .build();

                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                // 응답 처리
                if (response.statusCode() == 200) {
                    String responseBody = response.body();
                    JsonNode responseJson = objectMapper.readTree(responseBody);
                    String emotion = responseJson.get("response").asText();
                    // 응답 에서 악성 체크
                    if(emotion.equals("악성")){
                        commentRepository.save(new CommentEntity(diary.get(), currentDate, "클린봇이 삭제한 댓글입니다.", "혐오", user.get()));
                        user.get().setCount(user.get().getCount() + 1 );
                        dirtyCommentRepository.save(new DirtyCommentEntity(commentRequestDTO.getContent(), user.get().getNickName()));

                    }else {
                        commentRepository.save(new CommentEntity(diary.get(), currentDate, commentRequestDTO.getContent(), emotion, user.get()));
                    }
                    return 1L;
                }else{
                    return -2L;
                }
            } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    return -2L;
            }
        } else {
            return -1L;
        }
    }

    /**
     * 댓글 삭제 비밀번호 검증하여 댓글 삭제
     * - 한승완 2023.05.24
     */
    public String deleteComment(Long cid) {
        CommentEntity comment = commentRepository.findByCid(cid)
                .orElse(null);
        if (comment == null) {
            return "댓글이 존재하지 않습니다.";
        }
        commentRepository.delete(comment);
        return "삭제가 완료되었습니다.";
    }

    @Transactional(readOnly = true)
    public boolean validateComment(Long cid, String name) {
        UserEntity user = userRepository.findByNickName(name);
        CommentEntity comment = commentRepository.findById(cid).orElseThrow(EntityNotFoundException::new);

        String writer = comment.getUser().getNickName();

        if(!StringUtils.equals(user.getNickName(), writer)) {
            return false;
        }

        return true;
    }

}
