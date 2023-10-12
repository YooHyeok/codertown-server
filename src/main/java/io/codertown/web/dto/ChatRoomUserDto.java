package io.codertown.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomUserDto {
    private UserDto userDto;
    private Boolean isConnectedRoom;
}
