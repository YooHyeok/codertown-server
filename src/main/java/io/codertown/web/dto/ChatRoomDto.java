package io.codertown.web.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRoomDto {
    private String lastChatMessage;
    private LocalDateTime lastChatMessageDate;
    private List<UserDto> chatUserList;

    private Boolean isConfirm;
    
    
}
