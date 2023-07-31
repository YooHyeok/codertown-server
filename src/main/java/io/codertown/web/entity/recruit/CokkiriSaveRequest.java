package io.codertown.web.entity.recruit;

import lombok.Data;

import java.util.List;

@Data
public class CokkiriSaveRequest {
    private String coggleTitle; // 코글 제목
    private String projectSubject; // 프로젝트 주제
    private String projectTitle; // 프로젝트 제목
    private String teamname; // 프로젝트 팀명
    private Integer objectWeek; // 목표 소요 기간(주)
    private List<Object> partList; // 파트
    private String content;
}
