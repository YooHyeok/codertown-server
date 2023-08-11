package io.codertown.web.controller;

import io.codertown.web.dto.UserDto;
import io.codertown.web.payload.ExistsResult;
import io.codertown.web.payload.SignStatus;
import io.codertown.web.payload.request.SignInRequest;
import io.codertown.web.payload.request.SignUpRequest;
import io.codertown.web.payload.request.UserUpdateRequest;
import io.codertown.web.payload.response.SignInResponse;
import io.codertown.web.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * *****************************************************<p>
 * 패키지:io.codertown.web.user<p>
 * 파일 : UserApiController.java<p>
 * 프로그램 설명 : 회원가입, 로그인(토큰), 마이페이지, 로그아웃<p>
 * 연관테이블 : user, user_project, project, coggle<p>
 * 개발 : 유재혁<p>
 * *****************************************************<p>
 */
@RestController
@RequiredArgsConstructor
//public class UserController extends CommonLoggerComponent {
public class UserApiController {
    private final UserService userService;

    /**
     * Eamil 중복 확인 API
     * @param email Client Text/Plain 파라미터
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     * </pre>
     * @return ExistsResult("existst",Boolean) - true : 중복
     */
    @ApiOperation(value="이메일 중복확인 API", notes="회원가입시 이메일 중복 확인")
    @ApiResponse(description = "중복 확인 성공 결과",responseCode = "200")
    @PostMapping(path = "/email-exists", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExistsResult> emailExists(@RequestBody String email) {
        try {
            Boolean exists = userService.existsByEmail(email);
            ExistsResult result = ExistsResult
                    .builder()
                    .Exists(exists)
                    .build();
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
    @ApiOperation(value="회원가입 API", notes="회원가입 기능")
    @ApiResponse(description = "회원가입 성공 결과",responseCode = "200")
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
    @ApiOperation(value="로그인 API", notes="로그인 기능")
    @ApiResponse(description = "로그인 성공 결과" , responseCode = "200", content = @Content(schema = @Schema(implementation = SignInResponse.class)))
    @PostMapping(path="/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest request) {
        try {
            SignInResponse signUpResponse = userService.signIn(request);
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
    @ApiOperation(value="마이페이지 - 회원정보 조회 API", notes="회원정보 출력")
    @ApiResponse(description = "회원 정보 조회 성공 결과",responseCode = "200")
    @PostMapping(path = "/user-info", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
    @ApiOperation(value="닉네임 중복확인 API", notes="회원정보 수정시 닉네임 중복 확인")
    @ApiResponse(description = "중복 확인 성공 결과",responseCode = "200")
    @PostMapping(path = "/nickname-exists", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExistsResult> nicknameExists(@RequestBody String email) {
        try {
            Boolean exists = userService.existsByNickname(email);
            ExistsResult result = ExistsResult
                    .builder()
                    .Exists(exists)
                    .build();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 마이페이지 회원정보 수정 API
     * @param request Client 파라미터
     * <pre>
     *       originEmail : 기존 이메일 (로그인계정) <br/>
     *       changeEmail : 변경 이메일  <br/>
     *          nickname : 변경 닉네임 <br/>
     *          password : 변경 패스워드 <br/>
     *          profile : 변경 프로필 <br/>
     *          file : 프로필 이미지 파일 <br/>
     * </pre>
     * @return UserDto - [회원정보]
     */
    @ApiOperation(value="마이페이지 - 회원정보 수정 API", notes="회원정보 수정")
    @ApiResponse(description = "회원정보 수정 성공 결과",responseCode = "200")
    @PostMapping(path="/user-update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> userUpdate(@ModelAttribute UserUpdateRequest request) {
        try {
            UserDto updateResult = userService.userUpdate(request);
            return ResponseEntity.ok(updateResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 클라이언트에서 프로필사진을 출력할때 호출된다 <br/>
     * img태그의 src속성에 의해 해당 url이 호출된다. <br/>
     * byte[] 배열로 변환하면서 DB의 blob타입 컬럼에 저장할경우 MIME타입이 함께 저장된다. <br/>
     * img태그에서 src에의해 파일정보가 호출되고 MIME 타입 정보를 기반으로 이미지를 올바른 형식으로 표시하게된다.
     * @param id
     * @param response
     */
    @GetMapping("/profileImage/{id}")
    public void profileThumbnail(@PathVariable Long id, HttpServletResponse response) {
        try {
            byte[] thumbnail = userService.profileImage(id);
            OutputStream out = response.getOutputStream();
            out.write(thumbnail);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
