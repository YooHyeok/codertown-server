package io.codertown.web.entity.project;

import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.UserProject;
import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.payload.request.CokkiriSaveRequest;
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
@ToString(exclude = "cokkiri")
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_NO")
    private Long id;
    private String subject; //프로젝트주제
    private String projectTitle; //프로젝트명
    private String teamName; //프로젝트팀명

    @Enumerated(EnumType.STRING)
    private TotalStatusEnum projectStatus; //프로젝트 전체 상태 - 모집(RECURUIT), 진행(RUN), 무산(FAIL), 종료(CLOSED)
    @Enumerated(EnumType.STRING)
    private PersonalStatusEnum personalStatus; //프로젝트 개인 상태 - 대기(WAIT), 진행(RUN), 하차(QUIT), 종료(CLOSED)

    @OneToMany(mappedBy = "project")
    private List<UserProject> userProjects = new ArrayList<>();
    private Integer objectWeek; //목표 기간(주)
    private LocalDateTime startDate; // 시작 일자
    private LocalDateTime expectedEndDate; // 종료 예정 일자
    private LocalDateTime lastClosingDate; // 최종 종료 일자 (목표)

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectPart> projectParts = new ArrayList<>();

    @OneToOne(mappedBy = "project", orphanRemoval = true)
    private Cokkiri cokkiri;

    public void addProjectParts(ProjectPart projectPart) {
        this.projectParts.add(projectPart);
    }

    public Project createProject(CokkiriSaveRequest request) {
        Project build = Project.builder()
                .subject(request.getProjectSubject())
                .teamName(request.getTeamname())
                .objectWeek(request.getObjectWeek())
                .projectTitle(request.getProjectTitle())
                .projectStatus(TotalStatusEnum.RECURUIT)
//                .personalStatus()
                .userProjects(new ArrayList<>())
                .projectParts(new ArrayList<>()) // 빌더 패턴은 객체 생성과 속성 설정을 분리하여 관리하므로 따로 초기화해야한다.
                .build();
        return build;
    }

    /**
     * 변경감지 수정을 위한 Project 초기화 메소드
     * @param subject, teamName
     */
    public void updateProject(String subject, Integer objectWeek, String teamName) {
        this.subject = subject;
        this.objectWeek = objectWeek;
        this.teamName = teamName;
    }

    /**
     * status 변경감지
     * @param status
     */
    public void changeStatus(String status) {
        TotalStatusEnum changeStatus = null;
        switch (status) {
            case "RECRUIT" :
                changeStatus = TotalStatusEnum.RECURUIT;
                break;
            case "RUN" :
                changeStatus = TotalStatusEnum.RUN;
                if (this.startDate == null) { // 시작일자가 단 한번도 등록된 적이 없다면
                    this.startDate = LocalDateTime.now();
                    this.expectedEndDate = this.startDate.plusWeeks(this.objectWeek); // 종료 예정일자를 최초시작일자 기준 3주후로 지정
                }
                break;
            case "FAIL" :
                changeStatus = TotalStatusEnum.FAIL;
                break;
            case "CLOSED" :
                changeStatus = TotalStatusEnum.CLOSED;
                this.lastClosingDate = LocalDateTime.now();
                break;
        }
        this.projectStatus = changeStatus;
    }
}
