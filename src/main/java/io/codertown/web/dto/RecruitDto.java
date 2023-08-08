package io.codertown.web.dto;

import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.entity.recruit.Mammoth;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruitDto {
    private String category;
    private String title;
    private String content;
    private String link;
//    private String writer;
    private UserDto writer; //추후 writer정보 변경가능
    private String location;
    private Integer objectWeek;

    public static RecruitDto cokkiriEntityToDto(Cokkiri cokkiri, UserDto userDto, String dType) {
        return RecruitDto.builder()
                .category(dType)
                .title(cokkiri.getTitle()) // 코끼리 글 제목
                .link(cokkiri.getLink())
                .content(cokkiri.getContent()) // 코끼리 글 내용
                .writer(userDto) // 코끼리 글 작성자 (추후 String값으로 수정)
                .objectWeek(cokkiri.getObjectWeek()) // 목표 기간(주)
                .build();
    }

    public static RecruitDto mammothEntityToDto(Mammoth cokkiri, UserDto userDto, String dType) {
        return RecruitDto.builder()
                .category(dType)
                .title(cokkiri.getTitle()) // 코끼리 글 제목
                .link(cokkiri.getLink())
                .content(cokkiri.getContent()) // 코끼리 글 내용
                .writer(userDto) // 코끼리 글 작성자 (추후 String값으로 수정)
                .location(cokkiri.getLocation())
                .build();
    }

}
