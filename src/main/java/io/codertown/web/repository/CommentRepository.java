package io.codertown.web.repository;

import io.codertown.web.entity.Coggle;
import io.codertown.web.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"user", "children"})
    @Query(value = "select c from Comment c where c.coggle = :coggle order by c.parent.id asc nulls first , c.firstRegDate asc")
    List<Comment> findByCoggle(@Param("coggle") Coggle coggle);
}
