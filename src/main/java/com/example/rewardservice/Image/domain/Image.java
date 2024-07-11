package com.example.rewardservice.Image.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Image {

    private String uploadName;

    private String storeName;

    public Image(final String uploadName, final String storeName) {
        this.uploadName = uploadName;
        this.storeName = storeName;
    }
}
