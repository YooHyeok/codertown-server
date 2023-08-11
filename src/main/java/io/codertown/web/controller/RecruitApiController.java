package io.codertown.web.controller;

import io.codertown.web.dto.RecruitDto;
import io.codertown.web.payload.SuccessBooleanResult;
import io.codertown.web.payload.request.*;
import io.codertown.web.payload.response.CokkiriDetailResponse;
import io.codertown.web.payload.response.RecruitListResponse;
import io.codertown.web.service.RecruitService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecruitApiController {

    private final RecruitService recruitService;

    /**
     * 코끼리 목록 API
     * @param page
     * @return
     */
    @ApiOperation(value="코끼리 목록 출력 API", notes="코끼리 목록 출력에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "코끼리 목록 리스트 JSON 데이터",responseCode = "200")
    @GetMapping(path = "/recruit/{page}/{dType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecruitListResponse> cokkiriList(@PathVariable(required = false) Integer page,
                                                           @PathVariable(required = false) String dType) {
        try {
            RecruitListResponse recruitList = recruitService.recruitList(page, dType);
            return ResponseEntity.ok(recruitList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);        }
    }

    /**
     * 코끼리 & 프로젝트 저장 API <br/>
     * @param request : JSON 데이터 <br/>
     * { <br/>
     *     "userId": String, <br/>
     *     "coggleTitle": String, <br/>
     *     "projectSubject": String, <br/>
     *     "projectTitle": String, <br/>
     *     "teamname": String, <br/>
     *     "objectWeek": String(int), <br/>
     *     "partList": [ <br/>
     *         { <br/>
     * 			 "partNo" : String(int), <br/>
     * 			 "count" : String(int) <br/>
     *         },{} <br/>
     *     ], <br/>
     *     "content" : String(text) <br/>
     * }
     * @return
     */
    @ApiOperation(value="코끼리&프로젝트 저장 API", notes="코끼리와 프로젝트를 동시에 저장")
    @ApiResponse(description = "저장 성공 결과",responseCode = "200")
    @PostMapping(path = "/cokkiri-save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> cokkiriSave(@RequestBody CokkiriSaveRequest request) {
        try {
            Boolean result = recruitService.cokkiriSave(request);
            return ResponseEntity.ok(SuccessBooleanResult.builder().build().setResult(result));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코끼리 & 프로젝트 수정(삭제) API
     * @param request
     * @return
     */
    @ApiOperation(value="코끼리&프로젝트 수정 API", notes="코끼리와 프로젝트를 동시에 수정")
    @ApiResponse(description = "수정 성공 결과",responseCode = "200")
    @PostMapping(path = "/cokkiri-update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> cokkiriUpdate(@RequestBody CokkiriUpdateRequest request) {
        try {
            Boolean result = recruitService.cokkiriUpdate(request);
            return ResponseEntity.ok(SuccessBooleanResult.builder().build().setResult(result));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 코끼리 & 프로젝트 상세보기 API
     * @param recruitNo
     * @return
     */
    @ApiOperation(value="코끼리 상세페이지 API", notes="코끼리 상세페이지 출력에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "코끼리 상세페이지 JSON 데이터",responseCode = "200")
    @GetMapping(path = "/cokkiri-detail/{recruitNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CokkiriDetailResponse> cokkiriDetail(@PathVariable(required = true) Long recruitNo) {
        try {
            CokkiriDetailResponse cokkiriDetailResponse = recruitService.cokkiriDetail(recruitNo);
            return ResponseEntity.ok(cokkiriDetailResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);        }
    }

    /**
     * 프로젝트 참가 요청 API
     * 요청 회원 정보, 원하는 파트 정보에 대한 DM을 전송한다.
     * @param request
     */
    @ApiOperation(value="프로젝트 참가 요청 API", notes="프로젝트 참여를 요청한다.")
    @ApiResponse(description = "참여요청 성공 결과",responseCode = "200")
    @PostMapping("/cokkiri/join-request")
    public void projectJoinRequest(@RequestBody ProjectJoinRequest request) {
        recruitService.projectJoinRequest(request);
    }

    /**
     * 맘모스 저장 API <br/>
     * @param request : JSON 데이터 <br/>
     * { <br/>
     *     "userId": String, <br/>
     *     "coggleTitle": String, <br/>
     *     "location": String, <br/>
     *     "content" : String(text) <br/>
     * }
     * @return
     */
    @ApiOperation(value="맘모스 게시글 저장 API", notes="맘모스 게시글 저장")
    @ApiResponse(description = "저장 성공 결과",responseCode = "200")
    @PostMapping(path = "/mammoth-save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> mammothSave(@RequestBody MammothSaveRequest request) {
        try {
            Boolean result = recruitService.mammothSave(request);
            return ResponseEntity.ok(SuccessBooleanResult.builder().build().setResult(result));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 맘모스 상세보기 API
     * @param recruitNo
     * @return
     */
    @ApiOperation(value="맘모스 상세페이지 API", notes="맘모스 상세페이지 출력에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "맘모스 상세페이지 JSON 데이터",responseCode = "200")
    @GetMapping(path = "/mammoth-detail/{recruitNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecruitDto> mammothDetail(@PathVariable(required = true) Long recruitNo) {
        try {
            RecruitDto mammothDto = recruitService.mammothDetail(recruitNo);
            return ResponseEntity.ok(mammothDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 맘모스 글 수정 API
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @ApiOperation(value="맘모스 게시글 수정 API", notes="맘모스 게시글 수정")
    @ApiResponse(description = "수정 성공 결과",responseCode = "200")
    @PostMapping(path = "/mammoth-update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> mammothUpdate(@RequestBody MammothUpdateRequest request) {
        try {
            Boolean result = recruitService.mammothUpdate(request);
            return ResponseEntity.ok(SuccessBooleanResult.builder().build().setResult(result));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
