package com.example.rewardservice.shop;

import com.example.rewardservice.image.application.dto.StoreImageDto;
import com.example.rewardservice.image.application.service.ProfileImageService;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.application.service.ProductService;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@DisplayName("상품 등록, 수정이 가능한지 확인하는 테스트")
class ProductRegisterTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private ProfileImageService profileImageService;

    @Autowired
    private UserRepository userRepository;

    private User mockUser;
    private RegisterProductRequest registerProductRequest;
    private UpdateProductRequest updateProductRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        // 실제 데이터베이스에 들어가있는 유저uuid
        mockUser.setId(UUID.fromString("188d690e-f3be-4dbd-a938-414cf6416ac5"));
        mockUser.setEmail("company@example.com");
        mockUser.setName("Company Name");
        mockUser.setprofileImage("defaultImage.jpg");
        userRepository.saveAndFlush(mockUser);  // 저장 후 즉시 플러시하여 DB에 반영

        MultipartFile image = new MockMultipartFile("productImages", "image.jpg", "image/jpeg", "image content".getBytes());

        registerProductRequest = new RegisterProductRequest();
        registerProductRequest.setCategory("카페");
        registerProductRequest.setProductName("스타벅스 아메리카노 ICE");
        registerProductRequest.setPrice(1000);
        registerProductRequest.setProductImages(Collections.singletonList(image));
        registerProductRequest.setDescription("스타벅스 ICE아메리카노입니다.");

        updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setCategory("카페");
        updateProductRequest.setProductName("스타벅스 아메리카노 HOT");
        updateProductRequest.setPrice(1200);
        updateProductRequest.setProductImages(Collections.singletonList(image));
        updateProductRequest.setDescription("스타벅스 Hot아메리카노입니다.");

        List<StoreImageDto> storeImageDtos = Collections.singletonList(new StoreImageDto("image.jpg", "storedImage.jpg"));
        when(profileImageService.storeProfileImages(anyList())).thenReturn(storeImageDtos);
    }

    @Test
    @DisplayName("상품을 성공적으로 등록할 수 있어야 한다.")
    public void testCreateProduct() {
        // When
        ProductInfoResponse response = productService.createProduct(mockUser, registerProductRequest);

        // Then
        assertNotNull(response);
        assertEquals(registerProductRequest.getCategory(), response.getCategory());
        assertEquals(mockUser.getName(), response.getCompanyName());
        assertEquals(registerProductRequest.getProductName(), response.getProductName());
        assertEquals(registerProductRequest.getPrice(), response.getPrice());
        assertEquals(registerProductRequest.getDescription(), response.getDescription());
    }

    @Test
    @DisplayName("상품을 성공적으로 조회할 수 있어야 한다")
    public void testGetProductById() {
        // Given
        ProductInfoResponse createdProduct = productService.createProduct(mockUser, registerProductRequest);
        UUID productId = createdProduct.getId();

        // When
        ProductInfoResponse response = productService.getProductById(productId);

        // Then
        assertNotNull(response);
        assertEquals(createdProduct.getId(), response.getId());
        assertEquals(createdProduct.getCategory(), response.getCategory());
        assertEquals(createdProduct.getCompanyName(), response.getCompanyName());
        assertEquals(createdProduct.getProductName(), response.getProductName());
        assertEquals(createdProduct.getPrice(), response.getPrice());
        assertEquals(createdProduct.getDescription(), response.getDescription());
    }
}
