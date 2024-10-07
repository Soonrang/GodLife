package com.example.rewardservice.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Value("${com.example.image.user.dir}")
//    private String imageStoreDir;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 이미지 파일 경로 설정
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations("file:" + imageStoreDir);

//        // Swagger UI 리소스 경로 설정
//        registry.addResourceHandler("/swagger-ui/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
//    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://www.pointsmilet.store", "http://localhost:5173","http://3.34.249.62:8080") // 프론트엔드 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
