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
/**
 * 프로젝트에 참여 할 때 저장되는 엔터티이다!
 */
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_PART_NO")
    private ProjectPart projectPart; //프로젝트별 파트 번호

    /**
     * 양방향 연관관계 <br/>
     * projectPart 값을 초기화 하면서 참여자목록에 현재객체를 추가한다.
     * @param projectPart
     */
    public void addProjectPart(ProjectPart projectPart) {
        this.projectPart = projectPart;
        projectPart.getUserProjects().add(this);
        projectPart.increaseUserCount();
    }

    public void removeProjectPart() {
        projectPart.getUserProjects().remove(this);
        projectPart.decreaseUserCount();
    }


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
