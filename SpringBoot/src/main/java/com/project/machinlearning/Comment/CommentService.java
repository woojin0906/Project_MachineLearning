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

    public String saveComment(CommentRequestDTO commentRequestDTO)throws ParseException {

        String format = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = formatter.parse(commentRequestDTO.getDate());

        UserEntity user = userRepository.findByNickName(commentRequestDTO.getNickname());
        Optional<DiaryEntity> diary = diaryRepository.findByUser(user);

        if (diary.isPresent()) {
            Date currentDate = new Date();

            // 감정 분류 api 호출
            try {
                HttpClient httpClient = HttpClient.newHttpClient();
                String externalApiUrl = "http://127.0.0.1:5000/prediction";  // 외부 API의 엔드포인트 URL

                // 전달할 데이터를 JSON 형식으로 변환
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
                    // 예를 들어, JSON 형식의 응답이라면 JSON 파싱을 통해 값을 추출하고 활용할 수 있습니다.
                    return commentRepository.save(new CommentEntity(diary.get(), currentDate, commentRequestDTO.getContent(),emotion, commentRequestDTO.getPw())).getEmotion();
                }else{
                    return "api 연결 실패";
                }
            } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    return "글 존재 안함";
            }
        } else {
            return "글이없습니다.";
        }
    }




}
