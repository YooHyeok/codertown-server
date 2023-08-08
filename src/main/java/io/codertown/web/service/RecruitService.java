package io.codertown.web.service;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.*;
import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.entity.recruit.Mammoth;
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
                Cokkiri cokkiri = (Cokkiri)oRecruit.get();
                UserDto userDto = UserDto.userEntityToDto(cokkiri.getRecruitUser());
                // 프로젝트별 파트 조회 정보
                List<ProjectPartDto> projectPartList = cokkiri.getProject().getProjectParts().stream()
                        .map(projectPart -> ProjectPartDto.builder().build().entityToDto(projectPart))
                        .collect(Collectors.toList());
                // 프로젝트 조회 정보
                ProjectDto projectDto = ProjectDto.builder().build().entityToDto(cokkiri.getProject() ,projectPartList);
                // 코끼리 조회 정보
                CokkiriDto cokkiriDto = CokkiriDto.builder().build().entityToDto(cokkiri, userDto);
                return CokkiriDetailResponse.builder()
                        .projectDto(projectDto)
                        .cokkiriDto(cokkiriDto)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("게시글 없음");
    }

    /**
     * Recruit 목록 출력
     */
    public RecruitListResponse recruitList(Integer page, String dType) {
        page = page == null ? 1 : page;
        PageInfo pageInfo = PageInfo.builder().build().createPageRequest(page, "id", "DESC");
        try {
            Page<Recruit> pages = recruitRepository.findByType(dType, pageInfo.getPageRequest());
            pageInfo.setPageInfo(pages, pageInfo);
            List<RecruitListDto> recruitList = pages.getContent().stream().map(recruit -> {
                UserDto userDto = UserDto.userEntityToDto(recruit.getRecruitUser());
                RecruitListDto build;
                CokkiriDto cokkiriDto = null;
                MammothDto mammothDto = null;
                ProjectDto projectDto = null;
                if (recruit instanceof Cokkiri) {
                    Cokkiri cokkiri = (Cokkiri) recruit;
                    cokkiriDto = CokkiriDto.builder().build().entityToDto(cokkiri, userDto);
                    List<ProjectPartDto> projectPartList = cokkiri.getProject().getProjectParts().stream()
                            .map(projectPart -> ProjectPartDto.builder().build().entityToDto(projectPart))
                            .collect(Collectors.toList());
                    projectDto = ProjectDto.builder().build().entityToDto(cokkiri.getProject(), projectPartList);
                }
                if (recruit instanceof Mammoth) {
                    Mammoth mammoth = (Mammoth) recruit;
                    mammothDto = MammothDto.builder().build().entityToDto(mammoth, userDto);
                }
                return RecruitListDto.builder()
                        .mammothDto(mammothDto)
                        .cokkiriDto(cokkiriDto)
                        .projectDto(projectDto)
                        .build();
            }).collect(Collectors.toList());
            return RecruitListResponse.builder().recruitList(recruitList).pageInfo(pageInfo).build();
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
