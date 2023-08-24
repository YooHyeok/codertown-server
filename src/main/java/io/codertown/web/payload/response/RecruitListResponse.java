package io.codertown.web.payload.response;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.RecruitListDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecruitListResponse {

    List<RecruitListDto> recruitList;
    private PageInfo pageInfo;

    private Long articleCount; //전체 조회 개수
}
