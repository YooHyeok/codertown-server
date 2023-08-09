package io.codertown.web.entity.recruit;

import io.codertown.web.payload.request.MammothSaveRequest;
import io.codertown.web.payload.request.MammothUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@ToString(callSuper = true)
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Mammoth extends Recruit {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location; // 위치

    /**
     * 저장을 위한 Mammoth 생성 메소드
     * @param request
     * @return
     */
    public static Mammoth createMammoth(MammothSaveRequest request) {
        Mammoth build = Mammoth.builder()
                .recruitUser(request.getUser())
                .title(request.getTitle())
                .content(request.getContent())
                .location(request.getLocation())
                .build();
        return build;
    }

    /**
     * Mammoth 변경감지 메소드 <br/>
     * updateRecruit()메소드를 호출하여 부모엔티티인 Recruit 엔티티 변경감지 시도
     * @param request
     */
    public void updateMammoth(MammothUpdateRequest request) {
        updateRecruit(request.getTitle(), request.getLink(), request.getContent());
        this.location = request.getLocation();
    }
}
