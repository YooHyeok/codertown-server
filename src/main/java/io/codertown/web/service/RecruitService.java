package io.codertown.web.service;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.*;
import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.entity.recruit.Mammoth;
import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.request.*;
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
            Cokkiri savedCokkiri = recruitRepository.save(cokkiri);
            return savedCokkiri.getId()!=null ? true: false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 코끼리 & 프로젝트 수정
     * @param request
     * @return Boolean
     */
    public Boolean cokkiriUpdate(CokkiriUpdateRequest request) {
        return null;
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
                RecruitDto cokkiriDto = RecruitDto.builder().build().cokkiriEntityToDto(cokkiri, userDto, null);
                return CokkiriDetailResponse.builder()
                        .projectDto(projectDto)
                        .cokkiriDto(cokkiriDto)
                        .build();
            }
            throw new RuntimeException("게시글 없음");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
                RecruitDto recruitDto = null;
                ProjectDto projectDto = null;
                if (recruit instanceof Cokkiri) {
                    Cokkiri cokkiri = (Cokkiri) recruit;
                    recruitDto = RecruitDto.builder().build().cokkiriEntityToDto(cokkiri, userDto, "cokkiri");
                    List<ProjectPartDto> projectPartList = cokkiri.getProject().getProjectParts().stream()
                            .map(projectPart -> ProjectPartDto.builder().build().entityToDto(projectPart))
                            .collect(Collectors.toList());
                    projectDto = ProjectDto.builder().build().entityToDto(cokkiri.getProject(), projectPartList);
                }
                if (recruit instanceof Mammoth) {
                    Mammoth mammoth = (Mammoth) recruit;
                    recruitDto = RecruitDto.builder().build().mammothEntityToDto(mammoth, userDto, "mammoth");
                }
                return RecruitListDto.builder()
                        .recruitDto(recruitDto)
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
    @Transactional(readOnly = false)
    public Boolean mammothSave(MammothSaveRequest request) {
        try {
            User findUser = (User) userRepository.findByEmail(request.getWriter());
            request.setUser(findUser);
            //코끼리 & 프로젝트 저장 (Cascade.All)
            Mammoth mammoth = Mammoth.builder().build().createMammoth(request);
            Mammoth savedMammoth = recruitRepository.save(mammoth);
            return savedMammoth.getId()!=null ? true: false; // 값 존재 여부로 성공 / 실패 Boolean 반환
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 맘모스 상세보기
     */
    public RecruitDto mammothDetail(Long recruitNo) {
        try{
            Optional<Recruit> oRecruit = recruitRepository.findById(recruitNo);
            if (oRecruit.isPresent()) {
                Mammoth mammoth = (Mammoth) oRecruit.get();
                UserDto userDto = UserDto.userEntityToDto(mammoth.getRecruitUser());
                // 코끼리 조회 정보
                return RecruitDto.builder().build().mammothEntityToDto(mammoth, userDto, "mammoth");
            }
            throw new RuntimeException("게시글 없음");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 맘모스 글 수정
     */
    @Transactional(readOnly = false)
    public Boolean mammothUpdate(MammothUpdateRequest request) {
        try {
            Optional<Recruit> oRecruit = recruitRepository.findById(request.getRecruitNo().longValue());
            if (oRecruit.isPresent()) {
                Mammoth findMammoth = (Mammoth) oRecruit.get();
                try { //2차 Try문 - 수정로직
                    findMammoth.updateMammoth(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("맘모스 수정 실패"); //Controller에서 Catch
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("현재 맘모스를 찾을수 없습니다."); //Controller에서 Catch
        }
        return true; //2차 Try까지 무사히 통과하면 true반환
    }
}
