package io.codertown.web.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRoomDetailResponse {
    private List<ChatMessageDto> chatMessageDtoList;
    private ProjectDto project;
    private ProjectPartDto projectPart;
    private Boolean isConfirm;

}