package io.codertown.web.payload.response;

import io.codertown.web.dto.ChatRoomUserDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class ChatRoomListResponse {
    private List<ChatRoomUserDto> chatRomUserDtoList;
}
