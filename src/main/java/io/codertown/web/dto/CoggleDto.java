package io.codertown.web.dto;

import io.codertown.web.entity.coggle.Coggle;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CoggleDto {
    private Long coggleNo;
    private Character category;
    private String title;
    private String content;
    private Boolean status;
//    private String writer;
    private UserDto writer;
    private Long views;
    private LocalDateTime firstRegDate;
    private LocalDateTime lastModDate;

    /**
     * Coggle 엔터티 -> CoggleDto 변환 메소드 <br/>
     * @param findCoggle
     * @return
     */
    public CoggleDto changeEntityToDto(Coggle findCoggle) {
        return CoggleDto.builder()
                .coggleNo(findCoggle.getCoggleNo())
                .category(findCoggle.getCategory())
//                .writer(findCoggle.getUser().getEmail())
                .writer(UserDto.userEntityToDto(findCoggle.getUser()))
                .title(findCoggle.getTitle())
                .content(findCoggle.getContent())
                .status(findCoggle.getStatus())
                .views(findCoggle.getViews())
                .firstRegDate(findCoggle.getFirstRegDate())
                .lastModDate(findCoggle.getLastModDate())
                .build();
    }
}
