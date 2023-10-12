package io.codertown.web.payload.response;

import io.codertown.web.dto.ProjectDto;
import io.codertown.web.dto.RecruitDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CokkiriDetailResponse {
    private RecruitDto cokkiriDto;
    private ProjectDto projectDto;
//    private Boolean isChatMaden;
    private List<Long> takedProjectPartNos;
}
