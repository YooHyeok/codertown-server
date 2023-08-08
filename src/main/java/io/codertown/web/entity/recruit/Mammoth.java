package io.codertown.web.entity.recruit;

import io.codertown.web.service.MammothSaveRequest;
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

    public static Mammoth createMammoth(MammothSaveRequest request) {

        Mammoth build = Mammoth.builder()
                .recruitUser(request.getUser())
                .title(request.getTitle())
                .content(request.getContent())
                .location(request.getLocation())
                .build();
        return build;
    }
}
