package io.codertown.web.repository;

import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.repository.querydsl.RecruitQuerydslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitQuerydslRepository, QuerydslPredicateExecutor<Recruit> {
    @Query("select r from Recruit r where TYPE(r) = Cokkiri")
    Page<Recruit> findByCategory(PageRequest pageRequest);
}
