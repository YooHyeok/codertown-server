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
    public Page<Recruit> findByType(String dType, Pageable pageable, String keyword) {
        QRecruit recruit = QRecruit.recruit;

        /**
         *  and연산자로 묶기위한 최상위 BooleanExpression
         *  전체조회시 dType이 null일 경우 무시하기위함.
         */
        BooleanExpression  baseCondition = recruit.status.eq(false);
        /* dType 조건*/
        BooleanExpression dTypeCondition =
                dType.equals("Cokkiri") ?
                        recruit.instanceOf(Cokkiri.class) : (dType.equals("Mammoth") ? recruit.instanceOf(Mammoth.class) : null);
        /* 검색 키워드 조건 */
        BooleanExpression searchByKeword = (keyword == null)
                ? null : (recruit.title.like("%" + keyword + "%").or(recruit.recruitUser.nickname.like("%" + keyword + "%"))
                .or((recruit.content.notLike("%<p><img src=\"%" + keyword + "%</img><p>%").and(recruit.content.like("%" + keyword + "%"))))
        );
        if (dTypeCondition != null) {
            baseCondition = baseCondition.and(dTypeCondition);
        }

        if (searchByKeword != null) {
            baseCondition = baseCondition.and(searchByKeword);
        }

        /* 실제 QueryDSL 적용 */
        List<Recruit> content = queryFactory.selectFrom(recruit)
                .leftJoin(recruit.recruitUser).fetchJoin()
//                .where(dTypeCondition, recruit.status.eq(false) , searchByLeword)
                .where(baseCondition)
                .orderBy(recruit.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Recruit> countQuery = queryFactory
                .selectFrom(recruit)
                .where(baseCondition)
                .orderBy(recruit.id.desc());
        return PageableExecutionUtils.getPage(content, pageable, new LongSupplier(){
            @Override
            public long getAsLong() {
                return countQuery.fetchCount();
            }
        });
    }
}
