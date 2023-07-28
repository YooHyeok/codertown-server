package io.codertown.web.user;

import io.codertown.web.user.payload.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * *****************************************************<p>
 * 패키지:io.codertown.web.user<p>
 * 파일 : UserController.java<p>
 * 프로그램 설명 : 회원가입, 로그인(토큰), 마이페이지, 로그아웃<p>
 * 연관테이블 : user, user_project, project, coggle<p>
 * 개발 : 유재혁<p>
 * *****************************************************<p>
 */
@RestController
@RequiredArgsConstructor
//public class UserController extends CommonLoggerComponent {
public class UserController {
    private final UserService userService;

    /**
     * Eamil 중복 확인 API
     * @param email Client Text/Plain 파라미터
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     * </pre>
     * @return Map("existstu",Boolean) - true : 중복
     */
    @PostMapping(path = "/email-exists", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> emailExists(@RequestBody String email) {
        try {
            Boolean exists = userService.existsByEmail(email);
            Map<String, Object> result = new HashMap<>();
            result.put("exists", exists);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 회원 가입 API
     * @param request Client JSON 데이터
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     *    password : 비밀번호 <br/>
     *    nickname : 닉네임 <br/>
     * profileIcon : 프로필 아이콘 <br/>
     *      gender : 성별
     * </pre>
     * @return SignStatus 저장 성공/실패 여부
     */
    @ApiOperation(value="회원가입", notes="회원가입 기능")
    @ApiResponse(description = "회원가입 성공",responseCode = "200")
    @PostMapping(path = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignStatus> signUp(@RequestBody SignUpRequest request) {
        try {
            SignStatus signUpResult = userService.signUp(request);
            return ResponseEntity.ok(signUpResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 로그인 API
     * @param request Client 요청 DTO 객체
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     *    password : 비밀번호
     * </pre>
     * @return SignUpResponse - [로그인정보/성공여부]
     */
    @ApiOperation(value="로그인", notes="로그인 기능")
    @ApiResponse(description = "로그인 성공" , responseCode = "200", content = @Content(schema = @Schema(implementation = SignUpResponse.class)))
    @PostMapping(path="/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignUpResponse> signIn(@RequestBody SignInRequest request) {
        try {
            SignUpResponse signUpResponse = userService.signIn(request);
            return ResponseEntity.ok(signUpResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 마이페이지 회원정보 API
     * @param loginEmail Client 파라미터
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     * </pre>
     * @return UserDto - [회원정보]
     */
    @ApiOperation(value="마이페이지", notes="회원 정보")
    @ApiResponse(description = "정상 출력",responseCode = "200")
    @PostMapping(path = "/mypage/userInfo", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> mypage(@RequestBody String loginEmail) {
        try {
            UserDto userDto = userService.userInfo(loginEmail);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 닉네임 중복 확인 API
     * @param email Client Text/Plain 파라미터
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     * </pre>
     * @return Map("existstu",Boolean) - true : 중복
     */
    @PostMapping(path = "/mypage/nickname-exists", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> nicknameExists(@RequestBody String email) {
        try {
            Boolean exists = userService.existsByNickname(email);
            Map<String, Object> result = new HashMap<>();
            result.put("exists", exists);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 마이페이지 회원정보 수정 API
     * @param userEdit Client 파라미터
     * <pre>
     *       originEmail : 기존 이메일 (로그인계정) <br/>
     *       changeEmail : 변경 이메일  <br/>
     *          nickname : 변경 닉네임 <br/>
     *          password : 변경 패스워드 <br/>
     *          profile : 변경 프로필 <br/>
     * </pre>
     * @return UserDto - [회원정보]
     */
    @PostMapping("/mypage/userEdit")
    public ResponseEntity<UserDto> userEdit(@RequestBody UserEditRequest userEdit) {
        try {
            UserDto editResult = userService.userEdit(userEdit);
            return ResponseEntity.ok(editResult);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
