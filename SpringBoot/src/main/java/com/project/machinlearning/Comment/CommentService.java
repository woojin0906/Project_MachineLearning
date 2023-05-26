package com.project.machinlearning.Comment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.machinlearning.Comment.DTO.CommentRequestDTO;
import com.project.machinlearning.Diary.DiaryEntity;
import com.project.machinlearning.Diary.DiaryRepository;
import com.project.machinlearning.User.UserEntity;
import com.project.machinlearning.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /**
     * flask api server 접근하여 감정 분류 후 댓글 저장
     * - 한승완 2023.05.23
     */
    public String saveComment(CommentRequestDTO commentRequestDTO){

        Optional<DiaryEntity> diary = diaryRepository.findByNumId(commentRequestDTO.getNumId());
        Optional<UserEntity> user = userRepository.findById(commentRequestDTO.getUid());
        if (diary.isPresent()) {
            Date currentDate = new Date();

            // 감정 분류 api 호출
            try {
                HttpClient httpClient = HttpClient.newHttpClient();
                String externalApiUrl = "http://127.0.0.1:5000/prediction";  // API URL

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
                    // 응답 값 받아서 DB 저장
                    return commentRepository.save(new CommentEntity(diary.get(), currentDate, commentRequestDTO.getContent(),emotion, user.get())).getEmotion();
                }else{
                    return "API 연결 실패";
                }
            } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    return "API 연결 실패";
            }
        } else {
            return "글이 없습니다.";
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





}
