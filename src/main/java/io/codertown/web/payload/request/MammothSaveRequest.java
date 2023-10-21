package io.codertown.web.payload.request;

import io.codertown.web.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MammothSaveRequest {
    private String writer;
    @Schema(hidden = true)
    private User user;
    private String title;
    private String link;
//    private String location;

    private String sido; //시/도
    private String sigungu; //시/군/구
    private String dong; //동
    private String fullLocation; //도로명 풀주소

    private String content;

}
