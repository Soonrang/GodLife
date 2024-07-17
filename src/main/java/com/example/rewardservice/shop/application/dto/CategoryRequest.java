package com.example.rewardservice.shop.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CategoryRequest {
    @NotNull(message = "카테고리 ID를 입력해주세요.")
    private UUID id;

    @NotBlank(message = "한글 이름을 입력해주세요.")
    @Size(max = 50, message = "한글 이름은 50자를 초과할 수 없습니다.")
    private String name;


}
