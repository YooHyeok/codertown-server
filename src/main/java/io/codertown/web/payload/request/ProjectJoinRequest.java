package io.codertown.web.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectJoinRequest {

    private String requesterEmail;
    private Long projectPartNo;
    private Long partNo;
}
