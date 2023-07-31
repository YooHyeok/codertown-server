package io.codertown.web.entity.project;

import io.codertown.web.entity.UserProject;
import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.payload.CokkiriSaveRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_NO")
    private Long id;
    private String subject; //프로젝트주제
    private String projectTitle; //프로젝트명
    private String teamName; //프로젝트팀명

    @Enumerated
    private TotalStatusEnum projectStatus; //프로젝트 전체 상태 - 모집(RECURUIT), 진행(RUN), 무산(FAIL), 종료(CLOSED)
    @Enumerated
    private PersonalStatusEnum personalStatus; //프로젝트 개인 상태 - 대기(WAIT), 진행(RUN), 하차(QUIT), 종료(CLOSED)

    @OneToMany(mappedBy = "project")
    private List<UserProject> projects = new ArrayList<>();

    private LocalDateTime startDate; // 시작 일자
    private LocalDateTime expectedEndDate; // 종료 예정 일자
    private LocalDateTime lastClosingDate; // 최종 종료 일자 (목표)

    @OneToOne(mappedBy = "project", orphanRemoval = true)
    private Cokkiri cokkiri;

    public static Project createProject(CokkiriSaveRequest request) {
        return Project.builder()
                .subject(request.getProjectSubject())
                .teamName(request.getTeamname())
                .projectTitle(request.getProjectTitle())
                .projectStatus(TotalStatusEnum.RECURUIT)
//                .personalStatus()
                .build();
    }
}
