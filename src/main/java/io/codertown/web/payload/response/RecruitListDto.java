package io.codertown.web.payload.response;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.CokkiriDto;
import io.codertown.web.dto.MammothDto;
import io.codertown.web.dto.ProjectDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruitListDto {
    private CokkiriDto cokkiriDto;
    private MammothDto mammothDto;
    private ProjectDto projectDto;
    private PageInfo pageInfo;

}
