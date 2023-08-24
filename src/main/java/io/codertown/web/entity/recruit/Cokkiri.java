package io.codertown.web.entity.recruit;

import io.codertown.web.dto.CokkiriUpdateDto;
import io.codertown.web.entity.project.Project;
import io.codertown.web.payload.request.CokkiriSaveRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@ToString(callSuper = true)
@SuperBuilder
@Getter
@NoArgsConstructor
@Entity
public class Cokkiri extends Recruit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer objectWeek; //목표 기간(주)

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Project도 함께 저장한다.
    @JoinColumn(name = "PROJECT_NO")
    private Project project;

    /**
     * 저장을 위한 Cokkiri 생성 메소드
     * @param request
     * @return
     */
    public static Cokkiri createCokkiri(CokkiriSaveRequest request) {
        Project project = Project.builder()
                .build().createProject(request);
        Cokkiri build = Cokkiri.builder()
                .recruitUser(request.getUser())//부모 변수 초기화 가능
                .title(request.getCokkiriTitle()) //부모 변수 초기화 가능
                .objectWeek(request.getObjectWeek())
                .link(request.getLink()) //부모 변수 초기화 가능
                .project(project)
                .content(request.getContent())//부모 변수 초기화 가능
                .status(false)
                .build();
        return build;
    }

    /**
     * 변경감지 수정을 위한 Cokkiri 초기화 메소드
     * @param cokkiriUpdate
     */
    public void updateCokkiri(CokkiriUpdateDto cokkiriUpdate) {
        updateRecruit(cokkiriUpdate.getCokkiriTitle(), cokkiriUpdate.getLink(), cokkiriUpdate.getContent());
        this.objectWeek = cokkiriUpdate.getObjectWeek();
        project.updateProject(cokkiriUpdate.getProjectSubject(), cokkiriUpdate.getTeamname());
    }
}
