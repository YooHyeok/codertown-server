package io.codertown.web.recruit.cokkiri;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.codertown.web.part.Part.ProjectPart;
import io.codertown.web.recruit.Recruit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Cokkiri extends Recruit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer objectWeek; //목표 기간(주)

    @JsonIgnore
    @OneToMany(mappedBy = "cokkiri")
    private List<ProjectPart> projectParts = new ArrayList<>();
}
