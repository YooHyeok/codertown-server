package io.codertown.web.entity.recruit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {
    private String sido; //시/도
    private String sigungu; //시/군/구
    private String bname; //동
    private String roadAddress; //도로명 풀주소

}
