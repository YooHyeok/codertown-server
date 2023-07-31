package io.codertown.web.entity.recruit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.codertown.web.entity.ProjectPart;
import io.codertown.web.payload.CokkiriSaveRequest;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@SuperBuilder
@NoArgsConstructor
@Entity
public class Cokkiri extends Recruit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer objectWeek; //목표 기간(주)

    @JsonIgnore
    @OneToMany(mappedBy = "cokkiri")
    private List<ProjectPart> projectParts = new ArrayList<>();

    public static Cokkiri createCokkiri(CokkiriSaveRequest requestDto) {
        Cokkiri build = Cokkiri.builder()
                .title(requestDto.getCokkiriTitle())
                .content(requestDto.getContent())
                .recruitUser(requestDto.getUser())
                .objectWeek(requestDto.getObjectWeek())
                .build();
        return build;
    }
}
