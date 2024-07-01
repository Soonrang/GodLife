package com.example.rewardservice.service;

import com.example.rewardservice.domain.Shop.Product;
import com.example.rewardservice.domain.Shop.ProductImage;
import com.example.rewardservice.domain.User.User;
import com.example.rewardservice.dto.ProductDTO;
import com.example.rewardservice.dto.ProductImageDTO;
import com.example.rewardservice.repository.ProductRepository;
import com.example.rewardservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = dtoToEntity(productDTO);
        productRepository.save(product);
        return entityToDto(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(this::entityToDto).orElse(null);
    }

    public ProductDTO updateProduct(UUID productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        User company = userRepository.findById(productDTO.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID"));

        List<ProductImage> updatedImages = productDTO.getProductImages().stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());

        existingProduct.updateProduct(productDTO, company, updatedImages);

        existingProduct = productRepository.save(existingProduct);
        return entityToDto(existingProduct);
    }

    public boolean deleteProduct(UUID id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Product dtoToEntity(ProductDTO productDTO) {
        return Product.builder()
                .category(productDTO.getCategory())
                .productName(productDTO.getProductName())
                .price(productDTO.getPrice())
                .productImages(productDTO.getProductImages().stream()
                        .map(this::dtoToEntity)
                        .collect(Collectors.toList()))
                .description(productDTO.getDescription())
                .build();
    }

    private ProductImage dtoToEntity(ProductImageDTO productImageDTO) {
        return ProductImage.builder()
                .id(productImageDTO.getId())
                .imageUrl(productImageDTO.getImageUrl())
                .build();
    }

    private ProductDTO entityToDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .category(product.getCategory())
                .companyId(product.getCompany().getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .description(product.getDescription())
                .productImages(product.getProductImages().stream()
                        .map(this::entityToDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private ProductImageDTO entityToDto(ProductImage productImage) {
        return ProductImageDTO.builder()
                .id(productImage.getId())
                .imageUrl(productImage.getImageUrl())
                .productId(productImage.getId())
                .build();
    }

}
