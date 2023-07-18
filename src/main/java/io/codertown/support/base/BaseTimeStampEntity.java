package io.codertown.support.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


/**
 * 전역 타임 스탬프 Auditing 엔터티 <p>
 * 최초 작성일시, 최종 수정일시 <p>
 * 해당 엔티티 클래스 상속시 최초 작성일시, 최종 수정일시 컬럼 값 자동주입 한다
 */
@Setter @Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeStampEntity {
    @CreatedDate
    @Column
    private LocalDateTime firtRegDate;

    @LastModifiedDate
    @Column
    private LocalDateTime lastModDate;

}