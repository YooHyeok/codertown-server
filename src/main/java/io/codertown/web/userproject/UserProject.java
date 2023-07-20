package io.codertown.web.userproject;


import io.codertown.web.project.Project;
import io.codertown.web.user.User;
import javax.persistence.*;

@Entity
public class UserProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_PROJECT_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private User projectUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_NO")
    private Project project;
}
