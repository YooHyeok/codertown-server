package io.codertown.web.payload.request;

import lombok.Data;

@Data
public class MammothUpdateRequest {
    private Long recruitNo;
    private String title;
    private String link;
    private String content;
    private String location;
    private String sido; //시/도
    private String sigungu; //시/군/구
    private String dong; //동
    private String fullLocation; //도로명 풀주소
}
