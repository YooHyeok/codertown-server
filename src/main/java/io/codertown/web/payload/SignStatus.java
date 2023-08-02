package io.codertown.web.payload;

import io.codertown.web.payload.response.CommonResponse;
import io.swagger.annotations.ApiModel;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@ApiModel(description = "회원가입 및 로그인 상태 정보")
public class SignStatus {
    private boolean success;
    private int code;
    private String msg;

    public void setSuccessResult(SignStatus result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    public void setFailResult(SignStatus result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }

}
