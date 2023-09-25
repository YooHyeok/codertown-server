package io.codertown.web.payload;

import io.codertown.web.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageSaveResult {

    UserDto sender;
    String message;
    LocalDateTime chatSendDate;
}
