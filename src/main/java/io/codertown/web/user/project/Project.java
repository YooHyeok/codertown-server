package io.codertown.web.user.project;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectNo;
    private String projectTitle;
    private LocalDateTime startDate; // 시작 일자
    private LocalDateTime expectedEndDate; // 종료 예정 일자 (목표)

    @Enumerated
    private TotalStatus projectStatus; //프로젝트 전체 상태 - 모집(RECURUIT), 진행(RUN), 무산(FAIL), 종료(CLOSED)
    @Enumerated
    private PersonalStatus personalStatus; //프로젝트 개인 상태 - 대기(WAIT), 진행(RUN), 하차(QUIT), 종료(CLOSED)
}
