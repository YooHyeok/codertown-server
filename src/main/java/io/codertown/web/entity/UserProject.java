package io.codertown.web.entity;


import io.codertown.web.entity.project.Project;
import io.codertown.web.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"project", "projectPart"})
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
        projectPart.getUserProjects().add(this);
        projectPart.increaseUserCount();
        this.projectPart = projectPart;
    }

    public void removeProjectPart() {
        this.projectPart.getUserProjects().remove(this);
        this.projectPart.decreaseUserCount();
    }


    /**
     * [회원 프로젝트 생성]
     * @param projectUser
     * @param projectPart
     * @return
     */
    public UserProject createUserProject(User projectUser, ProjectPart projectPart) {
        UserProject userProject = UserProject.builder()
                .projectUser(projectUser) //참여자 저장
                .project(projectPart.getProject()) //프로젝트 저장
                .projectPart(this.projectPart) // 프로젝트 파트 저장
                .build();
        userProject.addProjectPart(projectPart);
        projectPart.getProject().getUserProjects().add(userProject); //프로젝트의 userProjectList에 양방향 주입
        return userProject;
    }
}
