package io.codertown.web.controller;

import io.codertown.web.dto.ChatRoomDetailResponse;
import io.codertown.web.payload.SuccessBooleanResult;
import io.codertown.web.payload.request.CreateCokkiriChatRoomRequest;
import io.codertown.web.payload.response.ChatRoomListResponse;
import io.codertown.web.service.ChatRoomService;
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
public class ChatRoomApiController {

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

    /**
     * 채팅 Room 목록 조회
     * @param roomNo
     * @return
     */
    @ApiOperation(value="코끼리 참여 채팅방 목록 출력 API", notes="코끼리 참여 채팅방 목록 출력에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "코끼리 참여 채팅방 목록 리스트 JSON 데이터",responseCode = "200")
    @PostMapping(path = "/cokkiri-chat-list", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatRoomListResponse> cokkiriChatList(String loginEmail) {

        try {
            ChatRoomListResponse response = chatRoomService.userChatList(loginEmail);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 채팅 Room 상세 정보 조회
     * @param roomNo
     * @return
     */
    @ApiOperation(value="코끼리 참여 채팅방 상세 출력 API", notes="코끼리 참여 채팅방 상세 출력에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "코끼리 참여 채팅방 상세 JSON 데이터",responseCode = "200")
    @PostMapping(path = "/cokkiri-chat-detail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatRoomDetailResponse> userChatDetail(String roomNo) {

        try {
            return ResponseEntity.ok(chatRoomService.userChatDetail(roomNo));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
