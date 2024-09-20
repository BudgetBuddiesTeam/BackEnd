package com.bbteam.budgetbuddies.domain.notice.service;

import com.bbteam.budgetbuddies.domain.notice.dto.NoticeRequestDto;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeResponseDto;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NoticeServiceTest {

    @Autowired
    NoticeService noticeService;
    @Autowired
    UserRepository userRepository;

   @Test
    void save() {
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        NoticeRequestDto dto = NoticeRequestDto.builder()
                .body("바바이")
                .title("헬로우")
                .build();
        NoticeResponseDto result = noticeService.save(dto, user1.getId());

        Assertions.assertThat(result.getUserName()).isEqualTo(user1.getName());
        Assertions.assertThat(result.getTitle()).isEqualTo("헬로우");
        Assertions.assertThat(result.getBody()).isEqualTo("바바이");
    }

    @Test
    void findOne() {
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        NoticeRequestDto dto = NoticeRequestDto.builder()
                .body("바바이")
                .title("헬로우")
                .build();
        NoticeResponseDto result = noticeService.save(dto, user1.getId());

        NoticeResponseDto one = noticeService.findOne(result.getNoticeId());

        Assertions.assertThat(result.getNoticeId()).isEqualTo(one.getNoticeId());
        Assertions.assertThat(result.getUserName()).isEqualTo(one.getUserName());
    }

    @Test
    void findAll() {
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        NoticeRequestDto dto = NoticeRequestDto.builder()
                .body("바바이")
                .title("헬로우")
                .build();
        NoticeResponseDto result = noticeService.save(dto, user1.getId());
        NoticeRequestDto dto2 = NoticeRequestDto.builder()
                .body("바바이")
                .title("헬로우")
                .build();
        NoticeResponseDto result2 = noticeService.save(dto2, user1.getId());
        NoticeRequestDto dto3 = NoticeRequestDto.builder()
                .body("바바이")
                .title("헬로우")
                .build();
        NoticeResponseDto result3 = noticeService.save(dto3, user1.getId());
        NoticeRequestDto dto4 = NoticeRequestDto.builder()
                .body("바바이")
                .title("헬로우")
                .build();
        NoticeResponseDto result4 = noticeService.save(dto4, user1.getId());

        PageRequest request1 = PageRequest.of(0, 2);
        Page<NoticeResponseDto> list1 = noticeService.findAll(request1);
        Assertions.assertThat(list1.getNumberOfElements()).isEqualTo(2);

        PageRequest request2 = PageRequest.of(1, 2);
        Page<NoticeResponseDto> list2 = noticeService.findAll(request2);
        Assertions.assertThat(list2.getNumberOfElements()).isEqualTo(2);

        PageRequest request3 = PageRequest.of(2, 2);
        Page<NoticeResponseDto> list3 = noticeService.findAll(request3);
        Assertions.assertThat(list3.getNumberOfElements()).isEqualTo(0);
    }

    @Test
    void update() {
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        NoticeRequestDto dto = NoticeRequestDto.builder()
                .body("바바이")
                .title("헬로우")
                .build();
        NoticeResponseDto result = noticeService.save(dto, user1.getId());

        NoticeRequestDto updateDto = NoticeRequestDto.builder()
                .body("좋아요")
                .title("아니에요!")
                .build();
        NoticeResponseDto update = noticeService.update(result.getNoticeId(), updateDto);

        Assertions.assertThat(update.getNoticeId()).isEqualTo(result.getNoticeId());
        Assertions.assertThat(update.getBody()).isEqualTo("좋아요");
        Assertions.assertThat(update.getTitle()).isEqualTo("아니에요!");
    }

    @Test
    void delete() {
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        NoticeRequestDto dto = NoticeRequestDto.builder()
                .body("바바이")
                .title("헬로우")
                .build();
        NoticeResponseDto result = noticeService.save(dto, user1.getId());

        noticeService.delete(result.getNoticeId());

        assertThrows(NoSuchElementException.class, () -> noticeService.findOne(result.getNoticeId()));
    }


}