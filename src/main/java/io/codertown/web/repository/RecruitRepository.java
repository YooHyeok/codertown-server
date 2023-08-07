package io.codertown.web.repository;

import io.codertown.web.entity.recruit.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    @Query("select r from Recruit r where TYPE(r) = Cokkiri")
    Page<Recruit> findByCategory(PageRequest pageRequest);
}
