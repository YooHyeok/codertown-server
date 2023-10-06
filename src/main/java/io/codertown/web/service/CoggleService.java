package io.codertown.web.service;

import io.codertown.support.PageInfo;
import io.codertown.web.dto.CoggleDto;
import io.codertown.web.dto.CoggleListDto;
import io.codertown.web.dto.CommentDto;
import io.codertown.web.entity.coggle.Coggle;
import io.codertown.web.entity.coggle.Comment;
import io.codertown.web.entity.coggle.LikeMark;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.request.*;
import io.codertown.web.repository.CoggleRepository;
import io.codertown.web.repository.CommentRepository;
import io.codertown.web.repository.LikeMarkRepository;
import io.codertown.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoggleService {

    private final CoggleRepository coggleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeMarkRepository likeMarkRepository;

    /**
     * 코글 저장
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @Transactional
    public Boolean coggleSave(CoggleSaveRequest request) {
        try {
            User writer = (User)userRepository.findByEmail(request.getWriter());
            Coggle coggle = Coggle.builder()
                    .category(request.getCategory())
                    .title(request.getTitle())
                    .content(request.getContent())
                    .status(false)
                    .views(0L)
                    .writer(writer)
                    .build();
            Coggle savedCoggle = coggleRepository.save(coggle);
            return savedCoggle.getId()!=null? true:false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 코글 상세페이지
     * @param coggleNo
     * @return 성공: TRUE | 실패: FALSE
     */
    @Transactional(readOnly = false)
    public CoggleDto coggleDetail(Long coggleNo, String loginId) throws RuntimeException {
        try {
            Optional<Coggle> oCoggle = coggleRepository.findById(coggleNo);
            if (oCoggle.isPresent()) {
                Coggle findCoggle = oCoggle.get();
                /* 로그인하지 않았다면 좋아요여부 false  */
                boolean isLiked =  loginId.equals("null") ?  false : findCoggle.getLikeMarkList()
                        .stream().anyMatch(bookMark ->  bookMark.getUser().getEmail().equals(loginId));
                findCoggle.incrementViews();
                return CoggleDto.builder().build().changeEntityToDto(findCoggle, isLiked); //코글 변환후 반환
            }
            throw new RuntimeException("현재 코글을 찾을수 없습니다."); //Controller에서 Catch
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 코글 좋아요 Toggle
     * @param coggleNo
     * @param userId
     * @return
     */
    @Transactional(readOnly = false)
    public Boolean likeMarkToggle(Long coggleNo, String userId) throws Exception {
        User user = (User) userRepository.findByEmail(userId);
        try {
            Optional<Coggle> oCoggle = coggleRepository.findById(coggleNo);
            if (oCoggle.isPresent()) {
                Coggle coggle = oCoggle.get();
                Optional<LikeMark> likeMark = likeMarkRepository.findByUserAndCoggle(user, coggle);
                LikeMark coggleLikeMark = LikeMark.builder().build().createCoggleLikeMark(user, coggle);
                if (likeMark.isEmpty()) { // 존재하지 않는다면 추가
                    likeMarkRepository.save(coggleLikeMark);
                    return likeMark.isEmpty(); // 추가됨
                }
                likeMarkRepository.delete(likeMark.get()); // 존재한다면 제거
                return likeMark.isEmpty(); // 제거됨
            }
            throw new RuntimeException("게시글 없음");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 코글 수정
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @Transactional
    public Boolean coggleUpdate(CoggleUpdateRequest request) throws RuntimeException {
        try {
            Optional<Coggle> oCoggle = coggleRepository.findById(request.getCoggleNo());
            if (oCoggle.isPresent()) {
                Coggle findCoggle = oCoggle.get();
                try {
                    findCoggle.updateCoggle(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("코글 수정 실패"); //Controller에서 Catch
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("현재 코글을 찾을수 없습니다."); //Controller에서 Catch
        }
        return true;
    }

    /**
     * 코글 삭제
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @Transactional
    public Boolean coggleDelete(Long coggleNo) throws RuntimeException {
        try {
            Optional<Coggle> oCoggle = coggleRepository.findById(coggleNo);
            if (oCoggle.isPresent()) {
                Coggle findCoggle = oCoggle.get();
                try {
                    findCoggle.deleteCoggle(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("코글 수정 실패"); //Controller에서 Catch
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("현재 코글을 찾을수 없습니다."); //Controller에서 Catch
        }
        return true;
    }

    /**
     * 코글 목록 출력
     * @param page 페이지 정보
     * @param request 쿼리 조건 컨디션
     * @return 성공: TRUE | 실패: FALSE
     */
    public CoggleListDto coggleList(CoggleListRequest request) throws RuntimeException {
        Integer page = request.getPage();
        page = page == null ? 1 : page;
        PageInfo pageInfo = PageInfo.builder().build()
                .createPageRequest(page, 10, "id", "DESC");
        Page<Coggle> pages = coggleRepository.findByCategoryAndUser(request.getCategory() , request.getKeyword(), request.getUrl(), request.getLoginId(), pageInfo.getPageRequest());
        Long totalCount = pages.getTotalElements();
        pageInfo.setPageInfo(pages, pageInfo);
        List<CoggleDto> coggleList = pages.stream()
                .map(coggle -> {
                    /* 회원별 게시글별 좋아요 유무 */
                    boolean isLikedMarked = coggle.getLikeMarkList()
                            .stream().anyMatch(bookMark ->  bookMark.getUser().getEmail().equals(request.getLoginId()));
                    return CoggleDto.builder().build().changeEntityToDto(coggle, isLikedMarked);
                })
                .collect(Collectors.toList());
        /* 마이페이지 좋아요일 경우 */
        if (request.getUrl() != null && request.getUrl().equals("myBookMark")) {
            coggleList = coggleList.stream().filter(coggleDto -> {
                return coggleDto.getIsLikeMarked() == true;
            }).collect(Collectors.toList());
            totalCount = Long.valueOf(coggleList.size());
        }
        return CoggleListDto.builder().pageInfo(pageInfo).coggleList(coggleList).articleCount(totalCount).build();
    }

    /**
     * 코글-댓글 저장
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @Transactional
    public Boolean coggleCommentSave(CommentSaveRequest request) {
        User findWriter = (User)userRepository.findByEmail(request.getWriter());
        User findMentionUser = (User)userRepository.findByEmail(request.getMentionUser());
        Optional<Coggle> oCoggle = coggleRepository.findById(request.getCoggleNo());
        Comment parentComment = null;
        if(request.getParentNo() != null) parentComment = commentRepository.findById(request.getParentNo()).get();
        if (oCoggle.isPresent()) {
            Coggle findCoggle = oCoggle.get();
            try {
                Comment buildComment = Comment.builder()
                        .writer(findWriter)
                        .coggle(findCoggle)
                        .parent(parentComment)
                        .content(request.getContent())
                        .depth(request.getDepth())
                        .mention(findMentionUser)
                        .status(false)
                        .build();
                if (parentComment != null) parentComment.getChildren().add(buildComment); // 현재자식을 부모의 자식리스트에 저장
                Comment savedComment = commentRepository.save(buildComment);
                return savedComment.getId()!=null? true:false;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("코글 댓글 작성 실패"); //Controller에서 Catch
            }
        }
        throw new RuntimeException("현재 코글을 찾을수 없습니다."); //Controller에서 Catch
    }

    /**
     * 코글-댓글 수정
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @Transactional
    public Boolean coggleCommentUpdate(CommentUpdateRequset request) {
        Optional<Comment> oComment = commentRepository.findById(request.getCommentNo());
        if (oComment.isPresent()) {
            try {
                Comment findComment = oComment.get();
                findComment.updateComment(request);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("코글 댓글 수정 실패"); //Controller에서 Catch
            }
        }
        throw new RuntimeException("현재 코글을 찾을수 없습니다."); //Controller에서 Catch
    }

    /**
     * 코글-댓글 삭제 API
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @Transactional
    public Boolean coggleCommentDelete(CoggleDeleteParamRequest request) {
        Optional<Comment> oComment = commentRepository.findById(request.getCommentNo());
        if (oComment.isPresent()) {
            try {
                Comment findComment = oComment.get();
                findComment.deleteComment();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("코글 댓글 삭제 실패"); //Controller에서 Catch
            }
        }
        throw new RuntimeException("현재 코글을 찾을수 없습니다."); //Controller에서 Catch
    }


    /**
     * 코글-댓글 출력
     *
     * @param coggleNo
     * @return
     */
    public List<CommentDto> coggleCommentJSON(Long coggleNo) throws Exception {
        Optional<Coggle> oCoggle = coggleRepository.findById(coggleNo);
        if (oCoggle.isPresent()) {
            Coggle coggle = oCoggle.get();
            List<CommentDto> collect = commentRepository.findByCoggle(coggle).stream()
                    .filter(comment -> comment.getParent() == null) // 1. parent가 null인 최상위 댓글들만 필터한다.
                    .map(comment ->  CommentDto.builder().build()
                            .changeEntityToDto(comment)) // 2. Entity를 Dto로 변환함과 동시에 내부적으로 children을 주입한다.
                    .collect(Collectors.toList());
            return collect;
        }
        throw new Exception("Exception발생!!");
    }
}
