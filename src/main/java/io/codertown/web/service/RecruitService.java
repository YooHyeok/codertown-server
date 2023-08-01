package io.codertown.web.service;

import io.codertown.web.dto.CokkiriDto;
import io.codertown.web.dto.ProjectDto;
import io.codertown.web.dto.ProjectPartDto;
import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.CokkiriDetailResponse;
import io.codertown.web.payload.CokkiriSaveRequest;
import io.codertown.web.repository.PartRepository;
import io.codertown.web.repository.RecruitRepository;
import io.codertown.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final ProjectPartRepository projectPartRepository;

    @Transactional(readOnly = false)
    public Boolean cokkiriSave(CokkiriSaveRequest request) {

        try {
            User findUser = (User) userRepository.findByEmail(request.getWriter());
            request.setUser(findUser);
            //코끼리 & 프로젝트 저장 (Cascade.All)
            Cokkiri cokkiri = Cokkiri.builder().build().createCokkiri(request);

            // 프로젝트 파트 저장
            List<ProjectPart> collect = request.getProjectParts().stream()
                    .map(projectPartDto -> ProjectPart.builder().build()
                            .createProjectPart(cokkiri.getProject()
                                    , projectPartDto.getRecruitCount()
                                    ,partRepository.findById(projectPartDto.getPartNo()).get())
                    )
                    .collect(Collectors.toList());
            collect.forEach(projectPartRepository::save);
            recruitRepository.save(cokkiri);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public CokkiriDetailResponse cokkiriDetail(Long recruitNo) throws RuntimeException {
        try {
            Optional<Recruit> oRecruit = recruitRepository.findById(recruitNo);
            if (oRecruit.isPresent()) {
                Cokkiri recruit = (Cokkiri)oRecruit.get();
                // 프로젝트별 파트 조회 정보
                List<ProjectPartDto> projectPartList = recruit.getProject().getProjectParts().stream()
                        .map(projectPart -> ProjectPartDto.builder()
                                .partNo(projectPart.getPart().getId()) // 파트 번호
                                .partName(projectPart.getPart().getPartName()) // 파트 이름
                                .recruitCount(projectPart.getRecruitCount()) // 모집 인원
                                .build())
                        .collect(Collectors.toList());
                // 프로젝트 조회 정보
                ProjectDto projectDto = ProjectDto.builder()
                        .subject(recruit.getProject().getSubject()) // 주제
                        .projectTitle(recruit.getProject().getProjectTitle()) // 프로젝트 제목
                        .teamName(recruit.getProject().getTeamName()) // 팀 이름
                        .projectStatus(recruit.getProject().getProjectStatus().name()) // 프로젝트 상태 (대기중)
                        .projectParts(projectPartList) // 프로젝트별 파트 목록
                        .build();
                // 코끼리 조회 정보
                CokkiriDto cokkiriDto = CokkiriDto.builder()
                        .title(recruit.getTitle()) // 코끼리 글 제목
                        .content(recruit.getContent()) // 코끼리 글 내용
//                        .recruiteUser(recruit.getRecruitUser()) // 코끼리 글 작성자 (추후 String값으로 수정)
                        .recruiteUser(recruit.getRecruitUser().getEmail()) // 코끼리 글 작성자 (추후 String값으로 수정)
                        .objectWeek(recruit.getObjectWeek()) // 목표 기간(주)
                        .build();
                CokkiriDetailResponse cokkiriDetailResponse = CokkiriDetailResponse.builder()
                        .projectDto(projectDto)
                        .cokkiriDto(cokkiriDto)
                        .build();
                System.out.println("cokkiriDetailResponse = " + cokkiriDetailResponse);
                return cokkiriDetailResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("게시글 없음");
    }
}
