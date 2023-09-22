package io.codertown.web.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatRoomUserDto {
    private ChatRoomDto chatRoom;
    private Boolean isRoomMaker;

}
