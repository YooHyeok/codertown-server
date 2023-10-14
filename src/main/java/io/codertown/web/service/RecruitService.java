package io.codertown.web.service;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.*;
import io.codertown.web.entity.Part;
import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.UserProject;
import io.codertown.web.entity.chat.ChatRoomUser;
import io.codertown.web.entity.project.Project;
import io.codertown.web.entity.recruit.BookMark;
import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.entity.recruit.Mammoth;
import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.request.*;
import io.codertown.web.payload.response.CokkiriDetailResponse;
import io.codertown.web.payload.response.RecruitListResponse;
import io.codertown.web.repository.*;
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
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final ProjectPartRepository projectPartRepository;
    private final BookMarkRepository bookMarkRepository;
    private final UserProjectRepository userProjectRepository;

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
            List<ProjectPart> projectParts = request.getProjectParts().stream().map(
                    projectPartDto -> ProjectPart.builder().build()
                    .createProjectPart(
                            cokkiri.getProject() // 이곳에서 ProjectPart에 Project가 양방향으로 주입된다.
                            , projectPartDto.getRecruitCount()
                            , partRepository.findById(projectPartDto.getPartNo()).get()
                    )).collect(Collectors.toList());
            /* 프로젝트 파트에 팀장추가 */
            ProjectPart projectPartLeader = ProjectPart.builder().build().createProjectPart(cokkiri.getProject(), 1, partRepository.findById(1L).get());
            /**
             * UserProject 엔티티에 [작성자]->[팀장] 으로 저장 
             * 1. addProjectPart() 호출
             *  -> 1. ProjetPart의 userProjects 양방향 - cascade에 의해 현재 UserProject가 ProjetPart에 Insert된다. (userProject 영속화 시)
             *  -> 2. ProjectPart 지원자수 1 증가
             * 2. Projet의 userProjects 양방향 - cascade에 의해 현재 UserProject가 Project에 Insert된다. (userProject 영속화 시)
             * 3. userProject생성및 반환
             */
            UserProject.builder().build().createUserProject(findUser, projectPartLeader);
            /* 팀장으로 추가된 프로젝트파트 프로젝트파트 리스트에 추가 */
            projectParts.add(projectPartLeader);
            /**
             * 프로젝트파트 리스트 루프
             * 프로젝트 엔터티의 프로젝트파트 리스트에 프로젝트파트를 추가한다.
             * casecade에 의해 -> 다수의 프로젝트파트가 insert 된다
             */
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
     * 다중 수정은 ProjectPart 변경감지에 의해 UPDATE 동작 <br/>
     * 다중 삭제는 Project엔티티의 orphanRemoval(List remove)에 의해 DELETE 동작 <br/>
     * 다중 추가는 Cokkiri 변경감지(List add)에 의해 영속화된 엔티티 INSERT 동작
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
                cokkiri.updateCokkiri(request.getCokkiriUpdate()); //Update - 변경 감지
                // 프로젝트 파트 수정
                /*if (request.getProjectPartUpdate().getUpdate().size() > 0) {
                    // UPDATE 다중 수정
                    request.getProjectPartUpdate().getUpdate().forEach(projectPartUpdateDto -> {
                        ProjectPart projectPart = projectPartRepository.findById(projectPartUpdateDto.getProjectPartNo()).get();
                        projectPart.updateProjectPart(projectPartUpdateDto.getRecruitCount()); //Update - 변경 감지
                    });
                }*/
                if (request.getProjectPartUpdate().getDelete().size() > 0) {
                    // DELETE 다중 삭제
                    request.getProjectPartUpdate().getDelete().forEach(projectPartUpdateDto -> {
                        ProjectPart projectPart = projectPartRepository.findById(projectPartUpdateDto.getProjectPartNo()).get();
                        /**
                         * Delete - 영속성 정의
                         * 1. 부모 Project의 자식리스트 List<ProjectPart> projectParts 에서 자식요소 제거
                         * 2. orphanRemoval = true에 의해 부모 리스트로부터 삭제된 projectPart 고아객체를 제거한다
                         */
                        cokkiri.getProject().getProjectParts().remove(projectPart); //orphanRemoval = true에 의해 고아객체 제거
                    });
                }
                if (request.getProjectPartUpdate().getInsert().size() > 0) {
                    // INSERT 다중 추가
                    request.getProjectPartUpdate().getInsert().forEach(projectPartUpdateDto -> {
                        Part part = partRepository.findById(projectPartUpdateDto.getPartNo()).get();
                        ProjectPart projectPart = ProjectPart.builder().build().createProjectPart(cokkiri.getProject(), projectPartUpdateDto.getRecruitCount(), part);
                        cokkiri.getProject().addProjectParts(projectPart); // Cokkiri 변경감지에 의해 영속화 되어있는 projectPart의 INSERT 호출
                    });
                }
            } else throw new RuntimeException("코끼리 게시글이 존재하지 않습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true; //Try 모든 로직 성공시 true값을 Controller에 반환한다.
    }
    
    /**
     * 코끼리 & 프로젝트 상세보기
     * @param recruitNo
     * @return
     */
    @Transactional(readOnly = false)
    public CokkiriDetailResponse cokkiriDetail(Long recruitNo, String loginId) throws RuntimeException {
        try {
            Optional<Recruit> oRecruit = recruitRepository.findById(recruitNo);
            if (oRecruit.isPresent()) {
                Cokkiri cokkiri = (Cokkiri)oRecruit.get();
                cokkiri.incrementViews(); //조회수 증가
                UserDto userDto = UserDto.userEntityToDto(cokkiri.getRecruitUser());
                /* 로그인하지 않았다면 좋아요여부 false  */
                boolean isBookmarked =  loginId.equals("null") ?  false : cokkiri.getBookMarkList()
                        .stream().anyMatch(bookMark ->  bookMark.getUser().getEmail().equals(loginId));
                // 프로젝트별 파트 조회 정보
                List<ProjectPartDto> projectPartList = cokkiri.getProject().getProjectParts().stream()
                        .filter(projectPart -> projectPart.getPart().getId() != 1L)
                        .map(projectPart -> ProjectPartDto.builder().build().entityToDto(projectPart))
                        .collect(Collectors.toList());
                // 프로젝트 조회 정보
                ProjectDto projectDto = ProjectDto.builder().build().entityToDto(cokkiri.getProject() ,projectPartList);
                /* 채팅방 존재 여부 */
                /*Boolean isChatMaden =
                        loginId.equals("null") ?  false : cokkiri.getProject().getChatRoom() == null ? false : cokkiri.getProject().getChatRoom().getChatRoomUserList()
                        .stream().anyMatch(user -> user.getChatRoomUser().getEmail().equals(loginId)) ? true : false;*/

                Boolean isChatMaden =
                        loginId.equals("null") ?  false : cokkiri.getProject().getChatRoomList().isEmpty() ? false
                                :
                                cokkiri.getProject().getChatRoomList().stream()
                                        .anyMatch(chatRoom -> chatRoom.getChatRoomUserList().stream()
                                                .anyMatch(chatRoomUser -> chatRoomUser.getChatRoomUser().getEmail().equals(loginId))) ? true:false;

                /* 로그인 한 회원이 참여 요청한 프로젝트파트 번호 (확장성 고려 - Project One To Many ChatRoom )*/
                /*List<List<Long>> collect1 = cokkiri.getProject().getChatRoomList().stream()
                        .filter(chatRoom -> chatRoom.getProject().getId() == cokkiri.getProject().getId())
                        .map(chatRoom -> {
                            List<ChatRoom> collect = chatRoom.getChatRoomUserList().stream()
                                    .filter(chatRoomUser -> chatRoomUser.getChatRoomUser().getEmail().equals(loginId))
                                    .map(chatRoomUser -> chatRoomUser.getChatRoom()).collect(Collectors.toList());
                            return collect.stream().map(chatRoom1 -> chatRoom1.getProjectPart().getId()).collect(Collectors.toList());
                        }).collect(Collectors.toList());*/

                /* (flatMap에대해서 공부할것...) */
                List<Long> takedProjectPartNos = cokkiri.getProject().getChatRoomList().stream()
                        .filter(chatRoom -> chatRoom.getProject().getId() == cokkiri.getProject().getId()) // 프로젝트 번호 일치 필터링
                        .filter(chatRoom -> chatRoom.getChatRoomUserList().stream()
                                .anyMatch(chatRoomUser -> chatRoomUser.getChatRoomUser().getEmail().equals(loginId))
                        ) // 참여 중인 회원 필터링
                        .flatMap(chatRoom -> chatRoom.getChatRoomUserList().stream()
                                .filter(chatRoomUser -> chatRoomUser.getChatRoomUser().getEmail().equals(loginId))
                                .map(ChatRoomUser::getChatRoom)) // 참여 중인 회원의 채팅 룸 추출
                        .map(chatRoom -> chatRoom.getProjectPart().getId()) // ProjectPart의 ID를 추출
                        .collect(Collectors.toList());

                // 코끼리 조회 정보
//                Optional<BookMark> like = bookMarkRepository.findByUserAndRecruit(cokkiri.getRecruitUser(), cokkiri);
                RecruitDto cokkiriDto = RecruitDto.builder().build().cokkiriEntityToDto(cokkiri, userDto, null, isBookmarked);

                return CokkiriDetailResponse.builder()
                        .projectDto(projectDto)
                        .cokkiriDto(cokkiriDto)
//                        .isChatMaden(isChatMaden)
                        .takedProjectPartNos(takedProjectPartNos)
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
    public RecruitListResponse recruitList(Integer page, Integer size, String dType, String keyword, String loginId, String url) {
        page = page == null ? 1 : page;
        PageInfo pageInfo = PageInfo.builder().build().createPageRequest(page, size, "id", "DESC");
        try {
            Page<Recruit> pages = recruitRepository.findByType(dType, pageInfo.getPageRequest(), keyword, loginId, url);
            Long totalCount = pages.getTotalElements();
            pageInfo.setPageInfo(pages, pageInfo);
            List<RecruitListDto> recruitList = pages.getContent().stream().map(recruit -> {
                UserDto userDto = UserDto.userEntityToDto(recruit.getRecruitUser());
                RecruitDto recruitDto = null;
                ProjectDto projectDto = null;

                /* 회원별 게시글별 북마크 유무 */
                boolean isBookmarked = recruit.getBookMarkList()
                        .stream().anyMatch(bookMark ->  bookMark.getUser().getEmail().equals(loginId));

                /* 코끼리 변환 (조회한 Recruit리스트중 현재 요소가 Cokkiri인경우) */
                if (recruit instanceof Cokkiri) {
                    Cokkiri cokkiri = (Cokkiri) recruit;
                    recruitDto = RecruitDto.builder().build().cokkiriEntityToDto(cokkiri, userDto, "cokkiri", isBookmarked
                    );
                    List<ProjectPartDto> projectPartList = cokkiri.getProject().getProjectParts().stream()
                            .map(projectPart -> ProjectPartDto.builder().build().entityToDto(projectPart))
                            .collect(Collectors.toList());
                    projectDto = ProjectDto.builder().build().entityToDto(cokkiri.getProject(), projectPartList);
                }

                /* 맘모스 변환 (조회한 Recruit리스트중 현재 요소가 Mammoth인경우) */
                if (recruit instanceof Mammoth) {
                    Mammoth mammoth = (Mammoth) recruit;
                    recruitDto = RecruitDto.builder().build().mammothEntityToDto(mammoth, userDto, "mammoth", isBookmarked);
                }

                return RecruitListDto.builder()
                        .recruitDto(recruitDto)
                        .projectDto(projectDto)
                        .build();
            }).collect(Collectors.toList());

            /* 마이페이지 북마크일경우 */
            if (url != null && url.equals("myBookMark")) {
                recruitList = recruitList.stream().filter(recruitListDto -> {
                    return recruitListDto.getRecruitDto().getIsBookmarked() == true;
                }).collect(Collectors.toList());
                totalCount = Long.valueOf(recruitList.size());
            }

            return RecruitListResponse.builder().recruitList(recruitList).pageInfo(pageInfo).articleCount(totalCount).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 코끼리&맘모스 북마크 Toggle
     * @param recruitNo
     * @param userId
     * @return
     */
    @Transactional(readOnly = false)
    public Boolean bookMarkToggle(Long recruitNo, String userId) throws Exception {
        User user = (User) userRepository.findByEmail(userId);
        try {
            Optional<Recruit> oRecruit = recruitRepository.findById(recruitNo);
            if (oRecruit.isPresent()) {
                Recruit recruit = oRecruit.get();
                Optional<BookMark> bookMark = bookMarkRepository.findByUserAndRecruit(user, recruit);
                BookMark recruitLikeMark = BookMark.builder().build().createRecruitBookMark(user, recruit);
                if (bookMark.isEmpty()) { // 존재하지 않는다면 추가
                    bookMarkRepository.save(recruitLikeMark);
                    return bookMark.isEmpty(); // 추가됨
                }
                bookMarkRepository.delete(bookMark.get()); // 존재한다면 제거
                return bookMark.isEmpty(); // 제거됨
            }
        throw new RuntimeException("게시글 없음");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 프로젝트 참여 수락
     * @param request
     */
    @Transactional(readOnly = false)
    public void projectJoinConfirm(ProjectJoinRequest request) {
        User findUser = (User) userRepository.findByEmail(request.getRequesterEmail());
        ProjectPart findProjectPart = projectPartRepository.findById(request.getProjectPartNo()).orElseThrow();

        /**
         * UserProject 엔티티에 추가
         * 1. addProjectPart() 호출
         *  -> 1. ProjetPart의 userProjects 양방향 - cascade에 의해 현재 UserProject가 ProjetPart에 Insert된다. (userProject 영속화 시)
         *  -> 2. ProjectPart 지원자수 1 증가
         * 2. Projet의 userProjects 양방향 - cascade에 의해 현재 UserProject가 Project에 Insert된다. (userProject 영속화 시)
         * 3. userProject생성및 반환
         */
        UserProject userProject = UserProject.builder().build().createUserProject(findUser, findProjectPart);
        userProjectRepository.save(userProject);

        findProjectPart.getProject().getChatRoomList().stream()
                .filter(chatRoom -> chatRoom.getProjectPart().equals(findProjectPart))
                .findAny().orElseThrow()
                .updateConfirmTrue();

    }

    /**
     * 프로젝트 상태 변경
     */
    @Transactional(readOnly = false)
    public void projectStatusChange(Long projectNo, String status) {
        try {
            Optional<Project> oProject = projectRepository.findById(projectNo);
            if (oProject.isPresent()) {
                Project findProject = oProject.get();
                findProject.changeStatus(status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 프로젝트 파트별 참여자 추방
     * @param
     */
    @Transactional(readOnly = false)
    public Boolean projectQuitExit(Long userProjectNo) {
        try {
            Optional<UserProject> oUserProject = userProjectRepository.findById(userProjectNo);
            if (oUserProject.isPresent()) {
                UserProject findUserProject = oUserProject.get();
                findUserProject.removeProjectPart();
                return true;
            }
            throw new RuntimeException("회원이 참여중인 프로젝트 정보 없음. 삭제 불가!");
        } catch (Exception e) {
            e.printStackTrace();
            throw e; //컨트롤러로 Exception 리턴
        }
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
    @Transactional(readOnly = false)
    public RecruitDto mammothDetail(Long recruitNo, String loginId) {
        try{
            Optional<Recruit> oRecruit = recruitRepository.findById(recruitNo);
            if (oRecruit.isPresent()) {
                Mammoth mammoth = (Mammoth) oRecruit.get();
                /* 회원별 게시글별 좋아요 유무 */
                Optional<BookMark> like = bookMarkRepository.findByUserAndRecruit(mammoth.getRecruitUser(), mammoth);
                /* 로그인하지 않았다면 좋아요여부 false  */
                boolean isBookmarked =  loginId.equals("null") ?  false : mammoth.getBookMarkList()
                        .stream().anyMatch(bookMark ->  bookMark.getUser().getEmail().equals(loginId));
                UserDto userDto = UserDto.userEntityToDto(mammoth.getRecruitUser());
                mammoth.incrementViews();
                // 코끼리 조회 정보
                return RecruitDto.builder().build().mammothEntityToDto(mammoth, userDto, "mammoth", isBookmarked);
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


    /**
     * 맘모스 글 삭제
     */
    @Transactional(readOnly = false)
    public Boolean mammothDelete(Long recruitNo) {
        try {
            Optional<Recruit> oRecruit = recruitRepository.findById(recruitNo);
            if (oRecruit.isPresent()) {
                Recruit findRecruit = oRecruit.get();
                try { //2차 Try문 - 수정로직
                    findRecruit.deleteRecruit(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("맘모스 삭제 실패"); //Controller에서 Catch
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("현재 맘모스를 찾을수 없습니다."); //Controller에서 Catch
        }
        return true; //2차 Try까지 무사히 통과하면 true반환
    }

}
