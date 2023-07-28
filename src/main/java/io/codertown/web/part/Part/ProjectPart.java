package io.codertown.web.part.Part;

import io.codertown.web.recruit.cokkiri.Cokkiri;

import javax.persistence.*;

@Entity
public class ProjectPart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PROJECT_PART_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COKKIRI_ID")
    private Cokkiri cokkiri;

}
