package io.codertown.web.payload.response;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.CokkiriDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MammothListResponse {
    private CokkiriDto recruitDto;
    private PageInfo pageInfo;
}
