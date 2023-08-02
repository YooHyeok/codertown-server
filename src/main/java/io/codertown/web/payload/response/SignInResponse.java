package io.codertown.web.payload.response;

import io.codertown.web.payload.SignInResult;
import io.codertown.web.payload.SignStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResponse {
    private SignInResult signInResult;
    private SignStatus signStatus;
}
