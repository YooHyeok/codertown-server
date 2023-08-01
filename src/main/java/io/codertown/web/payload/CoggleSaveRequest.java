package io.codertown.web.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoggleSaveRequest {
    private Character category;
    private String title;
    private String content;
    private String writer;
}
