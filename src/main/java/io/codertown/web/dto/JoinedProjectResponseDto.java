package io.codertown.web.dto;

import io.codertown.web.entity.project.PersonalStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinedProjectResponseDto {
    private ProjectDto projectDto;
    private PartDto partDto;
    private PersonalStatusEnum personalStatus;
}
