package io.codertown.web.payload;

import io.codertown.web.dto.ProjectPartDto;
import io.codertown.web.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CokkiriSaveRequest {

    private String writer;
    private User user;
    private String cokkiriTitle; // 코글 제목
    private String projectSubject; // 프로젝트 주제
    private String projectTitle; // 프로젝트 제목
    private String teamname; // 프로젝트 팀명
    private Integer objectWeek; // 목표 소요 기간(주)
    private List<ProjectPartDto> projectParts = new ArrayList<>(); // 파트
    private String content;
}
