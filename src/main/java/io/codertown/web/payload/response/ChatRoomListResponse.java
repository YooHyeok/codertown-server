package io.codertown.web.payload.response;

import io.codertown.web.dto.ChatRoomUserListDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class ChatRoomListResponse {
    private List<ChatRoomUserListDto> chatRomUserDtoList;
}
