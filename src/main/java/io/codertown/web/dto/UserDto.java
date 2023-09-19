package io.codertown.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.codertown.web.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String email;
    @JsonIgnore
    private String password;
    private String nickname;
    private byte[] profileUrl;
    private Character gender;
    private String role;

    /* === Entity DTO 변환 === */
    public static UserDto userEntityToDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .password(user.getPassword())
                .gender(user.getGender())
                .build();
    }
}
