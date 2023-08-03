package io.codertown.web.controller;

import io.codertown.web.dto.CoggleDto;
import io.codertown.web.dto.CommentChildrenQueryDto;
import io.codertown.web.dto.CommentFlatDto;
import io.codertown.web.dto.CommentQueryDto;
import io.codertown.web.payload.request.*;
import io.codertown.web.service.CoggleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CoggleController {

    private final CoggleService coggleService;

    /**
     * 코글 저장 API
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @PostMapping("/coggle-save")
    public ResponseEntity<Boolean> coggleSave(@RequestBody CoggleSaveRequest request) {
        try {
            Boolean result = coggleService.coggleSave(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코글 상세페이지 API
     * @param coggleNo
     * @return 성공: TRUE | 실패: FALSE
     */
    @GetMapping("/coggle/{coggleNo}")
    public ResponseEntity<CoggleDto> coggleDetail(@PathVariable Long coggleNo) {
        try {
            CoggleDto result = coggleService.coggleDetail(coggleNo);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코글 수정 API
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @PostMapping("/coggle-edit")
    public ResponseEntity<Boolean> coggleEdit(@RequestBody CoggleEditRequest request) {
        try {
            Boolean result = coggleService.coggleEdit(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코글 목록 출력 API
     * @param request 페이지 정보
     * @return 성공: TRUE | 실패: FALSE
     */
    @GetMapping("/coggle")
    public ResponseEntity<List<CoggleDto>> coggleList() {
        try {
            List<CoggleDto> result = coggleService.coggleList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코글-댓글 저장 API
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @PostMapping("/coggle/comment-save")
    public ResponseEntity<Boolean> coggleCommentSave(@RequestBody CommentSaveRequest request) {
        try {
            Boolean result = coggleService.coggleCommentSave(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코글-댓글 수정 API
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @PostMapping("/coggle/comment-edit")
    public ResponseEntity<Boolean> coggleCommentEdit(@RequestBody CommentEditRequset request) {
        try {
            Boolean result = coggleService.coggleCommentEdit(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코글-댓글 삭제(블라인드처리) API
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @PostMapping("/coggle/comment-delete")
    public ResponseEntity<Boolean> coggleCommentDelete(@RequestBody CoggleDeleteParamRequest request) {
        try {
            Boolean result = coggleService.coggleCommentDelete(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코글-댓글(무한댓글 계층구조) 출력 API
     * @param coggleNo
     * @return
     */
    @GetMapping("/coggle/{coggleNo}/comment")
    public ResponseEntity<List<CommentQueryDto>> coggleCommentEdit(@PathVariable Long coggleNo) {
        try {
            List<CommentFlatDto> result = coggleService.coggleCommentJSON(coggleNo);
            List<CommentQueryDto> collect = result.stream()
                    .collect(Collectors.groupingBy( // 그룹핑 parent를 기준으로 그룹핑한다.
                              commentFlatDto -> CommentQueryDto.builder().build().groupingByBuilder(commentFlatDto)
                            , () -> new TreeMap<>(Comparator.comparing(CommentQueryDto::getParentNo))// 부모 댓글 기준 오름차순 정렬
                            , Collectors.mapping( // 매핑할 List - List변환시 매핑할 타입 객체
                                      commentFlatDto -> CommentChildrenQueryDto.builder().build().mappingByBuilder(commentFlatDto)
                                    , Collectors.toList()
                            )
                    )).entrySet().stream()
                    //중복 제거 - 매핑한 List의 타입으로 중복을 제거한다.
                    .map(entry -> CommentQueryDto.builder().build().entrySetToManByBuilder(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(collect);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
