package io.codertown.web.payload.request;

import lombok.Data;

@Data
public class CoggleListRequest {

    private Integer page;
    private Character category;
    private String keyword; //검색 조건 컨디션
    private String loginId;
    private String url;
}
