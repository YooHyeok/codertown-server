package io.codertown.web.repository;

import io.codertown.web.dto.JoinedProjectSimpleConvertDto;
import io.codertown.web.dto.JoinedProjectTestDto;
import io.codertown.web.entity.project.Project;
import io.codertown.web.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

//    @Query("SELECT NEW io.codertown.web.dto.JoinedProjectDto(p, up) FROM Project p LEFT JOIN fetch UserProject up ON (up.id = p.id) LEFT JOIN fetch ProjectPart pp ON (pp.id = up.id) where up.projectUser.email = 'webdevyoo@gmail.com'")
//    List<JoinedProjectDto> findJoinedProjectTest1();

    @Query("SELECT NEW io.codertown.web.dto.JoinedProjectTestDto(p, up, pp) FROM Project p " +
            "LEFT JOIN fetch UserProject up ON (up.project.id = p.id) " +
            "LEFT JOIN fetch ProjectPart pp ON (pp.id = up.projectPart.id) " +
            "where up.projectUser.email = 'webdevyoo@gmail.com'")
    List<JoinedProjectTestDto> findJoinedProjectTest2();

    /*@Query("SELECT p, up, pp FROM Project p " +
            "LEFT JOIN fetch UserProject up ON (up.project.id = p.id) " +
            "LEFT JOIN fetch ProjectPart pp ON (pp.id = up.projectPart.id) " +
            "where up.projectUser.email = 'webdevyoo@gmail.com'")
    List<Object[]> findJoinedProjectTest2();*/

/*
    @Query("SELECT pt AS pt, up AS up, pp AS pp FROM Project pt " +
            "LEFT JOIN fetch UserProject as up ON (up.id = pt.id) " +
            "LEFT JOIN fetch ProjectPart as pp ON (pp.id = up.id) " +
            "where up.projectUser.email = 'webdevyoo@gmail.com'")
    List<Tuple> findJoinedProjectTest3();
*/


    /* 객체 탐색으로 JOIN하면 DTO변환 혹은 Tuple, Object[] 조회가 불가능해진다. */
    /*@Query("SELECT p as project, pp.part.id as participationPartNo, pp.part as participationPartName FROM Project p " +
            "LEFT JOIN FETCH p.projects up " +
            "LEFT JOIN FETCH up.projectPart pp " +
            "WHERE up.projectUser = :loginUser")
    List<Map<String, Object>> findJoinedProject(@Param("loginUser") User loginUser);*/


    @Query("SELECT NEW io.codertown.web.dto.JoinedProjectSimpleConvertDto(p, pp) FROM Project p " +
            "LEFT JOIN fetch UserProject up ON (up.project.id = p.id) " +
            "LEFT JOIN fetch ProjectPart pp ON (pp.id = up.projectPart.id) " +
            "where up.projectUser.email = 'webdevyoo@gmail.com'")
    /*@Query("SELECT NEW io.codertown.web.dto.JoinedProjectDto(r, p, pp) FROM Recruit r " +
        "LEFT JOIN fetch Project p ON (p.id = r.id) " +
        "LEFT JOIN fetch UserProject up ON (up.project.id = p.id) " +
        "LEFT JOIN fetch ProjectPart pp ON (pp.id = up.projectPart.id) " +
        "where up.projectUser.email = 'webdevyoo@gmail.com'")*/
    Page<JoinedProjectSimpleConvertDto> findJoinedProject(@Param("loginUser") User loginUser, PageRequest pageRequest);


}
