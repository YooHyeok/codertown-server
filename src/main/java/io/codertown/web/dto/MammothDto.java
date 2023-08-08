package io.codertown.web.dto;

import io.codertown.web.entity.recruit.Mammoth;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MammothDto {

    private String title;
    private String content;
    private String link;
    private UserDto writer; //추후 writer정보 변경가능

    private String location;

    public static MammothDto entityToDto(Mammoth mammoth, UserDto userDto) {
        return MammothDto.builder()
                .title(mammoth.getTitle()) // 코끼리 글 제목
                .link(mammoth.getLink())
                .content(mammoth.getContent()) // 코끼리 글 내용
                .writer(userDto) // 코끼리 글 작성자 (추후 String값으로 수정)
//                .writer(cokkiri.getRecruitUser().getEmail()) // 코끼리 글 작성자 (추후 닉네임으로 수정 가능)
                .location(mammoth.getLocation()) // 목표 기간(주)
                .build();
    }
}
