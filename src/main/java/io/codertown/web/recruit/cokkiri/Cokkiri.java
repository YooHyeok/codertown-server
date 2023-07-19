package io.codertown.web.recruit.cokkiri;

import io.codertown.web.recruit.Recruit;
import lombok.*;
import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cokkiri extends Recruit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer objectWeek; //목표 기간(주)
}
