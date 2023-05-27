package com.project.machinlearning.Diary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    public String uploadFile(String uploadPath, String oriFileName, byte[] fileData) throws IOException {

        UUID uuid = UUID.randomUUID();

        String extension = oriFileName.substring(oriFileName.lastIndexOf("."));

        String savedFileName = uuid.toString() + extension;

        String fileUploadUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadUrl);

        fos.write(fileData);
        fos.close();

        return savedFileName;

    }

    // 파일을 지우는 동작
    public void deleteFile(String filePath) {

        File deleteFile = new File(filePath);

        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제했습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
