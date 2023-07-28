package io.codertown.web.user.payload;

import lombok.Data;

@Data
public class UserEditRequest {
    private String originEmail;
    private String changeEmail;
    private String password;
    private String nickname;
    private String profileIcon;
}
