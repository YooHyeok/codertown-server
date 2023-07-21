package io.codertown.web.user;

import lombok.Getter;

@Getter
public enum CommonResponse {
    SUCCESS(0, "Success"), FAIL(-1, "Fail");
    int code;
    String msg;

    CommonResponse(int i, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
