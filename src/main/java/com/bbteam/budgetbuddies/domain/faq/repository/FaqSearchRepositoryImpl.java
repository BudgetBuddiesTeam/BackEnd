package com.bbteam.budgetbuddies.domain.faq.repository;

import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import com.bbteam.budgetbuddies.domain.searchkeyword.domain.QSearchKeyword;
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
import static com.bbteam.budgetbuddies.domain.searchkeyword.domain.QSearchKeyword.*;

public class FaqSearchRepositoryImpl implements FaqSearchRepository{

    private final JPAQueryFactory queryFactory;

    public FaqSearchRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Faq> searchFaq(Pageable pageable, String searchCondition) {
        List<Faq> result = queryFactory.select(faq)
                .from(faq)
                .where(faq.id.in(
                        JPAExpressions
                                .select(faqKeyword.faq.id)
                                .from(faqKeyword)
                                .join(searchKeyword).on(keywordMatch(searchCondition))
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(faq.count())
                .from(faq)
                .where(faq.id.in(
                        JPAExpressions
                                .select(faqKeyword.faq.id)
                                .from(faqKeyword)
                                .join(searchKeyword).on(keywordMatch(searchCondition))
                ))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);

    }

    private BooleanExpression keywordMatch(String searchCondition) {
        return searchCondition != null ? searchKeyword.keyword.contains(searchCondition) : null;
    }

}

