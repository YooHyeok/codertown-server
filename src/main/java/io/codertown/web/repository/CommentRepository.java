package io.codertown.web.repository;

import io.codertown.web.entity.coggle.Coggle;
import io.codertown.web.entity.coggle.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"writer", "children"})
    @Query(value = "select c from Comment c where c.coggle = :coggle AND c.parent.id is null order by c.firstRegDate, c.id asc")
//    @Query(value = "select c from Comment c where c.coggle = :coggle order by c.parent.id asc nulls first ,  c.firstRegDate asc, c.id asc")
    List<Comment> findByCoggle(@Param("coggle") Coggle coggle);
}
