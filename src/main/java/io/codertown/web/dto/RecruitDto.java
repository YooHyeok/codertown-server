package io.codertown.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruitDto {
    private String title;
    private String link;
    private String content;
}
