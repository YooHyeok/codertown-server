package io.codertown.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessageDto {

    private UserDto sender;

    private String message;

    private LocalDateTime chatSendDate;
}
