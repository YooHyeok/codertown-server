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
    private UserDto writer;
    private Boolean isLikeMarked;
    private Integer isLikedMarkedCount;
    private Long views;
    private LocalDateTime firstRegDate;
    private LocalDateTime lastModDate;

    /**
     * Coggle 엔터티 -> CoggleDto 변환 메소드 <br/>
     * @param findCoggle
     * @return
     */
    public CoggleDto changeEntityToDto(Coggle findCoggle, Boolean isLikeMarked) {
        return CoggleDto.builder()
                .coggleNo(findCoggle.getId())
                .category(findCoggle.getCategory())
//                .writer(findCoggle.getUser().getEmail())
                .writer(UserDto.userEntityToDto(findCoggle.getWriter()))
                .title(findCoggle.getTitle())
                .content(findCoggle.getContent())
                .status(findCoggle.getStatus())
                .isLikeMarked(isLikeMarked)
                .isLikedMarkedCount(findCoggle.getLikeMarkList().size())
                .views(findCoggle.getViews())
                .firstRegDate(findCoggle.getFirstRegDate())
                .lastModDate(findCoggle.getLastModDate())
                .build();
    }
}
