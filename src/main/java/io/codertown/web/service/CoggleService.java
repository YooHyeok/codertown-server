package io.codertown.web.service;

import io.codertown.web.dto.CoggleDto;
import io.codertown.web.dto.CommentFlatDto;
import io.codertown.web.entity.Coggle;
import io.codertown.web.entity.Comment;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.request.CoggleEditRequest;
import io.codertown.web.payload.request.CoggleSaveRequest;
import io.codertown.web.payload.request.CommentEditRequset;
import io.codertown.web.payload.request.CommentSaveRequest;
import io.codertown.web.repository.CoggleRepository;
import io.codertown.web.repository.CommentRepository;
import io.codertown.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoggleService {

    private final CoggleRepository coggleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

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
                    .user(writer)
                    .build();
            coggleRepository.save(coggle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 코글 상세페이지
     * @param coggleNo
     * @return 성공: TRUE | 실패: FALSE
     */
    public CoggleDto coggleDetail(Long coggleNo) throws RuntimeException {
        Optional<Coggle> oCoggle = coggleRepository.findById(coggleNo);
        if (oCoggle.isPresent()) {
            Coggle findCoggle = oCoggle.get();
            return CoggleDto.builder().build().changeCoggleDto(findCoggle); //코글 변환후 반환
        }
        throw new RuntimeException("현재 코글을 찾을수 없습니다."); //Controller에서 Catch
    }

    /**
     * 코글 수정
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @Transactional
    public Boolean coggleEdit(CoggleEditRequest request) throws RuntimeException {
        Optional<Coggle> oCoggle = coggleRepository.findById(request.getCoggleNo());
        if (oCoggle.isPresent()) {
            Coggle findCoggle = oCoggle.get();
            try {
                findCoggle.updateCoggle(request);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("코글 수정 실패"); //Controller에서 Catch
            }
        }
        throw new RuntimeException("현재 코글을 찾을수 없습니다."); //Controller에서 Catch
    }

    /**
     * 코글-댓글 저장
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @Transactional
    public Boolean coggleCommentSave(CommentSaveRequest request) {
        User findWriter = (User)userRepository.findByEmail(request.getWriter());
        Optional<Coggle> oCoggle = coggleRepository.findById(request.getCoggleNo());
        Comment parentComment = null;
        if(request.getParentNo() != null) parentComment = commentRepository.findById(request.getParentNo()).get();
        if (oCoggle.isPresent()) {
            Coggle findCoggle = oCoggle.get();
            try {
                Comment buildComment = Comment.builder()
                        .user(findWriter)
                        .coggle(findCoggle)
                        .parent(parentComment)
                        .content(request.getContent())
                        .build();
                //                    System.out.println("buildComment.getParent().getChildren() = " + buildComment.getParent().getChildren());
                if (parentComment != null) parentComment.getChildren().add(buildComment); // 현재자식을 부모의 자식리스트에 저장
                commentRepository.save(buildComment);
                return true;
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
    public Boolean coggleCommentEdit(CommentEditRequset request) {
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
     * 코글-댓글 출력
     *
     * @param coggleNo
     * @return
     */
    public List<CommentFlatDto> coggleCommentJSON(Long coggleNo) throws Exception {
        Optional<Coggle> oCoggle = coggleRepository.findById(coggleNo);
        if (oCoggle.isPresent()) {
            Coggle coggle = oCoggle.get();
            List<CommentFlatDto> collect = commentRepository.findByCoggle(coggle).stream()
                    .map(comment ->  CommentFlatDto.builder().build().changeEntityToDto(comment))
                    .collect(Collectors.toList());
            return collect;
        }
        throw new Exception("Exception발생!!");
    }
}
