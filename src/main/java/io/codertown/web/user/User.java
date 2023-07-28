package io.codertown.web.user;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.recruit.Recruit;
import io.codertown.web.user.payload.SignUpRequest;
import io.codertown.web.user.payload.UserEditRequest;
import io.codertown.web.userproject.UserProject;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
@ToString(exclude = {"projectUsers", "recruitUsers"})
@Entity
public class User extends BaseTimeStampEntity implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String nickname;
    private String profileIcon;
    private String password;
    private Character gender;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserStatus status; // 회원 상태(using, cancel ,freeze) - 사용중 탈퇴 정지

    @OneToMany(mappedBy = "projectUser")
    private List<UserProject> projectUsers = new ArrayList<>();

    @OneToMany(mappedBy = "recruitUser")
    private List<Recruit> recruitUsers = new ArrayList<>();

    /**
     * 회원정보 수정 - 변경감지 메소드
     * @param userEdit
     */
    public void updateUser(UserEditRequest userEdit) {
        this.email = userEdit.getChangeEmail();
        this.nickname = userEdit.getNickname();
        this.profileIcon = userEdit.getProfileIcon();
        this.password = userEdit.getPassword();
    }

    public String getRolesToString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < roles.size(); i++) {
            sb.append(roles.get(i));
            if (i < roles.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /* === DTO Entity 변환 === */
    public static User userDtoToEntity(SignUpRequest requestDto) {
        String role = requestDto.getRole() == null ? "ROLE_USER" : requestDto.getRole(); //null이면 USER정보, 아니면 그외 권한
        return User.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .profileIcon(requestDto.getProfileIcon())
                .password(requestDto.getPassword())
                .gender(requestDto.getGender())
                .roles(Collections.singletonList(role))
                .build();
    }

    /* === Security UserDetails 구현 === */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}