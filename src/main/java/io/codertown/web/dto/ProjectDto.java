package io.codertown.web.dto;

import io.codertown.web.entity.project.Project;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectDto {
    private String subject; //프로젝트주제
    private String projectTitle; //프로젝트명
    private String teamName; //프로젝트팀명
    private String projectStatus; //프로젝트 진행 상태

    private Integer objectWeek;

    private List<ProjectPartSaveDto> projectParts; // 파트

    public static ProjectDto entityToDto(Project project, List<ProjectPartSaveDto> projectParts) {
        return ProjectDto.builder()
                .subject(project.getSubject()) // 주제
                .projectTitle(project.getProjectTitle()) // 프로젝트 제목
                .teamName(project.getTeamName()) // 팀 이름
                .projectStatus(project.getProjectStatus().name()) // 프로젝트 상태 (대기중)
                .objectWeek(project.getObjectWeek())
                .projectParts(projectParts) // 프로젝트별 파트 목록
                .build();
    }
}
