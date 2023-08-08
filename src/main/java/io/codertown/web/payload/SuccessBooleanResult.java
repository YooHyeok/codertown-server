package io.codertown.web.payload;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessBooleanResult {
    private Boolean success;

    public SuccessBooleanResult setResult(Boolean result) {
        return SuccessBooleanResult.builder().success(result).build();
    }
}
