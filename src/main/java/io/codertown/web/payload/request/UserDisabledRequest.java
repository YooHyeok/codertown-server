package io.codertown.web.payload.request;

import io.codertown.web.entity.user.UserStatusEnum;
import lombok.Data;

@Data
public class UserDisabledRequest {
    private String loginEmail;
    private UserStatusEnum changeStatus;
}
