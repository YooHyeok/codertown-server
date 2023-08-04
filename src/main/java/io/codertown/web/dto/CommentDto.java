package io.codertown.web.dto;

import io.codertown.web.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private Boolean status;

    private List<CommentDto> children = new ArrayList<>();

    public CommentDto changeEntityToDto(Comment comment) {
        return CommentDto.builder()
                .coggleNo(comment.getCoggle().getCoggleNo())
                .parentNo(comment.getParent() == null ? 0 : comment.getParent().getId())
                .commentNo(comment.getId())
                .writer(comment.getUser().getEmail())
                .content(comment.getContent().isEmpty() ? null : comment.getContent())
                .status(comment.getStatus())
                .children(comment.getChildren().stream()
                        .map(childrenComment -> CommentDto.builder().build()
                                .changeEntityToDto(childrenComment))
                        .collect(Collectors.toList()))
                .build();
    }
}
