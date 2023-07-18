package io.codertown.support.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 전역 스탬프 Auditing 엔터티 <p>
 * 최초 작성자, 최종 수정자 <p>
 * 최초 작성일시, 최초 수정일시 <- BaseTimeStampEntity 상속<p>
 * 해당 엔티티 클래스 상속시 최초 작성자, 최초 수정자 + 최초 작성일시, 최종 수정일시 값이 자동주입 된다.
 */
@Setter @Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseStampEntity extends BaseTimeStampEntity{
    @CreatedBy
    @Column
    private String regId;

    @LastModifiedBy
    @Column
    private String modId;

}
