package io.codertown.web.user;

import lombok.*;

/**
 * *****************************************************<p>
 * 패키지:io.codertown.user<p>
 * 파일 : CreateUserRequestDto.java<p>
 * 프로그램 설명 : 회원 가입 요청시 폼데이터를 넘겨받아 엔티티와 매핑하는 DTO 클래스<p>
 * 연관테이블 : user<p>
 * 담당 : 유재혁<p>
 * *****************************************************<p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String profileIcon;
    private Character gender;

    public void setNickname(String email) {
        this.nickname = email.split("@")[0];
    }

    public String getNickname() { //커맨드 객체 파라미터 받을때 호출된다.
        setNickname(getEmail());
        return nickname;
    }

}
