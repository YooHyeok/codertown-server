package io.codertown.web.repository;

import io.codertown.web.dto.JoinedProjectSimpleConvertDto;
import io.codertown.web.entity.project.Project;
import io.codertown.web.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT NEW io.codertown.web.dto.JoinedProjectSimpleConvertDto(p, pp) FROM Project p " +
            "LEFT JOIN fetch UserProject up ON (up.project.id = p.id) " +
            "LEFT JOIN fetch ProjectPart pp ON (pp.id = up.projectPart.id) " +
            "where up.projectUser = :loginUser")
    Page<JoinedProjectSimpleConvertDto> findJoinedProject(@Param("loginUser") User loginUser, PageRequest pageRequest);

    @Query("SELECT NEW io.codertown.web.dto.JoinedProjectSimpleConvertDto(p, pp) FROM Project p " +
            "LEFT JOIN fetch UserProject up ON (up.project.id = p.id) " +
            "LEFT JOIN fetch ProjectPart pp ON (pp.id = up.projectPart.id) " +
            "where up.projectUser = :loginUser")
    List<JoinedProjectSimpleConvertDto> findJoinedProject(@Param("loginUser") User loginUser);

}
