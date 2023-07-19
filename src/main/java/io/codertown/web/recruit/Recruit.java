package io.codertown.web.recruit;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.user.User;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "category")
public abstract class Recruit extends BaseTimeStampEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_no")
    private Long id;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private User recruitUser;

}
