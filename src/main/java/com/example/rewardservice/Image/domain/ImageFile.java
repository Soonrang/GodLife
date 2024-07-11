package com.example.rewardservice.Image.domain;

import com.example.rewardservice.Image.dto.StoreImageDto;
import com.example.rewardservice.Image.imageException.ImageException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ImageFile {

    //파일 확장자 구분
    private static final String EXTENSION_DELIMITER = ".";
    private static final List<String> WHITE_IMAGE_EXTENSION = List.of("jpg","jpeg","png");

    @Value("${com.example.image.user.dir}")
    private String imageStoreDir;

    public List<StoreImageDto> storeImageFiles(final List<MultipartFile> files) {
        final List<StoreImageDto> storeImageDtos = new ArrayList<>();

        for(MultipartFile file : files) {
            if(file.isEmpty()) {
                throw new ImageException("File is empty");
            }
            storeImageDtos.add(storeImageFile(file));
        }
        return storeImageDtos;
    }

    public StoreImageDto storeImageFile(final MultipartFile file) {
        try {
            final String originalFilename = file.getOriginalFilename();
            final String storeImageFilename = createStoreImageFileName(originalFilename);
            final String fullPath = findFullPath(storeImageFilename);

            file.transferTo(new File(fullPath));
            return new StoreImageDto(originalFilename, storeImageFilename);
        } catch (IOException e) {
            throw new ImageException("이미지 저장에 실패했습니다.");
        }
    }

    private String findFullPath(final String storeImageFilename) {
        return imageStoreDir + storeImageFilename;
    }

    private String createStoreImageFileName(final String originalFilename) {
        final String extension = extractExtension(originalFilename);
        validateImageFileExtension(extension);
        final String uuid = UUID.randomUUID().toString();

        return uuid + EXTENSION_DELIMITER + extension;
    }

    private String extractExtension(final String originalFilename) {
        int position = originalFilename.lastIndexOf(EXTENSION_DELIMITER);
        return originalFilename.substring(position + 1);
    }

    private void validateImageFileExtension(final String extension) {
    if(!WHITE_IMAGE_EXTENSION.contains(extension)) {
        throw new ImageException("지원하지 않는 확장자입니다.: " + extension);
    }
    }

}
