package io.codertown.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecruitListDto {
    private RecruitDto recruitDto;
    private ProjectDto projectDto;
}
