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
     *
     * @param requestDto - Client로부터 formData 형식으로 받는다.
     * @return Boolean 가입 성공시 true를 반환한다.
     */
    @PostMapping("/signUp")
    public ResponseEntity<Boolean> signUp(@RequestBody CreateUserRequestDto requestDto) {
        ResponseEntity<Boolean> result = null;
        try {
            Boolean signUpResult = userService.signUp(requestDto);
            result = new ResponseEntity<>(signUpResult, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return result;
    }

}
