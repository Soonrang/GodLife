package com.example.rewardservice.image.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

import com.example.rewardservice.image.exception.ImageException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class S3ImageService  {
    private static final int MAX_IMAGE_LIST_SIZE = 5;
    private static final int EMPTY_LIST_SIZE = 0;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String upload(MultipartFile image) {
        if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
        return this.uploadImage(image);
    }

    public List<String> uploadMultiple(List<MultipartFile> images) {
        validateSizeOfImages(images);

        List<String> urls = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
                throw new IllegalArgumentException("파일이 비어있습니다.");
            }
            urls.add(this.uploadImage(image));
        }
        return urls;
    }


    private String uploadImage(MultipartFile image) {
        this.validateImageFileExtention(image.getOriginalFilename());
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new  IllegalArgumentException("파일이 비어있습니다.");
        }
    }

    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("확장자를 찾을 수 없습니다.");
        }

        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new IllegalArgumentException("사용가능한 확장자가 아닙니다.");
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename(); //원본 파일 명
        String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

        // 이미지 리사이징
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(image.getInputStream())
                .size(500, 500)
                .outputQuality(0.8)
                .toOutputStream(os);
        byte[] resizedBytes = os.toByteArray();

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

//        InputStream is = image.getInputStream();
//        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extention);
        metadata.setContentLength(resizedBytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(resizedBytes);

        try{
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest); // put image to S3
        }catch (Exception e){
            new IllegalArgumentException("파일 업로드에 실패했습니다.", e);
        }finally {
            byteArrayInputStream.close();
            os.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new IllegalArgumentException("파일 삭제에 실패했습니다.", e);
        }
    }

    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new IllegalArgumentException("이미지 주소를 해석할 수 없습니다.", e);
        }
    }

    private void validateSizeOfImages(final List<MultipartFile> images) {
        if(images.size() > MAX_IMAGE_LIST_SIZE) {
            throw new IllegalArgumentException("이미지 개수를 초과했습니다.");
        }
        if(images.size() == EMPTY_LIST_SIZE) {
            throw new IllegalArgumentException("이미지가 존재하지 않습니다." + EMPTY_LIST_SIZE);
        }
    }

}



