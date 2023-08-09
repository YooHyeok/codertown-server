package io.codertown.web.dto;

import lombok.Data;

@Data
public class CokkiriUpdateDto {

    private Long recruitNo;
    private String cokkiriTitle; // 코글 제목
    private String projectSubject; // 프로젝트 주제
    private String projectTitle; // 프로젝트 제목
    private String teamname; // 프로젝트 팀명
    private Integer objectWeek; // 목표 소요 기간(주)
    private String link;
    private String content;
}
