package io.codertown.web.user;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.recruit.Recruit;
import io.codertown.web.userproject.UserProject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************************<p>
 * user 테이블 엔터티 클래스<p>
 * 프로그램 설명 : 회원 테이블<p>
 * 담당 : 유재혁<p>
 * *****************************************************<p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseTimeStampEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long id;
    @Column(unique = true, updatable = false, nullable = false)
    private String email;
    private String nickname;
    private String profileIcon;
    private String password;
    private char gender;

    @Enumerated(EnumType.STRING)
    private UserStatus status; // 회원 상태(using, cancel ,freeze) - 사용중 탈퇴 정지

    @OneToMany(mappedBy = "projectUser")
    private List<UserProject> projectUsers = new ArrayList<>();

    @OneToMany(mappedBy = "recruitUser")
    private List<Recruit> recruitUsers = new ArrayList<>();

}