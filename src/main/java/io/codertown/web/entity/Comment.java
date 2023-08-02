package io.codertown.web.entity;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private LocalDateTime createDate; //작성일자

    //상위 작성자
    @ManyToOne
    @JoinColumn(name = "parent_no")
    private Comment parent;

    //댓글 작성자
    @ManyToOne
    @JoinColumn(name = "writer_no")
    private User user; //댓글 작성자

    @ManyToOne
    @JoinColumn(name = "coggle_no")
    private Coggle coggle; //게시글 번호

}
