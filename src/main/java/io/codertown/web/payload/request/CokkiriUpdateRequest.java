package io.codertown.web.payload.request;

import io.codertown.web.dto.CokkiriUpdateDto;
import lombok.Data;

@Data
public class CokkiriUpdateRequest {

    private CokkiriUpdateDto cokkiriUpdate;

    private ProjectPartUpdateRequest projectPartUpdate;

}
