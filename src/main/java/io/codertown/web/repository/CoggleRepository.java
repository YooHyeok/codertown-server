package io.codertown.web.repository;

import io.codertown.web.entity.coggle.Coggle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoggleRepository extends JpaRepository<Coggle, Long> {

    /**
     * 마이페이지 내 게시글, 코글 전체리스트, 코글 카테고리별 리스트에 사용된다.
     * condition으로 작성자, 제목을 임시로 설정해둠. (추후 변경 가능)
     * null에 대한 조건은 기본적으로 동등비교가 아닌 is 키워드를 사용하게 된다.
     * 파라미터가 null이 넘어오면 is null로 조건이 부여되기 때문에 아무것도 조회되지 않는다.
     * @param category
     * @param keyword
     * @param loginId
     * @param pageRequest
     * @return
     */
    @EntityGraph(attributePaths = "writer")
    @Query(
            "SELECT c FROM Coggle c WHERE (:category IS NULL OR c.category = :category) " +
            "AND (:keyword IS NULL OR (c.writer.nickname LIKE %:keyword% OR c.title LIKE %:keyword% OR c.content LIKE %:keyword%)) " +
            "AND (c.status = false) " +
            "AND (:url IS NOT 'myPost' OR (:loginId IS NULL OR c.writer.email = :loginId))" // url이 myPost가 아니면 true (break) & loginId가 null이면 true(break)
    )
    Page<Coggle> findByCategoryAndUser(@Param("category") Character category, @Param("keyword") String keyword, @Param("url") String url, @Param("loginId") String loginId, PageRequest pageRequest);
}
