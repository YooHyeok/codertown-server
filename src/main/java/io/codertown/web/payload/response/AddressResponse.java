package io.codertown.web.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {
    private Long addressNo;
    private String addrName;
}
