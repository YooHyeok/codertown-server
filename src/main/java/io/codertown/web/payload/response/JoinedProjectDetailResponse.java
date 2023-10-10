package io.codertown.web.payload.response;

import io.codertown.web.dto.ProjectPartDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinedProjectDetailResponse {
    private List<ProjectPartDetailDto> projectPartList;
}
