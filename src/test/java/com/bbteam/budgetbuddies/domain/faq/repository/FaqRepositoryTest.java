package com.bbteam.budgetbuddies.domain.faq.repository;

import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import com.bbteam.budgetbuddies.domain.faqkeyword.domain.FaqKeyword;
import com.bbteam.budgetbuddies.domain.faqkeyword.repository.FaqKeywordRepository;
import com.bbteam.budgetbuddies.domain.searchkeyword.domain.SearchKeyword;
import com.bbteam.budgetbuddies.domain.searchkeyword.repository.SearchKeywordRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class FaqRepositoryTest {
    @Autowired
    FaqRepository faqRepository;
    @Autowired
    FaqKeywordRepository faqKeywordRepository;
    @Autowired
    SearchKeywordRepository searchKeywordRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    void searchTest() {

        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .mobileCarrier("skt")
                .build();
        userRepository.save(user1);

        Faq faq1 = Faq.builder()
                .user(user1)
                .title("test1")
                .body("test1")
                .build();
        faqRepository.save(faq1);

        Faq faq2 = Faq.builder()
                .user(user1)
                .title("test2")
                .body("test2")
                .build();
        faqRepository.save(faq2);

        SearchKeyword keyword1 = SearchKeyword.builder()
                .keyword("러닝")
                .build();
        searchKeywordRepository.save(keyword1);

        FaqKeyword faqKeyword = FaqKeyword.builder()
                .searchKeyword(keyword1)
                .faq(faq1)
                .build();
        faqKeywordRepository.save(faqKeyword);

        SearchKeyword keyword2 = SearchKeyword.builder()
                .keyword("헬스")
                .build();
        searchKeywordRepository.save(keyword2);

        FaqKeyword faqKeyword2 = FaqKeyword.builder()
                .searchKeyword(keyword2)
                .faq(faq1)
                .build();
        faqKeywordRepository.save(faqKeyword2);

        FaqKeyword faqKeyword3 = FaqKeyword.builder()
                .searchKeyword(keyword1)
                .faq(faq2)
                .build();
        faqKeywordRepository.save(faqKeyword3);

        PageRequest pageRequest = PageRequest.of(0, 5);

        Page<Faq> result1 = faqRepository.searchFaq(pageRequest, "러닝");

        Page<Faq> result2 = faqRepository.searchFaq(pageRequest, "헬스");

        assertThat(result1.getNumberOfElements()).isEqualTo(2);
        assertThat(result2.getNumberOfElements()).isEqualTo(1);

    }


}