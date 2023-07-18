package io.codertown.web.user;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

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
public class CreateUserRequestDto {
    private String emailId;
    private String emailAddress;
    private String fullEmail;
    private String nickname;
    private String profileIcon;
    private String password;
    private char gender;

}
