package io.codertown.web.payload.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String originEmail;
    private String changeEmail;
    private String password;
    private String nickname;
    private String profileIcon;
}
