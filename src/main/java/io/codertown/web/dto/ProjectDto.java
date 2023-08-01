package io.codertown.web.dto;

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

    private List<ProjectPartDto> projectParts; // 파트
}
