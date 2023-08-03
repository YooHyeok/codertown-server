package io.codertown.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoggleDto {
    private Long coggleNo;
    private Character category;
    private String title;
    private String content;
    private Boolean status;

    private String writer;
}
