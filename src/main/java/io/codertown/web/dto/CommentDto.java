package io.codertown.web.dto;

import io.codertown.web.entity.coggle.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
//    private String writer;
    private UserDto writer;

    private String content;
    private Boolean status;
    private Integer depth;
    private String mentionUser;
    private LocalDateTime firstRegDate;
    private List<CommentDto> children = new ArrayList<>();

    public CommentDto changeEntityToDto(Comment comment) {
        return CommentDto.builder()
                .coggleNo(comment.getCoggle().getCoggleNo())
                .parentNo(comment.getParent() == null ? 0 : comment.getParent().getId())
                .commentNo(comment.getId())
                .writer(UserDto.userEntityToDto(comment.getUser()))
                .content(comment.getContent().isEmpty() ? null : comment.getContent())
                .status(comment.getStatus())
                .depth(comment.getDepth())
                .mentionUser(comment.getMentionUser())
                .children(getChildrenMapToList(comment))
                .firstRegDate(comment.getFirstRegDate())
                .build();
    }

    /**
     * Comment 엔터티를 CommentDto 객체로 변환할 때 호출한다. <br/>
     * children List에서 Stream을 통해 한번 더 변환 매핑 처리한다.
     * @param comment
     * @return
     */
    private static List<CommentDto> getChildrenMapToList(Comment comment) {
        return comment.getChildren()
                .stream()
                .map(childrenComment -> CommentDto.builder().build()
                        .changeEntityToDto(childrenComment))
                .collect(Collectors.toList());
    }
}
