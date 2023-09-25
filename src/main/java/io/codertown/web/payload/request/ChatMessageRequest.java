package io.codertown.web.payload.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessageRequest {
    private String roomId;
    private String senderId;
    private String message;
}
