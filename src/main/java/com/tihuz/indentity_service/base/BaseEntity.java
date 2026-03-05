package com.tihuz.indentity_service.base;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    // Tự động set thời gian khi tạo
    @CreatedDate
    @Column(updatable = false)
     LocalDateTime createdAt;

    // Tự động cập nhật khi update entity
    @LastModifiedDate
     LocalDateTime updatedAt;


}
