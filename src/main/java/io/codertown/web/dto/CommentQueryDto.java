package io.codertown.web.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "parentNo")
public class CommentQueryDto {
    private Long coggleNo;
    private Long parentNo;
    private Long commentNo;
    private String writer;
    private String content;
    private List<CommentChildrenQueryDto> children = new ArrayList<>();

}
