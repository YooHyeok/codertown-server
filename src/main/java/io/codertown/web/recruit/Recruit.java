package io.codertown.web.recruit;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recruit extends BaseTimeStampEntity {

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
