package io.codertown.web.repository;

import io.codertown.web.dto.JoinedProjectDto;
import io.codertown.web.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT NEW io.codertown.web.dto.JoinedProjectDto(p, up) FROM Project p LEFT JOIN fetch UserProject up ON (up.id = p.id) LEFT JOIN fetch ProjectPart pp ON (pp.id = up.id) where up.projectUser.email = 'webdevyoo@gmail.com'")
    List<JoinedProjectDto> findJoinedProjectTest1();

    @Query("SELECT p, up FROM Project p " +
            "LEFT JOIN fetch UserProject up ON (up.id = p.id) " +
            "LEFT JOIN fetch ProjectPart pp ON (pp.id = up.id) " +
            "where up.projectUser.email = 'webdevyoo@gmail.com'")
    List<Object[]> findJoinedProjectTest2();

    @Query("SELECT pt AS pt, up AS up FROM Project pt " +
            "LEFT JOIN fetch UserProject as up ON (up.id = pt.id) " +
            "LEFT JOIN fetch ProjectPart as pp ON (pp.id = up.id) " +
            "where up.projectUser.email = 'webdevyoo@gmail.com'")
    List<Tuple> findJoinedProjectTest3();

}
