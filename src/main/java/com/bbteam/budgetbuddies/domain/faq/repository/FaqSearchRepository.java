package com.bbteam.budgetbuddies.domain.faq.repository;

import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaqSearchRepository {

    Page<Faq> searchFaq(Pageable pageable, String searchCondition);
}
