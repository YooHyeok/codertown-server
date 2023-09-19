package io.codertown.web.payload.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String loginEmail;
    private String nickname;
    private String password;
//    private MultipartFile attachFile;
    private String profileUrl;

}
