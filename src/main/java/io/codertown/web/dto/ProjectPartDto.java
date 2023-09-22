package io.codertown.web.dto;

import io.codertown.web.entity.ProjectPart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPartDto {

    private Long projectPartNo;
    private Long partNo;
    private String partName;
    private int recruitCount;
    private int currentCount;

    public static ProjectPartDto entityToDto(ProjectPart projectPart) {
        return ProjectPartDto.builder()
                .projectPartNo(projectPart.getId()) // 프로젝트파트 번호
                .partNo(projectPart.getPart().getId()) // 파트 번호
                .partName(projectPart.getPart().getPartName()) // 파트 이름
                .recruitCount(projectPart.getRecruitCount()) // 모집 인원
                .currentCount(projectPart.getCurrentCount()) // 지원 인원
                .build();
    }
}
