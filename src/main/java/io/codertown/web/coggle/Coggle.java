package io.codertown.web.coggle;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true) // 부모 클래스인 AuditingBaseEntity 필드를 포함하는 역할 수행
@Entity
public class Coggle extends BaseTimeStampEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coggleNo;
    @Check(constraints = "IN ('T', 'C', 'D')")
    private Character category;
    private String title;
    private String content;

    @ColumnDefault("false")
    private Boolean status; // 글상태 True : 삭제 | False : 정상

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer", referencedColumnName = "USER_NO")
    private User user;


}
