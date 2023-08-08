package io.codertown.web.payload.request;

import lombok.Data;

@Data
public class CommentUpdateRequset {
    private Long commentNo;
    private String content;

    private Character status;
}