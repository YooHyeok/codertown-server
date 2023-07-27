package io.codertown.web.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String email;
    private String password;
    private String nickname;
    private String profileIcon;
    private Character gender;
    private String role;
}
