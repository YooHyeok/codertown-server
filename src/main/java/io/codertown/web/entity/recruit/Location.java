package io.codertown.web.entity.recruit;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {
    private String sido; //시/도
    private String sigungu; //시/군/구
    private String dong; //동
    private String fullLocation; //도로명 풀주소

}
