package io.codertown.web.coggle;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinColumn(name = "USER_ID")
    private User user;


}
