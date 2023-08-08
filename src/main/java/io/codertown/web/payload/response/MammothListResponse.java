package io.codertown.web.payload.response;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.MammothDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MammothListResponse {
    private MammothDto mammothDto;
    private PageInfo pageInfo;
}
