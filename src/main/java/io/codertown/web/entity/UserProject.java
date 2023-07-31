package io.codertown.web.entity;


import io.codertown.web.entity.project.Project;
import io.codertown.web.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_PROJECT_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private User projectUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_NO")
    private Project project;

    /**
     * [회원 프로젝트 생성]
     * @param projectUser
     * @param project
     * @return
     */
    public static UserProject createUserProject(User projectUser, Project project) {
        UserProject userProject = UserProject.builder()
                .projectUser(projectUser)
                .project(project)
                .build();
        return userProject;
    }
}
