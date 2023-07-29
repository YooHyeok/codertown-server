package io.codertown.web.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * *****************************************************<p>
 * 패키지:io.codertown.user<p>
 * 파일 : CreateUserRequestDto.java<p>
 * 프로그램 설명 : 회원 가입 요청시 폼데이터를 넘겨받아 엔티티와 매핑하는 DTO 클래스<p>
 * 연관테이블 : user<p>
 * 담당 : 유재혁<p>
 * *****************************************************<p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SignUpRequest {
    private String email;
    private String password;
    private String nickname;
    private String profileIcon;
    private Character gender;
    private String role;

    public void setNickname(String email) {
        this.nickname = email; //중복이 아니면 그대로 저장

    }
}
