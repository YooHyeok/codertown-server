package io.codertown.web.controller;

import io.codertown.web.dto.CoggleDto;
import io.codertown.web.dto.CoggleListDto;
import io.codertown.web.dto.CommentDto;
import io.codertown.web.payload.SuccessBooleanResult;
import io.codertown.web.payload.request.*;
import io.codertown.web.service.CoggleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @ApiOperation(value="코글 저장 API", notes="코글 게시글 저장")
    @ApiResponse(description = "저장 성공 결과",responseCode = "200")
    @PostMapping(path = "/coggle-save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> coggleSave(@RequestBody CoggleSaveRequest request) {
        try {
            Boolean result = coggleService.coggleSave(request);
            return ResponseEntity.ok(SuccessBooleanResult.builder().build().setResult(result));
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
    @ApiOperation(value="코글 상세페이지 API", notes="코글 상세페이지 출력에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "코글 상세페이지 JSON 데이터",responseCode = "200")
    @GetMapping(path = "/coggle-detail/{coggleNo}", produces = MediaType.APPLICATION_JSON_VALUE)
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
    @ApiOperation(value="코글 수정 API", notes="코글 게시글 수정")
    @ApiResponse(description = "수정 성공 결과",responseCode = "200")
    @PostMapping(path = "/coggle-update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> coggleupdate(@RequestBody CoggleUpdateRequest request) {
        try {
            Boolean result = coggleService.coggleUpdate(request);
            return ResponseEntity.ok(SuccessBooleanResult.builder().build().setResult(result));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코글 목록 출력 API
     * @param page 페이지 정보
     * @param request 쿼리 조건 컨디션
     * @return 성공: TRUE | 실패: FALSE
     */
    @ApiOperation(value="코글 목록 출력 API", notes="코글 목록 출력에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "코글 리스트 JSON 데이터",responseCode = "200")
    @GetMapping(path = "/coggle/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
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
    @ApiOperation(value="코글-댓글 저장 API", notes="코글-댓글 저장")
    @ApiResponse(description = "댓글 저장 성공 결과",responseCode = "200")
    @PostMapping(path = "/coggle/comment-save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> coggleCommentSave(@RequestBody CommentSaveRequest request) {
        try {
            Boolean result = coggleService.coggleCommentSave(request);
            return ResponseEntity.ok(SuccessBooleanResult.builder().build().setResult(result));
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
    @ApiOperation(value="코글-댓글 수정 API", notes="코글-댓글 수정")
    @ApiResponse(description = "댓글 수정 성공 결과",responseCode = "200")
    @PostMapping(path = "/coggle/comment-update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> coggleCommentUpdate(@RequestBody CommentUpdateRequset request) {
        try {
            Boolean result = coggleService.coggleCommentUpdate(request);
            return ResponseEntity.ok(SuccessBooleanResult.builder().build().setResult(result));
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
    @ApiOperation(value="코글-댓글 삭제 API", notes="코글-댓글 삭제 (블라인드 처리)")
    @ApiResponse(description = "댓글 삭제 성공 결과",responseCode = "200")
    @PostMapping(path = "/coggle/comment-delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> coggleCommentDelete(@RequestBody CoggleDeleteParamRequest request) {
        try {
            Boolean result = coggleService.coggleCommentDelete(request);
            return ResponseEntity.ok(SuccessBooleanResult.builder().build().setResult(result));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(SuccessBooleanResult.builder().build().setResult(false), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코글-댓글(무한댓글 계층구조) 출력 API
     * @param coggleNo
     * @return
     */
    @ApiOperation(value="코글-댓글 목록 출력 API", notes="코글-댓글 목록 출력에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "코글 댓글 리스트 JSON 데이터",responseCode = "200")
    @GetMapping(path = "/coggle/{coggleNo}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentDto>> coggleComment(@PathVariable Long coggleNo) {
        try {
            List<CommentDto> result = coggleService.coggleCommentJSON(coggleNo);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
