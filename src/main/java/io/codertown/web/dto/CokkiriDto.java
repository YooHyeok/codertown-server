package io.codertown.web.dto;

import io.codertown.web.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CokkiriDto {
    private String title;
    private String content;
    private User recruiteUser;
    private Integer objectWeek;
}
