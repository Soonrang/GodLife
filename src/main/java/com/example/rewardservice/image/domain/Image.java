package com.example.rewardservice.image.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
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
