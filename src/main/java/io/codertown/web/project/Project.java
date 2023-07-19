package io.codertown.web.project;

import io.codertown.web.userproject.UserProject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectNo;
    private String projectTitle;

    @Enumerated
    private TotalStatus projectStatus; //프로젝트 전체 상태 - 모집(RECURUIT), 진행(RUN), 무산(FAIL), 종료(CLOSED)
    @Enumerated
    private PersonalStatus personalStatus; //프로젝트 개인 상태 - 대기(WAIT), 진행(RUN), 하차(QUIT), 종료(CLOSED)

    @OneToMany(mappedBy = "project")
    private List<UserProject> projects = new ArrayList<>();

    private LocalDateTime startDate; // 시작 일자
    private LocalDateTime expectedEndDate; // 종료 예정 일자
    private LocalDateTime lastClosingDate; // 최종 종료 일자 (목표)

}
