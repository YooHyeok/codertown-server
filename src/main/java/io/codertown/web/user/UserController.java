package io.codertown.web.user;

import io.codertown.web.user.payload.SignInRequest;
import io.codertown.web.user.payload.SignStatus;
import io.codertown.web.user.payload.SignUpRequest;
import io.codertown.web.user.payload.SignUpResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
     * 회원 가입 API
     * @param request Client JSON 데이터
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     *    password : 비밀번호 <br/>
     *    nickname : 닉네임 <br/>
     * profileIcon : 프로필 아이콘 <br/>
     *      gender : 성별
     * </pre>
     * @return Boolean 저장 성공/실패 여부
     */

    @ApiOperation(value="회원가입", notes="회원가입 기능")
    @ApiResponse(description = "회원가입 성공",responseCode = "200")
    @PostMapping("/sign-up")
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
    @ApiResponse(description = "로그인 성공",responseCode = "200",content = @Content(schema = @Schema(implementation = SignUpResponse.class)))
    @PostMapping("/sign-in")
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
     * 마이페이지 API
     * @param loginEmail Client 파라미터
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     * </pre>
     * @return UserDto - [회원정보]
     */
    @PostMapping("/mypage")
    public ResponseEntity<UserDto> signIn(@RequestBody String loginEmail) {
        try {
            UserDto userDto = userService.userInfo(loginEmail);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
