package com.example.rewardservice.image.application.service;

import com.example.rewardservice.image.application.dto.ImageDto;
import com.example.rewardservice.image.exception.ImageException;
import lombok.Getter;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Service
public class ImageService {

    //파일 확장자 구분
    private static final String EXTENSION_DELIMITER = ".";
    private static final List<String> WHITE_IMAGE_EXTENSION = List.of("jpg","jpeg","PNG","png");

    //@Value("${com.example.image.user.dir}")
    private String imageStoreDir;

    public List<ImageDto> saveImages(final List<MultipartFile> files) {
        final List<ImageDto> ImageDtos = new ArrayList<>();

        for(MultipartFile file : files) {
            if(file.isEmpty()) {
                throw new ImageException("File is empty");
            }
            ImageDtos.add(saveImage(file));
        }
        return ImageDtos;
    }

    public ImageDto saveImage(final MultipartFile file) {
        try {
            final String originalFilename = file.getOriginalFilename();
            final String saveImageName = createSaveImageName(originalFilename);
            final String fullPath = findFullPath(saveImageName);

            ensureDirectoryExists(imageStoreDir);
            //프로필 이미지 등록시 이미지 크기 줄이기
            reduceFileSize(file, fullPath);

            return new ImageDto(originalFilename, saveImageName);
        } catch (IOException e) {
            throw new ImageException("이미지 저장에 실패했습니다.");
        }
    }

    //이미지 불러오기
    public byte[] getProfileImage(String profileImage) throws IOException {
        Path imagePath = Paths.get(imageStoreDir, profileImage); // 전체 경로로 수정
        return Files.readAllBytes(imagePath);
    }

    private void reduceFileSize(MultipartFile file, String fullPath) throws IOException {
        Thumbnails.of(file.getInputStream())
                .size(500,500)
                .outputQuality(0.8)
                .toFile(new File(fullPath));
    }

    //사진 경로
    public String findFullPath(final String imageName) {
        return imageStoreDir + imageName;
    }

    //난수로 이미지 저장(중복방지)
    private String createSaveImageName(final String originalFilename) {
        final String extension = extractExtension(originalFilename);
        validateProfileImageExtension(extension);
        final String uuid = UUID.randomUUID().toString();

        return uuid + EXTENSION_DELIMITER + extension;
    }

    //확장자 추출
    private String extractExtension(final String originalFilename) {
        int position = originalFilename.lastIndexOf(EXTENSION_DELIMITER);
        return originalFilename.substring(position + 1);
    }

    private void validateProfileImageExtension(final String extension) {
        if(!WHITE_IMAGE_EXTENSION.contains(extension)) {
            throw new ImageException("지원하지 않는 확장자입니다.: " + extension);
        }
    }

    private void ensureDirectoryExists(String directory) {
        File dir = new File(directory);
        if(!dir.exists()) {
            dir.mkdirs();
        }
    }





}
