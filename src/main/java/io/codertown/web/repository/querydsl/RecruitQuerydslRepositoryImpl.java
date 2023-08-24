package io.codertown.web.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.entity.recruit.Mammoth;
import io.codertown.web.entity.recruit.QRecruit;
import io.codertown.web.entity.recruit.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.function.LongSupplier;

public class RecruitQuerydslRepositoryImpl extends QuerydslRepositorySupport implements RecruitQuerydslRepository {

    /**
     * 의존성 주입
     * QuerydslRepositorySupport에 대한 주입 추가 (super)
     * -> EntityManager와 querydsl 유틸리티를 함께 사용할 수 있다.
     */
    private final JPAQueryFactory queryFactory;
    public RecruitQuerydslRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Recruit.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Recruit> findByType(String dType, Pageable pageable) {
        QRecruit recruit = QRecruit.recruit;
        /*BooleanExpression dTypeCondition =
                StringUtils.hasText(dType) ?
                        (dType.equals("Cokkiri") ?
                                recruit.instanceOf(Cokkiri.class) : dType.equals("Mammoth") ? recruit.instanceOf(Mammoth.class) : null)
                : null;*/
        BooleanExpression dTypeCondition =
                        dType.equals("Cokkiri") ?
                                recruit.instanceOf(Cokkiri.class) : (dType.equals("Mammoth") ? recruit.instanceOf(Mammoth.class) : null);
        List<Recruit> content = queryFactory.selectFrom(recruit)
                .leftJoin(recruit.recruitUser).fetchJoin()
                .where(dTypeCondition)
                .orderBy(recruit.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Recruit> countQuery = queryFactory
                .selectFrom(recruit)
                .where(dTypeCondition)
                .orderBy(recruit.id.desc());
        return PageableExecutionUtils.getPage(content, pageable, new LongSupplier(){
            @Override
            public long getAsLong() {
                return countQuery.fetchCount();
            }
        });
    }
}
