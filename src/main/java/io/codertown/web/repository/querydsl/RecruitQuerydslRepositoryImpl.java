package io.codertown.web.repository.querydsl;

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
    public RecruitQuerydslRepositoryImpl() {
        super(Recruit.class);
        this.queryFactory = new JPAQueryFactory(getEntityManager());
    }

    @Override
    public Page<Recruit> findByType(String dType, Pageable pageable) {
        QRecruit recruit = QRecruit.recruit;

        List<Recruit> content = queryFactory.selectFrom(recruit)
                .where(recruit.instanceOf(dTypeCondition(dType)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Recruit> countQuery = queryFactory
                .selectFrom(recruit)
                .where(recruit.instanceOf(dTypeCondition(dType)))
                .orderBy(recruit.id.desc());

        return PageableExecutionUtils.getPage(content, pageable, new LongSupplier(){
            @Override
            public long getAsLong() {
                return countQuery.fetchCount();
            }
        });
    }

    /**
     *
     * @param dType
     * @return
     */
    private static Class<? extends Recruit> dTypeCondition(String dType) {
        return dType.isEmpty() ? null : (dType.equals("Cokkiri") ? Cokkiri.class : Mammoth.class);
    }

}
