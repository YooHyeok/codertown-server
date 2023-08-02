package io.codertown.web.entity;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.entity.user.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(callSuper = true, exclude = "parent")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Comment extends BaseTimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_NO")
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    //상위 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_no", referencedColumnName = "COMMENT_NO")
    private Comment parent;

    //댓글 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_no")
    private User user; //댓글 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coggle_no")
    private Coggle coggle; //게시글 번호

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public void updateComment(CommentEditRequset request) {
        this.content = request.getContent();
    }
}
