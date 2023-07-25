package io.codertown.web.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignInResult {
    private String createToken;
    private String refreshToken;
    private String rememberMeToken;
    private String email;
    private String nickname;

}
