package io.codertown.web.dto;

import io.codertown.web.entity.project.Project;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProjectDto {

    private Long projectNo;
    private String subject; //프로젝트주제
    private String projectTitle; //프로젝트명
    private String teamName; //프로젝트팀명
    private String projectStatus; //프로젝트 진행 상태
    private Integer objectWeek;

    private LocalDateTime startDate; // 시작 일자
    private LocalDateTime expectedEndDate; // 종료 예정 일자
    private LocalDateTime lastClosingDate; // 최종 종료 일자 (목표)

    private List<ProjectPartDto> projectParts; // 파트

    public static ProjectDto entityToDto(Project project, List<ProjectPartDto> projectParts) {
        return ProjectDto.builder()
                .projectNo(project.getId())
                .subject(project.getSubject()) // 주제
                .projectTitle(project.getProjectTitle()) // 프로젝트 제목
                .teamName(project.getTeamName()) // 팀 이름
                .projectStatus(project.getProjectStatus().name()) // 프로젝트 상태 (대기중)
                .objectWeek(project.getObjectWeek())
                .projectParts(projectParts) // 프로젝트별 파트 목록
                .startDate(project.getStartDate())
                .expectedEndDate(project.getExpectedEndDate())
                .lastClosingDate(project.getLastClosingDate())
                .build();
    }
}
