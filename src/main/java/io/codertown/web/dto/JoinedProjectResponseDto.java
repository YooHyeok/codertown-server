package io.codertown.web.dto;

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
}
