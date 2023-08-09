package io.codertown.web.entity.recruit;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

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

    /**
     * Recruit 변경감지 메소드 <br/>
     * Cokkiri와 Mammoth 엔티티 변경감지시 호출된다.
     * @param request
     */
    public void updateRecruit(String tittle, String link, String Content) {
        this.title = tittle;
        this.link = link;
        this.content = Content;
    }
}
