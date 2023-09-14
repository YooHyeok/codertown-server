package io.codertown.web.dto;

import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.entity.recruit.Mammoth;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RecruitDto {
    private Long recruitNo;
    private String category;
    private String title;
    private String content;
    private String link;
    private Long views;
//    private String writer;
    private UserDto writer; //추후 writer정보 변경가능
    private String location;
//    private Integer objectWeek;
    private Integer likedCount;
    private Boolean isLiked;
    private LocalDateTime firstRegDate;
    private LocalDateTime lastModDate;

    public static RecruitDto cokkiriEntityToDto(Cokkiri cokkiri, UserDto userDto, String dType, Boolean isLiked) {
        return RecruitDto.builder()
                .recruitNo(cokkiri.getId())
                .category(dType)
                .title(cokkiri.getTitle()) // 코끼리 글 제목
                .link(cokkiri.getLink())
                .content(cokkiri.getContent()) // 코끼리 글 내용
                .views(cokkiri.getViews())
                .isLiked(isLiked)
                .likedCount(cokkiri.getBookMark().size())
                .firstRegDate(cokkiri.getFirstRegDate())
                .lastModDate(cokkiri.getLastModDate())
                .writer(userDto) // 코끼리 글 작성자 (추후 String값으로 수정)
//                .objectWeek(cokkiri.getObjectWeek()) // 목표 기간(주)
                .build();
    }

    public static RecruitDto mammothEntityToDto(Mammoth mammoth, UserDto userDto, String dType, Boolean isLiked) {
        return RecruitDto.builder()
                .recruitNo(mammoth.getId())
                .category(dType)
                .title(mammoth.getTitle()) // 맘모스 글 제목
                .link(mammoth.getLink())
                .content(mammoth.getContent()) // 맘모스 글 내용
                .views(mammoth.getViews())
                .firstRegDate(mammoth.getFirstRegDate())
                .lastModDate(mammoth.getLastModDate())
                .writer(userDto) // 맘모스 글 작성자 (추후 String값으로 수정)
                .location(mammoth.getLocation())
                .isLiked(isLiked)
                .likedCount(mammoth.getBookMark().size())
                .build();
    }

}
