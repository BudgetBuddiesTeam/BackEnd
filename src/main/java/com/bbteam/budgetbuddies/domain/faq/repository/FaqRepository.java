package com.bbteam.budgetbuddies.domain.faq.repository;

import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long>, FaqSearchRepository {


}
