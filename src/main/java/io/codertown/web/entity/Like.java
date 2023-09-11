package io.codertown.web.entity;

import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.entity.user.User;

import javax.persistence.*;

@Entity
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
