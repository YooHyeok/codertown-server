package io.codertown.web.entity;

import io.codertown.web.entity.recruit.Cokkiri;

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

    private int count;

}
