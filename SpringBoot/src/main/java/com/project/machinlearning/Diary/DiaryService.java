package com.project.machinlearning.Diary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.machinlearning.Comment.CommentEntity;
import com.project.machinlearning.Diary.DTO.DiaryRequestDTO;
import com.project.machinlearning.Diary.DTO.DiarySearchDTO;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository;

    public int saveDiary(DiaryRequestDTO diaryRequestDTO){

        UserEntity user = userRepository.findByNickName(diaryRequestDTO.getNickName());
        if(user != null){

            Date currentDate = new Date();
            Optional<DiaryEntity> currentDiary = diaryRepository.findByUserAndWriteDate(user, currentDate);
            if(currentDiary.isPresent()){
                return -1;
            }else{
                try {
                    HttpClient httpClient = HttpClient.newHttpClient();
                    String externalApiUrl = "http://127.0.0.1:5000/prediction";  // API URL

                    // JSON 변환
                    String content = diaryRequestDTO.getContent();
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
                        // 응답 값 받아서 감정값을 포함하여 DB 저장
                        DiaryEntity diary = new DiaryEntity(user, currentDate, diaryRequestDTO.getContent(),emotion,0,"사진");
                        diaryRepository.save(diary);
                        return 1;
                    }else{
                        return -2;
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    return -2;
                }


            }
        }
            return -1;
    }

    public int modifyDiary(Long numId, DiaryRequestDTO diaryRequestDTO) {
        DiaryEntity diary = diaryRepository.findByNumId(numId)
                .orElse(null);
        if (diary != null) {
            Date currentDate = new Date();
                try {
                    HttpClient httpClient = HttpClient.newHttpClient();
                    String externalApiUrl = "http://127.0.0.1:5000/prediction";  // API URL
                    // JSON 변환
                    String content = diaryRequestDTO.getContent();
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
                        // 응답 값 받아서 감정값을 포함하여 DB 저장
                        diary.setWriteDate(currentDate);
                        diary.setEmotion(emotion);
                        diary.setContent(diaryRequestDTO.getContent());
                        diary.setPhoto(diaryRequestDTO.getPhoto());
                        diary.setView(diaryRequestDTO.getViews());
                        diaryRepository.save(diary);
                        return 1;
                    } else {
                        return -2;
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    return -2;
                }
            }
        return -1;
    }

    public String deleteDiary(Long numId) {
        DiaryEntity diary = diaryRepository.findByNumId(numId)
                .orElse(null);
        if (diary == null) {
            return "일기가 존재하지 않습니다.";
        }
        diaryRepository.delete(diary);
        return "삭제가 완료되었습니다.";
    }

    public List<DiaryEntity> listDiary() {
        return diaryRepository.findAll();
    }

    public DiaryEntity postDiary(Long numId) {
        return diaryRepository.findByNumId(numId).get();
    }
}
