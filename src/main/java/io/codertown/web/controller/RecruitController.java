package io.codertown.web.controller;

import io.codertown.web.dto.RecruitDto;
import io.codertown.web.payload.request.CokkiriSaveRequest;
import io.codertown.web.payload.request.CokkiriUpdateRequest;
import io.codertown.web.payload.request.MammothSaveRequest;
import io.codertown.web.payload.request.ProjectJoinRequest;
import io.codertown.web.payload.response.CokkiriDetailResponse;
import io.codertown.web.payload.response.RecruitListResponse;
import io.codertown.web.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;

    /**
     * 코끼리 목록 API
     * @param page
     * @return
     */
    @GetMapping("/recruit/{dType}/{page}")
    public ResponseEntity<RecruitListResponse> cokkiriList(@PathVariable(required = false) Integer page,
                                                                 @PathVariable(required = false) String dType) {
        System.out.println("dType = " + dType);
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
    @PostMapping("/cokkiri-save")
    public ResponseEntity<Boolean> cokkiriSave(@RequestBody CokkiriSaveRequest request) {
        try {
            Boolean result = recruitService.cokkiriSave(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);        }
    }


    /**
     * 코끼리 & 프로젝트 수정(삭제) API
     */
    @PostMapping("/cokkiri-update")
    public void cokkiriEdit(@RequestBody CokkiriUpdateRequest request) {
        System.out.println("request = " + request);
        Boolean result = recruitService.cokkiriUpdate(request);
    }

    /**
     * 코끼리 & 프로젝트 상세보기 API
     * @param recruitNo
     * @return
     */
    @GetMapping("/cokkiri-detail/{recruitNo}")
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
    @PostMapping("/mammoth-save")
    public ResponseEntity<Boolean> mammothSave(@RequestBody MammothSaveRequest request) {
        try {
            Boolean result = recruitService.mammothSave(request);
            return ResponseEntity.ok(result);
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
    @GetMapping("/mammoth-detail/{recruitNo}")
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
     */
}
