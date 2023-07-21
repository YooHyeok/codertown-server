package io.codertown.web.user;

import io.codertown.support.base.CommonLoggerComponent;
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
public class UserController extends CommonLoggerComponent {
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
    @PostMapping("/signUp")
    public ResponseEntity<Boolean> signUp(@RequestBody CreateUserRequest request) {
        try {
            Boolean signUpResult = userService.signUp(request);
            return ResponseEntity.ok(signUpResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
