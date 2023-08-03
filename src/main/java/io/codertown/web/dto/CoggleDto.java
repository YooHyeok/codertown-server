package io.codertown.web.dto;

import io.codertown.web.entity.Coggle;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoggleDto {
    private Long coggleNo;
    private Character category;
    private String title;
    private String content;
    private Boolean status;

    private String writer;

    public CoggleDto changeCoggleDto(Coggle findCoggle) {
        return CoggleDto.builder()
                .coggleNo(findCoggle.getCoggleNo())
                .category(findCoggle.getCategory())
                .writer(findCoggle.getUser().getEmail())
                .title(findCoggle.getTitle())
                .content(findCoggle.getContent())
                .status(findCoggle.getStatus())
                .build();
    }
}
