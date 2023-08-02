package io.codertown.web.repository;

import io.codertown.web.entity.ProjectPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectPartRepository extends JpaRepository<ProjectPart, Long> {
}
