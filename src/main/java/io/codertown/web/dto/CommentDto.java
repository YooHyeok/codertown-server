package io.codertown.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long coggleNo;
    private Long parentNo;
    private Long commentNo;
    private String writer;
    private String content;

}
