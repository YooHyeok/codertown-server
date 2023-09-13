package io.codertown.web.payload.response;

import io.codertown.web.dto.PartDto;
import io.codertown.web.dto.ProjectDto;
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
