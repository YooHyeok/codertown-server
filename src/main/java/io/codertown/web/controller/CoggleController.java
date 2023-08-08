package io.codertown.web.controller;

import io.codertown.web.dto.CoggleDto;
import io.codertown.web.dto.CoggleListDto;
import io.codertown.web.dto.CommentDto;
import io.codertown.web.payload.request.*;
import io.codertown.web.service.CoggleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코글 상세페이지 API
     * @param coggleNo
     * @return 성공: TRUE | 실패: FALSE
     */
    @GetMapping("/coggle/detail/{coggleNo}")
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
     * @param page 페이지 정보
     * @return 성공: TRUE | 실패: FALSE
     */
    @GetMapping("/coggle/{page}")
    public ResponseEntity<CoggleListDto> coggleList(@PathVariable(required = false) Integer page, @RequestBody CoggleListRequest request) {
        try {
            CoggleListDto result = coggleService.coggleList(page,request);
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
    public ResponseEntity<List<CommentDto>> coggleCommentEdit(@PathVariable Long coggleNo) {
        try {
            List<CommentDto> result = coggleService.coggleCommentJSON(coggleNo);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
