package io.codertown.web.entity.recruit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecruitController {


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
    @PostMapping("/coggle-save")
    public ResponseEntity<Object> coggleSave(@RequestBody CokkiriSaveRequest request) {
        System.out.println("request = " + request);
        return null;
    }
}
