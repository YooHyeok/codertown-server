package io.codertown.web.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatRoomUserListDto {
    private ChatRoomDto chatRoom;
    private Boolean isRoomMaker;
    private Long newMsgCount;

}
