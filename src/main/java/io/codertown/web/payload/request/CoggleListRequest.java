package io.codertown.web.payload.request;

import lombok.Data;

@Data
public class CoggleListRequest {
    private Character category;
    private String writer; //검색 조건 컨디션
    private String keyword; //검색 조건 컨디션
}
