package com.bbteam.budgetbuddies.domain.faq.service;

import com.bbteam.budgetbuddies.domain.faq.dto.FaqRequestDto;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqResponseDto;
import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import com.bbteam.budgetbuddies.domain.faq.repository.FaqRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FaqServiceTest {

    @Autowired
    FaqService faqService;
    @Autowired
    FaqRepository faqRepository;
    @Autowired
    UserRepository userRepository;

    static Long userId;
    static Long faqId;

    @BeforeEach
    void init() {
        User user = User.builder()
                .email("changjun157@naver.com")
                .phoneNumber("010-1234-1234")
                .name("tester1")
                .mobileCarrier("kt")
                .build();

        userRepository.save(user);
        userId = user.getId();

        Faq faq = Faq.builder()
                .title("testtitle1")
                .body("testbody")
                .user(user)
                .build();
        faqRepository.save(faq);
        faqId = faq.getId();
    }

    @Test
    void findOneFaq() {
        FaqResponseDto.FaqFindResponse response = faqService.findOneFaq(faqId);

        assertThat(response.getBody()).isEqualTo("testbody");
        assertThat(response.getTitle()).isEqualTo("testtitle1");
    }

    @Test
    void findAllWithPaging() {
        User user = userRepository.findById(userId).get();

        Faq faq1 = Faq.builder()
                .title("test1")
                .body("test1")
                .user(user)
                .build();
        faqRepository.save(faq1);

        Faq faq2 = Faq.builder()
                .title("test2")
                .body("test2")
                .user(user)
                .build();
        faqRepository.save(faq2);

        Faq faq3 = Faq.builder()
                .title("test3")
                .body("test3")
                .user(user)
                .build();
        faqRepository.save(faq3);

        Faq faq4 = Faq.builder()
                .title("test4")
                .body("test4")
                .user(user)
                .build();
        faqRepository.save(faq4);

        Faq faq5 = Faq.builder()
                .title("test5")
                .body("test5")
                .user(user)
                .build();
        faqRepository.save(faq5);

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<FaqResponseDto.FaqFindResponse> page1 = faqService.findAllWithPaging(pageRequest);
        assertThat(page1.getNumberOfElements()).isEqualTo(2);
        assertThat(page1.getTotalPages()).isEqualTo(3);
    }

    @Test
    void postFaq() {
        FaqRequestDto.FaqPostRequest dto = FaqRequestDto.FaqPostRequest.builder()
                .body("안녕하세요")
                .title("테스트입니다.")
                .build();

        FaqResponseDto.FaqPostResponse response = faqService.postFaq(dto, userId);

        Faq faq = faqRepository.findById(response.getFaqId()).get();

        assertThat(faq.getBody()).isEqualTo("안녕하세요");
        assertThat(faq.getTitle()).isEqualTo("테스트입니다.");
    }
    @Test
    void modifyFaq() {
        User user = userRepository.findById(userId).get();
        Faq faq = faqRepository.findById(faqId).get();

        FaqRequestDto.FaqModifyRequest dto = FaqRequestDto.FaqModifyRequest.builder()
                .title("modititle")
                .body("modibody")
                .build();

        faqService.modifyFaq(dto, faqId);

        assertThat(faq.getTitle()).isEqualTo("modititle");
        assertThat(faq.getBody()).isEqualTo("modibody");


    }


    @Test
    void deleteFaq() {
        faqService.deleteFaq(faqId);
        Optional<Faq> faq = faqRepository.findById(faqId);

        assertThat(faq.isEmpty()).isTrue();
    }
}