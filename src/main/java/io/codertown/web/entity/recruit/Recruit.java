package io.codertown.web.entity.recruit;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.entity.user.User;
import lombok.AllArgsConstructor;
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

}
