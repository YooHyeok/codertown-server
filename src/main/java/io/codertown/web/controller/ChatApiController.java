package io.codertown.web.controller;

import io.codertown.web.payload.SuccessBooleanResult;
import io.codertown.web.payload.request.CreateCokkiriChatRoomRequest;
import io.codertown.web.repository.ChatRoomRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatRoomService chatRoomService;

    /**
     * 코끼리 참여 채팅방 생성 API <br/>
     * @param request : JSON 데이터 파싱<br/>
     * { <br/>
     *     "loginId": String, <br/>
     *     "writerId": String, <br/>
     *     "projectNo": Long, <br/>
     *     "projectPartNo" : Long <br/>
     * }
     * @return
     */
    @ApiOperation(value="코끼리 참여 채팅방 생성 API", notes="코끼리 참여 채팅방 생성")
    @ApiResponse(description = "저장 성공 결과",responseCode = "200")
    @PostMapping(path = "/create-cokkiri-chatroom", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> createCokkiriChatroom(@RequestBody CreateCokkiriChatRoomRequest request) {
        try {
            Boolean result = chatRoomService.createChatRoomForCokkiri(request);
            return ResponseEntity.ok(SuccessBooleanResult.builder().build().setResult(result));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}