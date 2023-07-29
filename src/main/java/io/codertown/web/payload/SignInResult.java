package io.codertown.web.payload;

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
