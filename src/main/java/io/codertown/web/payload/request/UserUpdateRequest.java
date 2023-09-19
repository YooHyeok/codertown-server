package io.codertown.web.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequest {
    private String loginEmail;
    private String nickname;
    private String password;
    private MultipartFile attachFile;
}
