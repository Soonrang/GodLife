package com.example.rewardservice.common.dataInsert;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.ProductImage;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Environment env;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            addProduct("식음료", "[특가]아이스 카페 아메리카노 T", 4500, 10, "image1.jpg");
            addProduct("식음료", "(ICE)아메리카노", 2000, 10, "image2.jpg");
            addProduct("문화·생활", "네이버페이 포인트 5,000원", 5000, 10, "image3.jpg");
            addProduct("식음료", "[특가]카페 아메리카노 T", 4500, 10, "image4.jpg");
            addProduct("문화·생활", "네이버페이 포인트 10,000원", 10000, 10, "image5.jpg");
            addProduct("문화·생활", "네이버페이 포인트 50,000원", 50000, 10, "image6.jpg");
            addProduct("식음료", "아메리카노(Ice)(TAKE-OUT)", 1500, 10, "image7.jpg");
            addProduct("문화·생활", "신세계 상품권 100,000원", 100000, 10, "image8.jpg");
            addProduct("식음료", "아이스 카페 라떼 T", 5000, 10, "image9.jpg");
            addProduct("문화·생활", "CU 모바일상품권 1만원권", 10000, 10, "image10.jpg");
            addProduct("문화·생활", "GS25 모바일 상품권 1만원권", 10000, 10, "image11.jpg");
            addProduct("문화·생활", "GS25 모바일 상품권 5천원권", 5000, 10, "image12.jpg");
            addProduct("문화·생활", "네이버페이 포인트 30,000원", 30000, 10, "image13.jpg");
            addProduct("문화·생활", "신세계 상품권 50,000원", 50000, 10, "image14.jpg");
            addProduct("식음료", "오늘도 달콤하게 아이스 카페 아메리카노 T+7레이어 가나슈 케이크", 10200, 10, "image15.jpg");
            addProduct("식음료", "[배달의민족] 모바일상품권 2만원", 20000, 10, "image16.jpg");
            addProduct("문화·생활", "CU 모바일상품권 5천원권", 5000, 10, "image17.jpg");
            addProduct("식음료", "시원하게 함께 아이스 카페 아메리카노 T 2잔", 9000, 10,  "image18.jpg");
            addProduct("문화·생활", "이마트 50,000원 상품권", 50000, 10, "image19.jpg");
        };
    }

    private void addProduct(String category, String productName, long price, int stock, String imagePath) {
        Optional<Product> productOptional = productRepository.findByProductName(productName);
        if (!productOptional.isPresent()) {
            Product product = new Product();
            product.setCategory(category);
            product.setProductName(productName);
            product.setPrice(price);
            product.setStock(stock);

            ProductImage productImage = new ProductImage(imagePath);
            productImage.setProduct(product); // Link the image to the product
            product.setProductImages(Collections.singletonList(productImage));

            // 이미지 파일이 존재하는지 확인
            productRepository.save(product);
        }
    }
}