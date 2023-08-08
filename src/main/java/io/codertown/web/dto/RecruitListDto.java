package io.codertown.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruitListDto {
    private CokkiriDto cokkiriDto;
    private MammothDto mammothDto;
    private ProjectDto projectDto;
}
