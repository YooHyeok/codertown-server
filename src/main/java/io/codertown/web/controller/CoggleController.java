package io.codertown.web.controller;

import io.codertown.web.dto.CommentChildrenQueryDto;
import io.codertown.web.dto.CommentFlatDto;
import io.codertown.web.dto.CommentQueryDto;
import io.codertown.web.payload.request.CoggleEditRequest;
import io.codertown.web.payload.request.CoggleSaveRequest;
import io.codertown.web.payload.request.CommentEditRequset;
import io.codertown.web.payload.request.CommentSaveRequest;
import io.codertown.web.service.CoggleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
     * 코글-댓글(무한댓글 계층구조) 출력 API
     * @param coggleNo
     * @return
     */
    @PostMapping("/coggle/{coggleNo}/comment")
    public ResponseEntity<List<CommentQueryDto>> coggleCommentEdit(@PathVariable Long coggleNo) {
        try {
            List<CommentFlatDto> result = coggleService.coggleCommentJSON(coggleNo);
            List<CommentQueryDto> collect = result.stream()
                    .collect(Collectors.groupingBy(commentFlatDto ->
                                    CommentQueryDto.builder()
                                            .coggleNo(commentFlatDto.getCoggleNo())
                                            .parentNo(commentFlatDto.getParentNo())
                                            .commentNo(commentFlatDto.getCommentNo())
                                            .writer(commentFlatDto.getWriter())
                                            .content(commentFlatDto.getContent())
                                            .build()
                            , () -> new TreeMap<>(Comparator.comparing(CommentQueryDto::getParentNo))// 부모 댓글 기준 오름차순 정렬
                            , Collectors.mapping(
                                    commentFlatDto ->
                                            new CommentChildrenQueryDto(commentFlatDto.getCoggleNo(),
                                                    commentFlatDto.getParentNo(),
                                                    commentFlatDto.getCommentNo(),
                                                    commentFlatDto.getWriter(),
                                                    commentFlatDto.getContent()), Collectors.toList()
                            )
                    )).entrySet().stream()
                    .map(entry -> new CommentQueryDto(
                            entry.getKey().getCoggleNo()
                            , entry.getKey().getParentNo()
                            , entry.getKey().getCommentNo()
                            , entry.getKey().getWriter()
                            , entry.getKey().getContent()
                            , entry.getValue()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok(collect);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
