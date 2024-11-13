package com.bbteam.budgetbuddies.domain.searchkeyword.repository;

import com.bbteam.budgetbuddies.domain.searchkeyword.domain.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
}
