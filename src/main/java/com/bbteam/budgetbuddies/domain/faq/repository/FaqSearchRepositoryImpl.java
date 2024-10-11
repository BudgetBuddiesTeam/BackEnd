package com.bbteam.budgetbuddies.domain.faq.repository;

import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.bbteam.budgetbuddies.domain.faq.entity.QFaq.*;
import static com.bbteam.budgetbuddies.domain.faqkeyword.domain.QFaqKeyword.*;

public class FaqSearchRepositoryImpl implements FaqSearchRepository{

    private final JPAQueryFactory queryFactory;

    public FaqSearchRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Faq> searchFaq(Pageable pageable, String searchCondition) {
        List<Faq> result = queryFactory.select(faq)
                .from(faq)
                .where(faq.in(
                        JPAExpressions
                                .select(faqKeyword.faq)
                                .from(faqKeyword)
                                .where(keywordMatch(searchCondition))
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(faq.count())
                .from(faq)
                .where(faq.in(
                        JPAExpressions
                                .select(faqKeyword.faq)
                                .from(faqKeyword)
                                .where(keywordMatch(searchCondition))
                ))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);

    }

    private BooleanExpression keywordMatch(String searchCondition) {
        return searchCondition != null ? faqKeyword.searchKeyword.keyword.eq(searchCondition) : null;
    }

}

