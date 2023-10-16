package io.codertown.web.controller;

import io.codertown.web.dto.UserDto;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.ExistsResult;
import io.codertown.web.payload.SignStatus;
import io.codertown.web.payload.SuccessBooleanResult;
import io.codertown.web.payload.request.SignInRequest;
import io.codertown.web.payload.request.SignUpRequest;
import io.codertown.web.payload.request.UserDisabledRequest;
import io.codertown.web.payload.request.UserUpdateRequest;
import io.codertown.web.payload.response.JoinedProjectDetailResponse;
import io.codertown.web.payload.response.JoinedProjectResponse;
import io.codertown.web.payload.response.NotificationResponse;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import java.util.Random;

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
    private final JavaMailSender javaMailSender;

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
    @PostMapping(path = "/email-exists", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExistsResult> emailExists(@RequestParam("email") String email) {
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

    @ApiOperation(value="이메일 인증 API", notes="회원가입시 이메일 인증번호 발송")
    @ApiResponse(description = "인증 번호 반환",responseCode = "200")
    @PostMapping(path = "/email-auth", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> emailAuthSend(@RequestParam("email") String email) {
        try {
            Random random = new Random();
            random.setSeed(System.currentTimeMillis());
            Integer randomValue = random.nextInt(1000000) % 1000000;
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(email);
//            message.setSubject("[코더타운] 회원가입 이메일 인증");
////          message.setText(randomValue.toString());
//            message.setText(textString, true);
//            javaMailSender.send(message);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("[코더타운] 회원가입 이메일 인증");

            String textString =
                    "<div style=\"background:#ffffff;margin:0;padding:0;font-family:'Apple SD Gothic', '맑은고딕', 'Nanum Gothic', sans-serif\">\n" +
                            "    <div style=\"background:#ffffff;margin:0 auto;padding:0;width:100%;max-width:600px;box-sizing:border-box;-webkit-text-size-adjust:none\">\n" +
                            "        <table align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#fff;margin:0;padding:0;max-width:600px\">\n" +
                            "            <tbody>" +
                            "               <tr>" +
                            "                   <td style=\"background:#ffffff;padding:63px 2px 0\">\n" +
                            "                       <table align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                            "                           <tbody>" +
                            "                               <tr>" +
                            "                                   <td align=\"center\">\n" +
                            "                                   <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                            "                                       <tbody>" +
                            "                                           <tr>" +
                            "                                               <td align=\"left\">" +
                            "                                                   <div class=\"logo-text\" style=\"font-size: 40px; font-family: 'KOTRAHOPE';  \">" +
                            "                                                       <b>C</b>oder<b>Town</b>" +
                            "                                                   </div>" +
                            "                                               </td>" +
                            "                                           </tr>\n" +
                            "                                           <tr>" +
                            "                                               <td colspan=\"2\" height=\"46\">" +
                            "                                               </td>" +
                            "                                           </tr>\n" +
                            "                                       </tbody>" +
                            "                                   </table>\n" +
                            "                               </td>" +
                            "                           </tr>\n" +
                            "                           <tr>" +
                            "                               <td align=\"left\" style=\"background:#fff;padding:0\">\n" +
                            "                                   <p style=\"margin:0 0 24px;padding:0;font-family:'Apple SD Gothic', '맑은고딕', 'Nanum Gothic', sans-serif;font-size:26px;color:#000;letter-spacing:-1px;line-height:32px;font-weight:bold\">" +
                            "                                       이메일 인증을 진행해주세요\n" +
                            "                                   </p>\n" +
                            "                                   <p style=\"margin:0 0 40px;padding:0;font-family:'Apple SD Gothic', '맑은고딕', 'Nanum Gothic',sans-serif;font-size:18px;color:#404040;letter-spacing:-1px;line-height:24px\">\n" +
                            "                                       안녕하세요. 코더타운을 이용해주셔서 감사합니다 :)<br>\n" +
                            "                                       코더타운 가입을 위해 아래 인증번호를 화면에 입력해주세요.\n" +
                            "                                   </p>\n" +
                            "                                   <div style=\"background:#f9f9fb;border:1px solid #ececec;border-radius:4px\">\n" +
                            "                                       <table align=\"center\" width=\"100%\" border=\"0\" bgcolor=\"#f9f9fe\" cellpadding=\"0\" style=\"border:0\">\n" +
                            "                                           <tbody>" +
                            "                                               <tr>" +
                            "                                                   <td height=\"38\"></td>" +
                            "                                               </tr>\n" +
                            "                                               <tr>" +
                            "                                                   <td align=\"center\" style=\"font-family:'Apple SD Gothic', '맑은고딕', 'Nanum Gothic',sans-serif;font-size:48px;color:#202020;letter-spacing:8px;line-height:56px\">\n" +
                                                                                    randomValue.toString() +"\n"+
                            "                                                   </td>" +
                            "                                               </tr>\n" +
                            "                                               <tr>" +
                            "                                                   <td height=\"38\">" +
                            "                                                   </td>" +
                            "                                               </tr>\n" +
                            "                                           </tbody>" +
                            "                                       </table>\n" +
                            "                                   </div>\n" +
                            "                                   <table align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border:0\">\n" +
                            "                                       <tbody>" +
                            "                                           <tr>" +
                        "                                                   <td align=\"left\" style=\"padding:16px 0 0;font-family:'Apple SD Gothic', '맑은고딕', 'Nanum Gothic',sans-serif;text-align:center;font-size:16px;color:#808080;letter-spacing:-1px;line-height:24px\">\n" +
                            "                                                   유효시간 5분 안에 인증하셔야 합니다.\n" +
                            "                                               </td>" +
                            "                                           </tr>\n" +
                            "                                           <tr>" +
                            "                                               <td height=\"56\">" +
                            "                                               </td>" +
                            "                                           </tr>\n" +
                            "                                       </tbody>" +
                            "                                   </table>\n" +
                            "                               </td>" +
                            "                           </tr>\n" +
                            "                       </tbody>" +
                            "                   </table>\n" +
                            "                   </td>" +
                            "               </tr>\n" +
                            "           </tbody>" +
                            "       </table>\n" +
                            "    </div>\n" +
                            "</div>";
            mimeMessageHelper.setText(textString, true);
            javaMailSender.send(mimeMessage);

            return ResponseEntity.ok(randomValue);
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
    @PostMapping(path = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignStatus> signUp(@ModelAttribute SignUpRequest request) {
        try {
            SignStatus signUpResult = userService.signUp(request);
            return ResponseEntity.ok(signUpResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 마이페이지 회원상태 수정 API
     * @param request UserDisabledRequest
     * <pre>
     *       loginEmail : 기존 이메일 (로그인계정) <br/>
     *       changeStatus : 변경할 상태값  <br/>

     * </pre>
     * @return SuccessBooleanResult - [성공여부]
     */
    @ApiOperation(value="마이페이지 - 회원상태 수정 API", notes="회원상태 수정")
    @ApiResponse(description = "회원상태 수정 성공 결과",responseCode = "200")
    @PostMapping(path="/change-status-account", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessBooleanResult> changeStatusAccount(@ModelAttribute UserDisabledRequest request) {
        try {
            SuccessBooleanResult successBooleanResult = userService.changeStatusAccount(request);
            return ResponseEntity.ok(successBooleanResult);
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


    @PostMapping("/userInfo")
    public ResponseEntity<UserDto> userInfo(String id) {
        User user = (User)userService.loadUserByUsername(id); //findById 로 id에대한 user의 레코드 정보를 반환
        UserDto userDto = UserDto.userEntityToDto(user);
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
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
    @PostMapping(path = "/user-info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> mypage(@RequestParam("loginEmail") String loginEmail) {
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
     * @param nickname Client Text/Plain 파라미터
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     * </pre>
     * @return Map("existstu",Boolean) - true : 중복
     */
    @ApiOperation(value="닉네임 중복확인 API", notes="회원정보 수정시 닉네임 중복 확인")
    @ApiResponse(description = "중복 확인 성공 결과",responseCode = "200")
    @PostMapping(path = "/nickname-exists", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExistsResult> nicknameExists(@RequestParam("nickname") String nickname) {
        try {
            Boolean exists = userService.existsByNickname(nickname);
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
    public ResponseEntity<SuccessBooleanResult> userUpdate(@ModelAttribute UserUpdateRequest request) {
        try {
            SuccessBooleanResult successBooleanResult = userService.userUpdate(request);
            return ResponseEntity.ok(successBooleanResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 참여중인 프로젝트 목록 API
     * @param page
     * @return
     */
    @ApiOperation(value="마이페이지 - 프로젝트 목록 출력 API", notes="프로젝트 목록 출력에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "프로젝트 목록 리스트 JSON 데이터",responseCode = "200")
    @GetMapping(path = "/joined-project", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JoinedProjectResponse> joinedProjectList(@RequestParam(required = false) Integer page,
                                                               @RequestParam(required = false, defaultValue = "10") Integer size,
                                                               @RequestParam String loginId) {
        try {
            JoinedProjectResponse joinedProject = userService.findJoinedProject(page, size, loginId);
            return ResponseEntity.ok(joinedProject);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * 회원삭제시 검증할 참여중인 프로젝트 갯수 API
     * @param loginId
     * @return
     */
    @ApiOperation(value="마이페이지 - 회원삭제시 프로젝트 갯수 조회 API", notes="프로젝트 갯수 출력에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "프로젝트 목록 갯수 JSON 데이터",responseCode = "200")
    @GetMapping(path = "/joined-project-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> joinedProjectList(@RequestParam String loginId) {
        try {
            Long joinedProjectCount = userService.findJoinedProjectCount(loginId);
            return ResponseEntity.ok(joinedProjectCount);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 프로젝트 인원현황 상세보기 API
     * @param page
     * @return
     */
    @ApiOperation(value="마이페이지 - 프로젝트 인원현황 상세보기 API", notes="프로젝트 인원현황 상세보기에 필요한 JSON 데이터 반환")
    @ApiResponse(description = "프로젝트 인원현황 상세보기 JSON 데이터",responseCode = "200")
    @PostMapping(path = "/joined-project-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JoinedProjectDetailResponse> joinedProjectDetail(@RequestParam Long projectNo) {
        try {
            JoinedProjectDetailResponse joinedProjectDetail = userService.joinedProjectDetail(projectNo);
            return ResponseEntity.ok(joinedProjectDetail);
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
     */
    @GetMapping("/profileImage/{loginId}")
    public ResponseEntity<byte[]> profileThumbnail(@PathVariable String loginId) {
        try {
            byte[] thumbnail = userService.profileImage(loginId);
            return ResponseEntity.ok(thumbnail);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="헤더 Notification 푸시알림 리스트 조회 API", notes="회원정보 출력")
    @ApiResponse(description = "Notification리스트 조회 성공 결과",responseCode = "200")
    @PostMapping(path = "/my-notification-list", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificationResponse> myNotificationList(@RequestParam("loginEmail") String loginEmail) {
        try {
            NotificationResponse notificationListResponse = userService.myNotificationList(loginEmail);
            return ResponseEntity.ok(notificationListResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="Notification 조회 후 count초기화 API", notes="회원정보 출력")
    @ApiResponse(description = "count초기화  결과",responseCode = "200")
    @PostMapping(path = "/init-new-notify-count", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void initNewNotifyCount(@RequestParam("loginEmail") String loginEmail) {
        try {
            userService.initNewNotifyCount(loginEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value="Notification 클릭(확인) 여부 API", notes="회원정보 출력")
    @ApiResponse(description = "클릭여부  결과",responseCode = "200")
    @PostMapping(path = "/notify-change-clicked", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void notifyChangeClicked(@RequestParam("notificationNo") Long notificationNo) {
        try {
            userService.notifyChangeClicked(notificationNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

