package com.example.rewardservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public abstract class BaseEntity {

    // 객체 입장에서 공통된 부분(생성시간, 수정시간) 나올 때 사용하는 어노테이션 @MappedSuperclass

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime  createdAt;

    @LastModifiedDate
    @Column(name= "modifiedAt")
    private LocalDateTime  modifiedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

}
