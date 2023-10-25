package io.codertown.web.payload.response;

import io.codertown.web.dto.NotificationDto;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class NotificationResponse {
    private List<NotificationDto> notificationDtoList = new ArrayList<>();

}
