package io.codertown.web.controller;

import io.codertown.web.payload.CokkiriSaveRequest;
import io.codertown.web.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;
    /**
     *
     * @param request : JSON 데이터 <br/>
     * {
     *     "userId": String,
     *     "coggleTitle": String,
     *     "projectSubject": String,
     *     "projectTitle": String,
     *     "teamname": String,
     *     "objectWeek": String(int), <br/>
     *     "partList": [
     *         {
     * 			 "partNo" : String(int),
     * 			 "count" : String(int)
     *         },{}
     *     ], <br/>
     *     "content" : String(text) <br/>
     * }
     * @return
     */
    @PostMapping("/cokkiri-save")
    public ResponseEntity<Boolean> cokkiriSave(@RequestBody CokkiriSaveRequest request) {
        try {
            System.out.println("request = " + request);
            Boolean result = recruitService.cokkiriSave(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
