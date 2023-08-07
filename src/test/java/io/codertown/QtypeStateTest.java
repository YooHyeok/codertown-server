package io.codertown;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.codertown.web.entity.recruit.QRecruit;
import io.codertown.web.entity.recruit.Recruit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 *
 * queryDSL은 결과적으로 JPQL로 빌더한다.
 * application.yml에 use_sql_comments : true를 추가하면 JPQ의 힌트가 나간다 (주석형태)
 *
 */
@SpringBootTest
@Transactional
//@Commit
public class QtypeStateTest {
    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
    }
    @Test
    public void qTypeDeclaration() {
        QRecruit recruit = QRecruit.recruit; //static Import로 사용 가능
        Recruit findRecruit = queryFactory
                .select(recruit)
                .from(recruit)
                .where(recruit.id.eq(2L)) //querydsl은 preparedStatement에의해 자동으로 파라미터 바인딩 해준다.
                .fetchOne();
        System.out.println("findRecruit = " + findRecruit);
    }
}

