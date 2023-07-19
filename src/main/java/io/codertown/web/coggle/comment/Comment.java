package io.codertown.web.coggle.reply;

import io.codertown.web.coggle.Coggle;
import io.codertown.web.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Comment {
    @Id @GeneratedValue
    private Long commentNo;
    private String content;
    private LocalDateTime createDate; //작성일자

    //상위 작성자
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    //댓글 작성자
    @ManyToOne
    @JoinColumn(name = "comenter")
    private User user; //댓글 작성자

    @ManyToOne
    @JoinColumn(name = "coggle_no")
    private Coggle coggle; //게시글 번호

}
