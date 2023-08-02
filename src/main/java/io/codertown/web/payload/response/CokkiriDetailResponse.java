package io.codertown.web.payload.response;

import io.codertown.web.dto.CokkiriDto;
import io.codertown.web.dto.ProjectDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CokkiriDetailResponse {
    private CokkiriDto cokkiriDto;
    private ProjectDto projectDto;
}
