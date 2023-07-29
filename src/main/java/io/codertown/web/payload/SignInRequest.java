package io.codertown.web.payload;

import lombok.Data;
/**
 * *****************************************************<p>
 * 패키지:io.codertown.user<p>
 * 파일 : CreateUserResponseDto.java<p>
 * 프로그램 설명 : 회원 가입 후 가입 정보를 반환하는 DTO클래스<p>
 * 비고 : 반환할 값 목록에 대한 확장 가능성 <p>
 * 연관테이블 : user<p>
 * 담당 : 유재혁<p>
 * *****************************************************<p>
 */
@Data
public class SignInRequest {
    private String email;
    private String password;

}
