package io.codertown.web.entity;

import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LikeMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKE_MARK_NO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User user;

    @ManyToOne
    @JoinColumn(name = "RECRUIT_NO")
    private Recruit recruit;

    @ManyToOne
    @JoinColumn(name = "COGGLE_NO")
    private Coggle coggle;

//    private Boolean isLiked;

    /**
     * LikeMark 생성 메서드
     */
    public LikeMark createRecruitLikeMark(User user, Recruit recruit) {
        return LikeMark.builder().user(user).recruit(recruit).build();
    }
}
