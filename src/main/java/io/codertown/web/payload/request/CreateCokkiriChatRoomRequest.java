package io.codertown.web.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCokkiriChatRoomRequest {
    private String loginId;
    private String writerId;
    private Long projectNo;
    private Long projectPartNo;

}
