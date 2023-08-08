package io.codertown.web.payload.request;

import lombok.Data;

@Data
public class MammothUpdateRequest {
    private Long recruitNo;
    private String title;
    private String link;
    private String content;
    private String location;
}
