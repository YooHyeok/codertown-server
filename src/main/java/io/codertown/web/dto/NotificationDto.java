package io.codertown.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NotificationDto {
    private Long notificationNo; //NOTIFICATION PK번호
    private String replyCondition; //NOTIFICATION구분값

    private Long commentNo; //댓글 번호
    private String writerNickname; //댓글 작성자 닉네임
    private byte[] profileUrl; //댓글 작성자 프로필URL
    private String mentionNickname; //멘션 대상자 닉네임
    private String  commentContent; //댓글 내용

    private LocalDateTime firstRegDate; //댓글 작성 일자
    private Long coggleNo; //코글 번호
    private String coggleTitle; //코글 글 제목
    private Boolean isClicked; //조회여부

}
