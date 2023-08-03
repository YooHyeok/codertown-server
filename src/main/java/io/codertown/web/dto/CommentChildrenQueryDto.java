package io.codertown.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentChildrenQueryDto {
    private Long coggleNo;
    private Long parentNo;
    private Long commentNo;
    private String writer;
    private String content;
    private Boolean status;


    public CommentChildrenQueryDto mappingByBuilder(CommentFlatDto commentFlatDto) {
        return CommentChildrenQueryDto.builder()
                .coggleNo(commentFlatDto.getCoggleNo())
                .parentNo(commentFlatDto.getParentNo())
                .commentNo(commentFlatDto.getCommentNo())
                .writer(commentFlatDto.getWriter())
                .content(commentFlatDto.getContent())
                .status(commentFlatDto.getStatus())
                .build();
    }
}
