package io.codertown.web.entity.recruit;

import io.codertown.web.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "recruit")
@Getter
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_MARK_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECRUIT_NO")
    private Recruit recruit;

//    private Boolean isLiked;

    /**
     * LikeMark 생성 메서드
     */
    public BookMark createRecruitBookMark(User user, Recruit recruit) {
        return BookMark.builder().user(user).recruit(recruit).build();
    }
}
