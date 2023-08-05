package io.codertown.web.repository;

import io.codertown.web.entity.Coggle;
import io.codertown.web.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoggleRepository extends JpaRepository<Coggle, Long> {

    /**
     * 마이페이지 내 게시글, 코글 전체리스트, 코글 카테고리별 리스트에 사용된다.
     * null에 대한 조건은 기본적으로 동등비교가 아닌 is 키워드를 사용하게 된다.
     * 파라미터가 null이 넘어오면 is null로 조건이 부여되기 때문에 아무것도 조회되지 않는다.
     * @param category
     * @param user
     * @param pageRequest
     * @return
     */
    @Query("SELECT c FROM Coggle c WHERE (:category IS NULL OR c.category = :category) AND (:user IS NULL OR c.user = :user)")
    Page<Coggle> findByCategoryAndUser(@Param("category") Character category, @Param("user") User user, PageRequest pageRequest);
}
