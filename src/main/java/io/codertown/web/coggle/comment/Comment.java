package io.codertown.web.coggle.comment;

import io.codertown.web.coggle.Coggle;
import io.codertown.web.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
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
    @JoinColumn(name = "commentor_no")
    private User user; //댓글 작성자

    @ManyToOne
    @JoinColumn(name = "coggle_no")
    private Coggle coggle; //게시글 번호

}
