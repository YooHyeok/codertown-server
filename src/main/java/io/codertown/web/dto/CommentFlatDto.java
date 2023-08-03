package io.codertown.web.dto;

import io.codertown.web.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentFlatDto {

    private Long coggleNo;
    private Long parentNo;
    private Long commentNo;
    private String writer;
    private String content;
    private Boolean status;

    public CommentFlatDto changeEntityToDto(Comment comment) {
        return CommentFlatDto.builder()
                .coggleNo(comment.getCoggle().getCoggleNo())
                .parentNo(comment.getParent() == null ? 0 : comment.getParent().getId())
                .commentNo(comment.getId())
                .writer(comment.getUser().getEmail())
                .content(comment.getContent().isEmpty() ? null : comment.getContent())
                .status(comment.getStatus())
                .build();
    }
}
