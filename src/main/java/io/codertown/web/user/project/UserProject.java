package io.codertown.web.user.project;


import io.codertown.web.user.User;
import javax.persistence.*;

@Entity
public class UserProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private User projectUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_NO")
    private Project project;
}
