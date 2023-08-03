package io.codertown.web.dto;

import io.codertown.support.PageInfo;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoggleListDto {
    private PageInfo pageInfo;
    private List<CoggleDto> coggleList;
}
