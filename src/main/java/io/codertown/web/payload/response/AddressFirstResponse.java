package io.codertown.web.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressFirstResponse {
    private Long addrFirstNo;
    private String addrName;
}
