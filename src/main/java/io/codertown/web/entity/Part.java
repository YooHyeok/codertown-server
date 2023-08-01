package io.codertown.web.entity;

import javax.persistence.*;

@Entity
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PART_NO")
    private Long id;

    private String partName;
}
