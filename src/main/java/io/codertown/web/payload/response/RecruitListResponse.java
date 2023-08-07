package io.codertown.web.payload.response;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.CokkiriDto;
import io.codertown.web.dto.ProjectDto;
import io.codertown.web.dto.RecruitDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruitListResponse {
    private RecruitDto recruitDto;
    private CokkiriDto cokkiriDto;
    private ProjectDto projectDto;
    private PageInfo pageInfo;

}
