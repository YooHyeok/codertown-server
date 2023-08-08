package io.codertown.web.entity;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.request.CoggleUpdateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@ToString(callSuper = true) // 부모 클래스인 AuditingBaseEntity 필드를 포함하는 역할 수행
@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
public class Coggle extends BaseTimeStampEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coggleNo;
    @Check(constraints = "IN ('T', 'C', 'D')")
    private Character category;
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ColumnDefault("false")
    private Boolean status; // 글상태 True : 삭제 | False : 정상

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer", referencedColumnName = "USER_NO")
    private User user;

    /**
     * 코글 수정 - 변경감지 메소드 <br/>
     * 제목과 내용만 수정이 가능하다. <br/>
     * 카테고리 수정 불가 (관리자만 가능)
     * @param request
     */
    public void updateCoggle(CoggleUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }
}
