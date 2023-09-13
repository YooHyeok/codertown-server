package io.codertown.web.payload.response;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.JoinedProjectResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinedProjectResponse {

    List<JoinedProjectResponseDto> projectList;

    private PageInfo pageInfo;

    private Long articleCount; //전체 조회 개수
}
