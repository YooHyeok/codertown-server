package io.codertown.web.repository;

import io.codertown.web.entity.recruit.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {
}
