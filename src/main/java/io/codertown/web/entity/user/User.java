package io.codertown.web.entity.user;

import io.codertown.support.base.BaseTimeStampEntity;
import io.codertown.web.entity.UserProject;
import io.codertown.web.entity.chat.ChatRoomUser;
import io.codertown.web.entity.coggle.Coggle;
import io.codertown.web.entity.coggle.Comment;
import io.codertown.web.entity.coggle.Notification;
import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.payload.request.SignUpRequest;
import io.codertown.web.payload.request.UserUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;

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
//@ToString(exclude = {"projectUsers", "recruitUsers", "attachFile", "roles", "chatRoomUserList", "notifications"})
@Entity
public class User extends BaseTimeStampEntity implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String nickname;
    @Column(columnDefinition = "mediumblob")
    @Nullable
    private byte[] profileUrl; //프로필 이미지 첨부파일
    private String password;
    private Character gender;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserStatusEnum status; // 회원 상태(using, cancel ,freeze) - 사용중 탈퇴 정지

    @OneToMany(mappedBy = "projectUser")
    private List<UserProject> projectUsers = new ArrayList<>();

    @OneToMany(mappedBy = "recruitUser")
    private List<Recruit> recruitUsers = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Coggle> coggleUsers = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Comment> commentUsers = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoomUser")
    private List<ChatRoomUser> chatRoomUserList = new ArrayList<>();

    @OneToMany(mappedBy = "notifyUser")
    private List<Notification> notifications = new ArrayList<>();

    private Long newMsgTotalCount;
    private Long newNotifyCount; // Notification 새글 갯수

    public void incrementNewMsgTotalCount() {
        this.newMsgTotalCount ++ ;
    };

    public void decrementNewMsgTotalCount(Long newMsgCount) {
        this.newMsgTotalCount = this.newMsgTotalCount - newMsgCount;
    };

    public void incrementNewNotifyCount() {
        this.newNotifyCount ++ ;
    };

    public void initNewNotifyCount() {
        this.newNotifyCount = 0L ;
    };

    /* Notification과의 양방향 연관관계 편의 메소드 */
    public void relatedNotificationSet(Notification notification) {
        this.notifications.add(notification);
    }

    /**
     * 회원정보 수정 - 변경감지 메소드
     * @param request
     */
    public void updateUser(UserUpdateRequest request) throws IOException {
        this.nickname = request.getNickname();
        if(request.getProfileUrl() != null) { //postman 사용으로 임시 처리
            this.profileUrl = Base64.getDecoder().decode(request.getProfileUrl().split(",")[1]);
        }
        if(!request.getPassword().equals("")) { //패스워드가 비어있지 않을경우에 초기화
            this.password = request.getPassword();
        }
    }

    /**
     * 회원정보 비활성화 - 변경감지 메소드
     * @param status
     */
    public void changeStatusAccount(UserStatusEnum status) throws IOException {
        this.status = status;
    }

    /**
     * Roles 필드 문자열 Split 함수
     * @return
     */
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
    public static User userDtoToEntity(SignUpRequest request) throws IOException {
        String role = request.getRole() == null ? "ROLE_USER" : request.getRole(); //null이면 USER정보, 아니면 그외 권한
        return User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .profileUrl(Base64.getDecoder().decode(request.getProfileUrl().split(",")[1]))
                .gender(request.getGender())
                .roles(Collections.singletonList(role))
                .status(UserStatusEnum.USING)
                .newMsgTotalCount(0L)
                .newNotifyCount(0L)
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