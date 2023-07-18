package io.codertown.web.user;

import lombok.Data;

/**
 * 회원 가입 후 가입 정보를 반환하는 DTO클래스이다.
 * 반환할 값 목록에 대한 확장 가능성
 */
@Data
public class CreateUserResponseDto {
    private String id;

    public CreateUserResponseDto(String id) {
        this.id = id;
    }
}
