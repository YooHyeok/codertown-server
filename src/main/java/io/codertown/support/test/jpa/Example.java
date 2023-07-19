package io.codertown.support.test.jpa;

import javax.persistence.*;

@Entity
public class Example {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
