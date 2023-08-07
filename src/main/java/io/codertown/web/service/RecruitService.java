package io.codertown.web.service;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.*;
import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.request.CokkiriSaveRequest;
import io.codertown.web.payload.request.ProjectJoinRequest;
import io.codertown.web.payload.response.CokkiriDetailResponse;
import io.codertown.web.payload.response.RecruitListResponse;
import io.codertown.web.repository.PartRepository;
import io.codertown.web.repository.ProjectPartRepository;
import io.codertown.web.repository.RecruitRepository;
import io.codertown.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    /**
     * 코끼리 & 프로젝트 저장
     * @param request
     * @return Boolean
     */
    @Transactional
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

    /**
     * 코끼리 & 프로젝트 상세보기
     * @param recruitNo
     * @return
     */
    public CokkiriDetailResponse cokkiriDetail(Long recruitNo) throws RuntimeException {
        try {
            Optional<Recruit> oRecruit = recruitRepository.findById(recruitNo);
            if (oRecruit.isPresent()) {
                Cokkiri recruit = (Cokkiri)oRecruit.get();
                // 프로젝트별 파트 조회 정보
                List<ProjectPartDto> projectPartList = recruit.getProject().getProjectParts().stream()
                        .map(projectPart -> ProjectPartDto.builder().build().entityToDto(projectPart))
                        .collect(Collectors.toList());
                // 프로젝트 조회 정보
                ProjectDto projectDto = ProjectDto.builder().build().entityToDto(recruit.getProject() ,projectPartList);
                // 코끼리 조회 정보
                CokkiriDto cokkiriDto = CokkiriDto.builder().build().entityToDto(recruit);
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

    /**
     * Recruit 목록 출력
     */
    public List<RecruitListResponse> recruitList(Integer page) {
        page = page == null ? 1 : page;
        PageInfo pageInfo = PageInfo.builder().build().createPageRequest(page, "id", "DESC");
        try {
//            Page<Recruit> pages = recruitRepository.findByCategory(pageInfo.getPageRequest());
            Page<Recruit> pages = recruitRepository.findByType("Cokkiri", pageInfo.getPageRequest());
            pageInfo.setPageInfo(pages, pageInfo);

            return pages.getContent().stream().map(recruit -> {
                Cokkiri cokkiri = (Cokkiri) recruit;
                UserDto userDto = UserDto.userEntityToDto(cokkiri.getRecruitUser());
                RecruitDto recruitDto = RecruitDto.builder()
                        .title(cokkiri.getTitle())
                        .link(cokkiri.getLink())
                        .content(cokkiri.getContent())
                        .build();
                CokkiriDto cokkiriDto = CokkiriDto.builder().build().entityToDto(cokkiri);
                List<ProjectPartDto> projectPartList = cokkiri.getProject().getProjectParts().stream()
                        .map(projectPart -> ProjectPartDto.builder().build().entityToDto(projectPart))
                        .collect(Collectors.toList());
                ProjectDto projectDto = ProjectDto.builder().build().entityToDto(cokkiri.getProject() ,projectPartList);
                return RecruitListResponse.builder()
                        .recruitDto(recruitDto)
                        .cokkiriDto(cokkiriDto)
                        .projectDto(projectDto)
                        .pageInfo(pageInfo)
                        .build();
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 프로젝트 참가 요청
     * @param request
     */
    public void projectJoinRequest(ProjectJoinRequest request) {
    }

    /**
     * 맘모스 저장
     */


    /**
     * 맘모스 상세보기
     */

    /**
     * 맘모스 글 수정
     */
}
