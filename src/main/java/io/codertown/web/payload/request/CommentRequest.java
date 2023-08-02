package io.codertown.web.payload.request;

import lombok.Data;

@Data
public class CommentRequest {
    private String writer;
    private Long coggleNo;
    private String content;
}
