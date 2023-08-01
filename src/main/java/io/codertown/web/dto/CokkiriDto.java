package io.codertown.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CokkiriDto {
    private String title;
    private String content;
    private String recruiteUser;
    private Integer objectWeek;
}
