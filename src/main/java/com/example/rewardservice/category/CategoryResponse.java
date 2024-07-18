package com.example.rewardservice.category;

import com.example.rewardservice.shop.domain.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class CategoryResponse {

    private final UUID id;
    private final String name;

    public static CategoryResponse of(final Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
