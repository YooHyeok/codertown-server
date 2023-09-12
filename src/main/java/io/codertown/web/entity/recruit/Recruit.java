package io.codertown.web.entity.recruit;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.entity.LikeMark;
import io.codertown.web.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public abstract class Recruit extends BaseTimeStampEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_no")
    private Long id;

    private String title;

    private String link;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private User recruitUser;

    @OneToMany(mappedBy = "recruit")
    private List<LikeMark> likeMark;

    @Column(columnDefinition = "boolean default false constraint status check(status in(true,false))")
    @ColumnDefault(value = "false")
    private Boolean status; // 글상태 True : 삭제 | False : 정상

    private Long views;

    /**
     * Recruit 수정 변경감지 메소드 <br/>
     * Cokkiri와 Mammoth 엔티티 변경감지시 호출된다.
     * @param tittle
     * @param link
     * @param Content
     */
    public void updateRecruit(String tittle, String link, String Content) {
        this.title = tittle;
        this.link = link;
        this.content = Content;
    }

    public void incrementViews() {
        this.views ++;
    }

    /**
     * Recruit 삭제 변경감지 메소드 <br/>
     * Cokkiri와 Mammoth 엔티티 변경감지시 호출된다.
     * @param status
     */
    public void deleteRecruit(Boolean status) {
        this.status = status;
    }
}
