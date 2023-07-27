package io.codertown.web.user.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {
    private SignInResult signInResult;
    private SignStatus signStatus;
}
