package io.codertown.web.payload.request;

import lombok.Data;

@Data
public class CoggleListRequest {
    private Character category;
    private String writer;
}
