package io.codertown.web.repository.querydsl;

import io.codertown.web.entity.recruit.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface RecruitQuerydslRepository {
    @EntityGraph(attributePaths = {"cokkiri", "cokkiri.project", "cokkiri.project.projectPart"})
    Page<Recruit> findByType(String dType, Pageable pageable);
}
