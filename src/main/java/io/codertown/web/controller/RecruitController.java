package io.codertown.web.controller;

import io.codertown.web.payload.CokkiriDetailResponse;
import io.codertown.web.payload.CokkiriSaveRequest;
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
     * 코끼리 저장 API <br/>
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
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 코끼리 상세보기 API
     * @param recruitNo
     * @return
     */
    @GetMapping("/cokkiri/{recruitNo}")
    public ResponseEntity<CokkiriDetailResponse> cokkiriDetail(@PathVariable(required = true) Long recruitNo) {
        try {
            CokkiriDetailResponse cokkiriDetailResponse = recruitService.cokkiriDetail(recruitNo);
            System.out.println("cokkiriDetailResponse = " + cokkiriDetailResponse);
            return ResponseEntity.ok(cokkiriDetailResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
