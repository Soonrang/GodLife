package com.example.rewardservice.challenge.application.request;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class ChallengeCreateRequest {
    @NotBlank(message = "챌린지 제목은 필수입니다.")
    @Size(max = 100, message = "챌린지 제목은 100자를 초과할 수 없습니다.")
    private String title;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotNull(message = "시작 날짜는 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료 날짜는 필수입니다.")
    private LocalDate endDate;

    @NotNull(message = "시작 시간은 필수입니다.")
    private LocalTime  startTime;

    @NotNull(message = "종료 시간은 필수입니다.")
    private LocalTime  endTime;

    @NotNull(message = "참가 인원 제한은 필수입니다.")
    private long participantsLimit;

    @NotBlank(message = "챌린지 설명은 필수입니다.")
    private String description;

    @NotBlank(message = "인증 방법은 필수입니다.")
    private String authMethod;

    @NotNull(message = "메인 이미지는 필수입니다.")
    private MultipartFile mainImage;

    @NotNull(message = "성공 인증 이미지는 필수입니다.")
    private MultipartFile successImage;

    @NotNull(message = "실패 인증 이미지는 필수입니다.")
    private MultipartFile failImage;

    public Challenge toEntity(User user, String mainImageUrl, String successImageUrl, String failImageUrl) {
        return Challenge.builder()
                .title(this.title)
                .category(this.category)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .participantsLimit(this.participantsLimit)
                .description(this.description)
                .authMethod(this.authMethod)
                .mainImage(mainImageUrl)
                .successImage(successImageUrl)
                .failImage(failImageUrl)
                .user(user)
                .build();
    }
}

