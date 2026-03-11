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
@MappedSuperclass      // no table, field in this will be followed by child entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)    //lắng nghe lifecycle events (create, update).
public abstract class BaseEntity {

    // Tự động set thời gian khi tạo
    @CreatedDate  //Save the time the record was created.
    @Column(updatable = false)  // no update, created once when insert
     LocalDateTime createdAt;

    // Tự động cập nhật khi update entity
    @LastModifiedDate   // save the time the record was updated
     LocalDateTime updatedAt;


}
