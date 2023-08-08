package io.codertown.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MammothDto {

    private String title;
    private String content;
    private String link;
    private UserDto writer; //추후 writer정보 변경가능

    private String location;
}
