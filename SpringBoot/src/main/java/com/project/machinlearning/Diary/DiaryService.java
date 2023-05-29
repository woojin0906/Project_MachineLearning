package com.project.machinlearning.Diary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.machinlearning.Comment.CommentEntity;
import com.project.machinlearning.Comment.CommentRepository;
import com.project.machinlearning.Comment.DTO.CommentResponseDTO;
import com.project.machinlearning.Diary.DTO.DiaryRequestDTO;
import com.project.machinlearning.Diary.DTO.DiarySpecificationResponseDTO;
import com.project.machinlearning.User.UserEntity;
import com.project.machinlearning.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;


/**
 *    다이어리 등록, 수정, 조회, 검색
 *    - 서비스 로직
 *
 *   @version          1.00 / 2023.05.23
 *   @author           한승완
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {


    @Value(value = "${itemImgLocation}")
    private String itemImgLocation;

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository;

    private final FileService fileService;

    /**
     * flask api server 접근하여 감정을 분류한 후 저장, 사진 저장
     * - 한승완 2023.05.23, 전우진 2023.05.28
     */
    public int saveDiary(DiaryRequestDTO diaryRequestDTO, MultipartFile itemImgeFileList) {

        String oriImgName = itemImgeFileList.getOriginalFilename();

        String imgName = "";

        String imgUrl = "";

        UserEntity user = userRepository.findByNickName(diaryRequestDTO.getNickName());

        if(user != null){
            Date currentDate = new Date();
            Optional<DiaryEntity> currentDiary = diaryRepository.findByUserAndWriteDate(user, currentDate);

            if(currentDiary.isPresent()){
                throw new IllegalStateException("이미 등록된 사용자 입니다.");
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

                    if(!StringUtils.isEmpty(oriImgName)) {
                        // 실제 이미지 값
                        // 파일의 정보는 byte배열
                        imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgeFileList.getBytes());
                        imgUrl = "/images/item/" + imgName;
                    }
                    System.out.println(response.statusCode());

                    // 응답 처리
                    if (response.statusCode() == 200) {
                        String responseBody = response.body();
                        JsonNode responseJson = objectMapper.readTree(responseBody);
                        String emotion = responseJson.get("response").asText();
                        // 응답 값 받아서 감정값을 포함하여 DB 저장
                        System.out.println(diaryRequestDTO.getContent() + emotion + oriImgName + imgName+ imgUrl);
                        DiaryEntity diary = new DiaryEntity(user, currentDate, diaryRequestDTO.getContent(),emotion,imgUrl, oriImgName, imgName );
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


    /**
     * 다이어리 탐색하여 있을경우 수정 후 저장
     * - 전우진 2023.05.24
     */
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
                        diary.setImgName(diaryRequestDTO.getImgName());
                        diary.setImgUrl(diaryRequestDTO.getImgUrl());
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

    /**
     * 다이어리를 탐색한 후 삭제
     * - 전우진 2023.05.24
     */
    public String deleteDiary(Long numId) {
        DiaryEntity diary = diaryRepository.findByNumId(numId)
                .orElse(null);
        if (diary == null) {
            return "일기가 존재하지 않습니다.";
        }
        diaryRepository.delete(diary);
        return "삭제가 완료되었습니다.";
    }

    /**
     * 단일 다이어리 조회수 1 증가 후 다이어리, 좋아요, 댓글 리턴
     * - 한승완 2023.05.26
     */
    public DiarySpecificationResponseDTO getDiaryWithCommentsAndRecommendations(Long numId) {
        Optional<DiaryEntity> diaryOptional = diaryRepository.getDiary(numId);
        if (diaryOptional.isPresent()) {
            DiaryEntity diaryEntity = diaryOptional.get();
            diaryEntity.setView(diaryEntity.getView()+1);
            List<CommentEntity> commentEntities = diaryEntity.getComments();

            List<CommentResponseDTO> commentDtoList = commentEntities.stream()
                    .map(comment -> new CommentResponseDTO(comment.getCid(), comment.getWriteDate(), comment.getContent(), comment.getEmotion(), comment.getUser().getUid()))
                    .collect(Collectors.toList());

            String emotion = diaryEntity.getEmotion();
            String imgurl="";
            if(emotion.equals("공포")) {
                imgurl="/img/emoji.png";
            } else if(emotion.equals("놀람")) {
                imgurl="/img/surprised.png";
            } else if(emotion.equals("분노")) {
                imgurl="/img/angry.png";
            } else if(emotion.equals("슬픔")) {
                imgurl="/img/sad.png";
            } else if(emotion.equals("중립")) {
                imgurl="/img/neutral.png";
            } else if(emotion.equals("행복")) {
                imgurl="/img/happy.png";
            } else if(emotion.equals("혐오")) {
                imgurl="/img/confused.png";
            }

            return new DiarySpecificationResponseDTO(
                    diaryEntity.getNumId(),
                    diaryEntity.getUser().getUid(),
                    diaryEntity.getWriteDate(),
                    diaryEntity.getContent(),
                    imgurl,
                    diaryEntity.getView(),
                    diaryEntity.getPhoto(),
                    diaryEntity.getRecommend(),
                    commentDtoList
            );
        } else {
            return new DiarySpecificationResponseDTO();
        }
    }

    /**
     * 다이어리를 페이징 후 최근 값 부터 DTO 형태로 리턴
     * - 한승완 2023.05.25
     */
    public List<DiarySpecificationResponseDTO> getAllDiariesWithComments(int page,UserEntity user) {

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("numId").descending());

        List<DiaryEntity> diaryEntities = diaryRepository.findAllWithComments(pageable);
        List<DiarySpecificationResponseDTO> result = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntities) {
            List<CommentResponseDTO> commentDtoList = diaryEntity.getComments().stream()
                    .map(comment -> new CommentResponseDTO(comment.getCid(), comment.getWriteDate(), comment.getContent(), comment.getEmotion(), comment.getUser().getUid()))
                    .collect(Collectors.toList());

            String emotion = diaryEntity.getEmotion();
            String imgurl="";
            if(emotion.equals("공포")) {
                imgurl="/img/emoji.png";
            } else if(emotion.equals("놀람")) {
                imgurl="/img/surprised.png";
            } else if(emotion.equals("분노")) {
                imgurl="/img/angry.png";
            } else if(emotion.equals("슬픔")) {
                imgurl="/img/sad.png";
            } else if(emotion.equals("중립")) {
                imgurl="/img/neutral.png";
            } else if(emotion.equals("행복")) {
                imgurl="/img/happy.png";
            } else if(emotion.equals("혐오")) {
                imgurl="/img/confused.png";
            }

            DiarySpecificationResponseDTO diaryResponseDto = new DiarySpecificationResponseDTO(
                    diaryEntity.getNumId(),
                    diaryEntity.getUser().getUid(),
                    diaryEntity.getWriteDate(),
                    diaryEntity.getContent(),
                    imgurl,
                    diaryEntity.getView(),
                    diaryEntity.getPhoto(),
                    diaryEntity.getRecommend(),
                    diaryEntity.getRecommends().stream().anyMatch(recommendEntity -> recommendEntity.getUser().equals(user))
                    ,commentDtoList
            );

            result.add(diaryResponseDto);
        }

        return result;
    }


    /**
     * 다이어리를 감정으로 검색하여 동일한 감정만 리턴
     * - 한승완 2023.05.25
     */
    public List<DiarySpecificationResponseDTO> listDiaryByEmotion(String emotion, int page, UserEntity user) {

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("numId").descending());
        List<DiaryEntity> diaryEntities = diaryRepository.findByEmotionWithComments(emotion, pageable);
        List<DiarySpecificationResponseDTO> result = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntities) {
            List<CommentResponseDTO> commentDtoList = diaryEntity.getComments().stream()
                    .map(comment -> new CommentResponseDTO(comment.getCid(), comment.getWriteDate(), comment.getContent(), comment.getEmotion(), comment.getUser().getUid()))
                    .collect(Collectors.toList());

            emotion = diaryEntity.getEmotion();
            String imgurl="";
            if(emotion.equals("공포")) {
                imgurl="/img/emoji.png";
            } else if(emotion.equals("놀람")) {
                imgurl="/img/surprised.png";
            } else if(emotion.equals("분노")) {
                imgurl="/img/angry.png";
            } else if(emotion.equals("슬픔")) {
                imgurl="/img/sad.png";
            } else if(emotion.equals("중립")) {
                imgurl="/img/neutral.png";
            } else if(emotion.equals("행복")) {
                imgurl="/img/happy.png";
            } else if(emotion.equals("혐오")) {
                imgurl="/img/confused.png";
            }

            DiarySpecificationResponseDTO diaryResponseDto = new DiarySpecificationResponseDTO(
                    diaryEntity.getNumId(),
                    diaryEntity.getUser().getUid(),
                    diaryEntity.getWriteDate(),
                    diaryEntity.getContent(),
                    imgurl,
                    diaryEntity.getView(),
                    diaryEntity.getPhoto(),
                    diaryEntity.getRecommend(),
                    diaryEntity.getRecommends().stream().anyMatch(recommendEntity -> recommendEntity.getUser().equals(user)),
                    commentDtoList
            );

            result.add(diaryResponseDto);
        }

        return result;
    }

    /**
     * 다이어리를 사용자 이름 검색하여 동일한 감정만 리턴
     * - 한승완 2023.05.25
     */
    public List<DiarySpecificationResponseDTO> listDiaryByNickName(String nickName) {

        UserEntity user = userRepository.findByNickName(nickName);
        List<DiaryEntity> diaryEntities = diaryRepository.findByUserNickNameWithComments(nickName);
        List<DiarySpecificationResponseDTO> result = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntities) {
            List<CommentResponseDTO> commentDtoList = diaryEntity.getComments().stream()
                    .map(comment -> new CommentResponseDTO(comment.getCid(), comment.getWriteDate(), comment.getContent(), comment.getEmotion(), comment.getUser().getUid()))
                    .collect(Collectors.toList());

            String emotion = diaryEntity.getEmotion();
            String imgurl="";
            if(emotion.equals("공포")) {
                imgurl="/img/emoji.png";
            } else if(emotion.equals("놀람")) {
                imgurl="/img/surprised.png";
            } else if(emotion.equals("분노")) {
                imgurl="/img/angry.png";
            } else if(emotion.equals("슬픔")) {
                imgurl="/img/sad.png";
            } else if(emotion.equals("중립")) {
                imgurl="/img/neutral.png";
            } else if(emotion.equals("행복")) {
                imgurl="/img/happy.png";
            } else if(emotion.equals("혐오")) {
                imgurl="/img/confused.png";
            }

            DiarySpecificationResponseDTO diaryResponseDto = new DiarySpecificationResponseDTO(
                    diaryEntity.getNumId(),
                    diaryEntity.getUser().getUid(),
                    diaryEntity.getWriteDate(),
                    diaryEntity.getContent(),
                    imgurl,
                    diaryEntity.getView(),
                    diaryEntity.getPhoto(),
                    diaryEntity.getRecommend(),
                    diaryEntity.getRecommends().stream().anyMatch(recommendEntity -> recommendEntity.getUser().equals(user)),
                    commentDtoList
            );

            result.add(diaryResponseDto);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public boolean validateDiary(Long numId, String name) {
        UserEntity user = userRepository.findByNickName(name);
        DiaryEntity diary = diaryRepository.findById(numId).orElseThrow(EntityNotFoundException::new);

        String writer = diary.getUser().getNickName();

        if(!StringUtils.equals(user.getNickName(), writer)) {
            return false;
        }

        return true;
    }

}
