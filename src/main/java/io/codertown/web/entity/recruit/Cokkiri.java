package io.codertown.web.entity.recruit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.project.Project;
import io.codertown.web.payload.CokkiriSaveRequest;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@SuperBuilder
@NoArgsConstructor
@Entity
public class Cokkiri extends Recruit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer objectWeek; //목표 기간(주)

    @JsonIgnore
    @OneToMany(mappedBy = "cokkiri")
    private List<ProjectPart> projectParts = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL) // Project도 함께 저장한다.
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
