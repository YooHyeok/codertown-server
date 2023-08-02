package io.codertown.web.repository;

import io.codertown.web.entity.Coggle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoggleRepository extends JpaRepository<Coggle, Long> {
}
