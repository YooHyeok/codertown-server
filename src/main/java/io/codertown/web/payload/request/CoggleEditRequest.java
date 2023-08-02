package io.codertown.web.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoggleEditRequest {

    private Character category;
    private String title;
    private String content;
    private Long coggleNo;
}
