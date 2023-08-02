package io.codertown.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data


@AllArgsConstructor
public class CommentChildrenQueryDto {
    private Long coggleNo;
    private Long parentNo;
    private Long commentNo;
    private String writer;
    private String content;

}
