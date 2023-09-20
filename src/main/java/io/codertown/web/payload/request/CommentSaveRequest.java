package io.codertown.web.payload.request;

import lombok.Data;

@Data
public class CommentSaveRequest {
    private String writer;
    private Long coggleNo;
    private Long parentNo;
    private String content;
    private Integer depth;
    private String mentionUser;
}
