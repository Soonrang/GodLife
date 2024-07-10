package com.example.rewardservice.Image.domain;

import lombok.Getter;
import org.aspectj.bridge.Message;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.util.StringUtils.getFilenameExtension;

@Getter
public class ImageFile {

    //파일 확장자 구분
    private static final String EXTENSION_DELIMITER = ".";

    private final MultipartFile file;
    private final String hashedName;

    public ImageFile(final MultipartFile file) {
        validateNullImage(file);
        this.file = file;
        this.hashedName = hashName(file);
    }

    private void validateNullImage(final MultipartFile file) {
        if(file.isEmpty()) {
            throw new RuntimeException();
        }
    }

    private String hashName(final MultipartFile image) {
        final String name = image.getOriginalFilename();
        final String filenameExtension = EXTENSION_DELIMITER + getFilenameExtension(name);
        final String nameAndDate = name + LocalDateTime.now();

        try {
            final MessageDigest hashAlgorithm = MessageDigest.getInstance("SHA=256");
            final byte[] hashBytes = hashAlgorithm.digest(nameAndDate.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes) + filenameExtension;
        }catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    private String bytesToHex(final byte[] bytes) {
        return IntStream.range(0, bytes.length) // 바이트 배열을 스트림으로 변환
                .mapToObj(i -> String.format("%02x", bytes[i] & 0xff)) // 각 바이트를 16진수로 변환
                .collect(Collectors.joining()); // 문자열로 연결
    }

    public long getSize() {
        return this.file.getSize();
    }

    public InputStream getInputStream() throws IOException {
        return this.file.getInputStream();
    }

}
