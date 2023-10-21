package io.codertown.web.entity.recruit;

import io.codertown.web.payload.request.MammothSaveRequest;
import io.codertown.web.payload.request.MammothUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embedded;
import javax.persistence.Entity;

@ToString(callSuper = true)
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Mammoth extends Recruit {

//    private String location; // 위치
    @Embedded
    private Location location; // 위치

    /**
     * 저장을 위한 Mammoth 생성 메소드
     * @param request
     * @return
     */
    public static Mammoth createMammoth(MammothSaveRequest request) {
        Location location = Location.builder()
                .sido(request.getSido())
                .sigungu(request.getSigungu())
                .dong(request.getDong())
                .fullLocation(request.getFullLocation())
                .build();
        Mammoth build = Mammoth.builder()
                .recruitUser(request.getUser())
                .title(request.getTitle())
                .link(request.getLink())
                .views(0L)
                .content(request.getContent())
                .location(location)
                .status(false)
                .build();
        return build;
    }

    /**
     * Mammoth 수정 변경감지 메소드 <br/>
     * updateRecruit()메소드를 호출하여 부모엔티티인 Recruit 엔티티 변경감지 시도
     * @param request
     */
    public void updateMammoth(MammothUpdateRequest request) {
        updateRecruit(request.getTitle(), request.getLink(), request.getContent());
        Location location = Location.builder()
                .sido(request.getSido())
                .sigungu(request.getSigungu())
                .dong(request.getDong())
                .fullLocation(request.getFullLocation())
                .build();
//        this.location = request.getLocation();
        this.location = location;
    }
}
