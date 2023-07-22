package io.codertown.web.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResult extends SignUpResult{
    private String token;

    @Builder
    public SignInResult(boolean success, int code, String msg, String token) {
        super(success, code, msg);
        this.token = token;
    }
}
