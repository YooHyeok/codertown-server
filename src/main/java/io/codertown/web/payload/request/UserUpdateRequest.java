package io.codertown.web.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequest {
    private String originEmail;
    private String changeEmail;
    private String password;
    private String nickname;
    private MultipartFile file;
    private String profileIcon;
}
