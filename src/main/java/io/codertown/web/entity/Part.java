package io.codertown.web.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PART_NO")
    private Long id;

    private String partName;
}
