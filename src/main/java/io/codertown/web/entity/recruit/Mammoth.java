package io.codertown.web.entity.recruit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Mammoth extends Recruit {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location; // 위치
}
