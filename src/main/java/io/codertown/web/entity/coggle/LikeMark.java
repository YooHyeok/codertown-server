package io.codertown.web.entity.coggle;

import io.codertown.web.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "coggle")
@Getter
public class LikeMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Like_MARK_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COGGLE_NO")
    private Coggle coggle;

    public LikeMark createCoggleLikeMark(User user, Coggle coggle) {
        return LikeMark.builder().user(user).coggle(coggle).build();
    }
}
