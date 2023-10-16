package io.codertown.web.entity.coggle;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.request.CommentUpdateRequset;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(callSuper = true)
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

    @ColumnDefault("false")
    private Boolean status;//댓글 상태 False:정상 | True:삭제

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_no", referencedColumnName = "COMMENT_NO")
    private Comment parent; //상위 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_no")
    private User writer; //댓글 작성자
//    private User user; //댓글 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mention_no")
    private User mention;

    @Column
    private Integer depth; //깊이 1,2,3

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coggle_no")
    private Coggle coggle; //게시글 번호

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public void updateComment(CommentUpdateRequset request) {
        this.content = request.getContent();
    }

    public void deleteComment(Boolean status) {
        this.status = status;
    }
}
