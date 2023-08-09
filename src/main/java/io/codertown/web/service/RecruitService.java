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

import java.util.ArrayList;
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
     * cascade = CascadeType.ALL 에 의해 <br/>
     * cokkiri가 영속화 되는 시점(flush되어 실제 저장되기직전)에 <br/>
     * Project, ProjectPart, Project의 List<projectParts>리스트에 <br/>
     * 값이 주입된 상태로 영속성 컨텍스트에 저장된다. <br/>
     * -> 조회시 재 주입된다. -> 영속성 컨텍스트에서 리스트에 값이 추가된 정보까지 관리하기때문. <br/>
     * @param request
     * @return Boolean
     */
    @Transactional
    public Boolean cokkiriSave(CokkiriSaveRequest request) {
        try {
            User findUser = (User) userRepository.findByEmail(request.getWriter());
            request.setUser(findUser);
            //코끼리 & 프로젝트 영속화 주입 (Cascade.All) - createCokkiri()
            Cokkiri cokkiri = Cokkiri.builder().build().createCokkiri(request);
            // 프로젝트 파트 영속화 주입 - 추후 양방향 연관관계에 의해서 저장될수 있도록 수정해야한다.
            List<ProjectPart> projectParts = new ArrayList<>();
            for (ProjectPartSaveDto projectPartSaveDto : request.getProjectParts()) {
                ProjectPart part = ProjectPart.builder().build()
                        .createProjectPart(
                                cokkiri.getProject() // 이곳에서 ProjectPart에 Project가 양방향으로 주입된다.
                                , projectPartSaveDto.getRecruitCount()
                                , partRepository.findById(projectPartSaveDto.getPartNo()).get()
                        );
                projectParts.add(part);
            }
            projectParts.forEach(projectPart -> cokkiri.getProject().getProjectParts().add(projectPart));
//            collect.forEach(projectPartRepository::save); //반복 저장
            Cokkiri savedCokkiri = recruitRepository.save(cokkiri); // 영속되어있는 Project, ProjectPart 함께 저장
            return savedCokkiri.getId()!=null ? true: false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 코끼리 & 프로젝트 수정 <br/>
     * 다중 수정은 변경감지에 의해 UPDATE 쿼리 호출 <br/>
     * 다중 삭제는 영속성 정의에 의해 DELETE 쿼리 호출
     * @param request
     * @return Boolean
     */
    @Transactional(readOnly = false)
    public Boolean cokkiriUpdate(CokkiriUpdateRequest request) {

        try {
            Optional<Recruit> oRecruit = recruitRepository.findById(request.getCokkiriUpdate().getRecruitNo());
            if (oRecruit.isPresent()) {
                // 코끼리&프로젝트 수정
                Cokkiri cokkiri = (Cokkiri)oRecruit.get();
                cokkiri.updateCokkiri(request.getCokkiriUpdate());
                // 프로젝트 파트 수정
                if (request.getProjectPartUpdate().getUpdate().size() > 0) {
                    // UPDATE 다중 수정
                    request.getProjectPartUpdate().getUpdate().forEach(projectPartUpdateDto -> {
                        ProjectPart projectPart = projectPartRepository.findById(projectPartUpdateDto.getProjectPartNo()).get();
                        projectPart.updateProjectPart(projectPartUpdateDto.getRecruitCount()); //Update - 변경 감지
                    });
                }
                if (request.getProjectPartUpdate().getDelete().size() > 0) {
                    // DELETE 다중 삭제
                    request.getProjectPartUpdate().getDelete().forEach(projectPartUpdateDto -> {
                        ProjectPart projectPart = projectPartRepository.findById(projectPartUpdateDto.getProjectPartNo()).get();
                        /**
                         * Delete - 영속성 정의
                         * 1. 부모 Project의 자식리스트 List<ProjectPart> projectParts 에서 자식요소 제거
                         * 2. orphanRemoval = true에 의해 부모 리스트로부터 삭제된 projectPart 고아객체를 제거한다
                         */
                        cokkiri.getProject().getProjectParts().remove(projectPart);
                    });
                }
                if (request.getProjectPartUpdate().getInsert().size() > 0) {
                    // INSERT 다중 추가
                    request.getProjectPartUpdate().getInsert().forEach(projectPartUpdateDto -> {
                    });
                }
            } else throw new RuntimeException("코끼리 게시글이 존재하지 않습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
                List<ProjectPartSaveDto> projectPartList = cokkiri.getProject().getProjectParts().stream()
                        .map(projectPart -> ProjectPartSaveDto.builder().build().entityToDto(projectPart))
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
                    List<ProjectPartSaveDto> projectPartList = cokkiri.getProject().getProjectParts().stream()
                            .map(projectPart -> ProjectPartSaveDto.builder().build().entityToDto(projectPart))
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
