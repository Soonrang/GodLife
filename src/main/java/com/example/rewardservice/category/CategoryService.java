package com.example.rewardservice.category;

import com.example.rewardservice.shop.domain.Category;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public UUID save(final CategoryRequest categoryRequest) {
        validateCategoryDuplicate(categoryRequest);
        return categoryRepository.save(Category.of(categoryRequest)).getId();
    }

    private void validateCategoryDuplicate(final CategoryRequest categoryRequest){
        validateCategoryDuplicateId(categoryRequest.getId());
        validateCategoryDuplicateName(categoryRequest.getName());
    }

    private void validateCategoryDuplicateId(final UUID id) {
        if(categoryRepository.existsById(id)) {
            throw new RuntimeException("중복된 카테고리 아이디값");
        }
    }

    private void validateCategoryDuplicateName(final String name) {
        if(categoryRepository.existsByName(name)) {
            throw new RuntimeException("이름이 중복됩니다.");
        }
    }

}
