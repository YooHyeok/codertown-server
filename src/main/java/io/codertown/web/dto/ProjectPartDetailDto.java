package io.codertown.web.dto;

import io.codertown.web.entity.ProjectPart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPartDetailDto {

    private Long projectPartNo;
    private Long partNo;
    private String partName;
    List<UserProjectDto> userProjectDtoList;
    private int recruitCount;
    private int currentCount;

    public static ProjectPartDetailDto entityToDto(ProjectPart projectPart, List<UserProjectDto> userProjectDtoList) {
        return ProjectPartDetailDto.builder()
                .projectPartNo(projectPart.getId()) // 프로젝트파트 번호
                .partNo(projectPart.getPart().getId()) // 파트 번호
                .partName(projectPart.getPart().getPartName()) // 파트 이름
                .userProjectDtoList(userProjectDtoList)
                .recruitCount(projectPart.getRecruitCount()) // 모집 인원
                .currentCount(projectPart.getCurrentCount()) // 지원 인원
                .build();
    }
}
