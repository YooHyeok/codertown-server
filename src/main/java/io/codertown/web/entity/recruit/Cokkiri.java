package io.codertown.web.entity.recruit;

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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Project도 함께 저장한다.
    @JoinColumn(name = "PROJECT_NO")
    private Project project;

    public static Cokkiri createCokkiri(CokkiriSaveRequest requestDto) {
        Project project = Project.builder()
                .build().createProject(requestDto);
        Cokkiri build = Cokkiri.builder()
                .title(requestDto.getCokkiriTitle())
                .content(requestDto.getContent())
                .recruitUser(requestDto.getUser())
                .objectWeek(requestDto.getObjectWeek())
                .project(project)
                .build();
        return build;
    }
}
