package com.bbteam.budgetbuddies.domain.faqkeyword.repository;

import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import com.bbteam.budgetbuddies.domain.faqkeyword.domain.FaqKeyword;
import com.bbteam.budgetbuddies.domain.searchkeyword.domain.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FaqKeywordRepository extends JpaRepository<FaqKeyword, Long> {
    Optional<FaqKeyword> findByFaqAndSearchKeyword(Faq faq, SearchKeyword searchKeyword);
}
