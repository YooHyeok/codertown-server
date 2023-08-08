package io.codertown.web.dto;

import io.codertown.web.entity.recruit.Cokkiri;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CokkiriDto {
    private String title;
    private String content;
    private String link;
//    private String writer;
    private UserDto writer; //추후 writer정보 변경가능

    private Integer objectWeek;

    public static CokkiriDto entityToDto(Cokkiri cokkiri, UserDto userDto) {
        return CokkiriDto.builder()
                .title(cokkiri.getTitle()) // 코끼리 글 제목
                .link(cokkiri.getLink())
                .content(cokkiri.getContent()) // 코끼리 글 내용
                .writer(userDto) // 코끼리 글 작성자 (추후 String값으로 수정)
//                .writer(cokkiri.getRecruitUser().getEmail()) // 코끼리 글 작성자 (추후 닉네임으로 수정 가능)
                .objectWeek(cokkiri.getObjectWeek()) // 목표 기간(주)
                .build();
    }
}
