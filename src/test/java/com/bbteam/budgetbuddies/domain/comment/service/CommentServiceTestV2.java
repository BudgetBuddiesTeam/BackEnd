package com.bbteam.budgetbuddies.domain.comment.service;

import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.repository.DiscountInfoRepository;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.repository.SupportInfoRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * comment service는 다음과 같은 기능을 제공해야한다.
 * 1. comment 저장 기능
 * 2. 특정 게시글에 따른 comment return
 * 현재 게시글의 종류는 2가지로 각각 할인정보, 지원정보이다.
 * 즉, 할인정보, 지원정보 ID가 들어오면 해당 게시글에 대한 댓글 정보를 다 가지고 올 수 있어야한다.
 * 아마 관리 측면에선 댓글 삭제 기능도 필요할 것이다.
 * 3. 특정 userid로 댓글 찾는 기능
 *  얘는 게시글 ID랑 제목 정도 같이???
 * 4. 특정 게시글 id로 댓글 찾는 기능
 */


/*
    테스트마다 테스트케이스가 다를 수 있어서 공통로직으로 처리하지 않아 매우 깁니다...
 */
@SpringBootTest
@Transactional
class CommentServiceTestV2 {

    @Qualifier("discountCommentService")
    @Autowired
    CommentService discountCommentService;
    
    @Qualifier("supportCommentService")
    @Autowired
    CommentService supportCommentService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    DiscountInfoRepository discountInfoRepository;
    @Autowired
    SupportInfoRepository supportInfoRepository;
    @Autowired
    EntityManager em;

    @Test
    public void saveDiscountInfoCommentTest(){
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        DiscountInfo sale1 = DiscountInfo.builder().title("무신사 할인")
                .anonymousNumber(0)
                .build();
        discountInfoRepository.save(sale1);

        CommentRequestDto.DiscountInfoCommentDto dto1 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale1.getId())
                .content("굿")
                .build();

        discountCommentService.saveComment(user1.getId(), dto1);
        em.flush();

        List<CommentResponseDto.DiscountInfoCommentDto> returnDto = discountCommentService.findByInfo(sale1.getId());

        Assertions.assertThat(returnDto.size()).isEqualTo(1);
        Assertions.assertThat(returnDto.get(0).getDiscountInfoId()).isEqualTo(sale1.getId());
        Assertions.assertThat(returnDto.get(0).getUserId()).isEqualTo(user1.getId());
        Assertions.assertThat(returnDto.get(0).getContent()).isEqualTo("굿");

    }

    @Test
    public void saveDiscountInfoCommentTest2(){
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("tester2")
                .email("12345")
                .age(7)
                .phoneNumber("1234567")
                .build();
        userRepository.save(user2);

        DiscountInfo sale1 = DiscountInfo.builder().title("무신사 할인")
                .anonymousNumber(0)
                .build();
        discountInfoRepository.save(sale1);
        DiscountInfo sale2 = DiscountInfo.builder().title("핫트랙스 할인")
                .anonymousNumber(0)
                .build();
        discountInfoRepository.save(sale2);


        CommentRequestDto.DiscountInfoCommentDto dto1 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale1.getId())
                .content("굿")
                .build();

        CommentRequestDto.DiscountInfoCommentDto dto2 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale1.getId())
                .content("좋아요")
                .build();
        CommentRequestDto.DiscountInfoCommentDto dto3 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale2.getId())
                .content("유용해요!")
                .build();

        discountCommentService.saveComment(user1.getId(), dto1);
        discountCommentService.saveComment(user2.getId(), dto2);
        discountCommentService.saveComment(user1.getId(), dto3);

        em.flush();

        List<CommentResponseDto.DiscountInfoCommentDto> returnDto = discountCommentService.findByInfo(sale1.getId());
        List<CommentResponseDto.DiscountInfoCommentDto> returnDto2 = discountCommentService.findByInfo(sale2.getId());
        Assertions.assertThat(returnDto.size()).isEqualTo(2);
        Assertions.assertThat(returnDto.get(0).getDiscountInfoId()).isEqualTo(sale1.getId());
        Assertions.assertThat(returnDto.get(0).getUserId()).isEqualTo(user1.getId());
        Assertions.assertThat(returnDto.get(0).getContent()).isEqualTo("굿");
        Assertions.assertThat(returnDto.get(1).getDiscountInfoId()).isEqualTo(sale1.getId());
        Assertions.assertThat(returnDto.get(1).getUserId()).isEqualTo(user2.getId());
        Assertions.assertThat(returnDto.get(1).getContent()).isEqualTo("좋아요");

        Assertions.assertThat(returnDto2.size()).isEqualTo(1);
        Assertions.assertThat(returnDto2.get(0).getDiscountInfoId()).isEqualTo(sale2.getId());
        Assertions.assertThat(returnDto2.get(0).getUserId()).isEqualTo(user1.getId());
        Assertions.assertThat(returnDto2.get(0).getContent()).isEqualTo("유용해요!");

    }

    @Test
    void DiscountAnonymousCommentTest(){
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("tester2")
                .email("12345")
                .age(7)
                .phoneNumber("1234567")
                .build();

        User user3 = User.builder()
                .name("tester3")
                .email("1234553")
                .age(9)
                .phoneNumber("1232134567")
                .build();
        userRepository.save(user2);
        userRepository.save(user3);

        DiscountInfo sale1 = DiscountInfo.builder().anonymousNumber(0).title("무신사 할인").build();
        discountInfoRepository.save(sale1);
        DiscountInfo sale2 = DiscountInfo.builder().anonymousNumber(0).title("핫트랙스 할인").build();
        discountInfoRepository.save(sale2);


        CommentRequestDto.DiscountInfoCommentDto dto1 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale1.getId())
                .content("굿")
                .build();

        CommentRequestDto.DiscountInfoCommentDto dto2 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale1.getId())
                .content("좋아요")
                .build();
        CommentRequestDto.DiscountInfoCommentDto dto3 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale2.getId())
                .content("유용해요!")
                .build();
        CommentRequestDto.DiscountInfoCommentDto dto4 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale1.getId())
                .content("구웃!")
                .build();

        discountCommentService.saveComment(user1.getId(), dto1);
        discountCommentService.saveComment(user2.getId(), dto2);
        discountCommentService.saveComment(user1.getId(), dto3);

        discountCommentService.saveComment(user1.getId(), dto4);
        discountCommentService.saveComment(user3.getId(), dto4);

        em.flush();

        List<CommentResponseDto.DiscountInfoCommentDto> result = discountCommentService.findByInfo(sale1.getId());
        Integer test1 = result.get(0).getAnonymousNumber();
        Integer test2 = result.get(1).getAnonymousNumber();
        Integer test3 = result.get(2).getAnonymousNumber();
        Integer test4 = result.get(3).getAnonymousNumber();

        Assertions.assertThat(test1).isEqualTo(1);
        Assertions.assertThat(test2).isEqualTo(2);
        Assertions.assertThat(test3).isEqualTo(1);
        Assertions.assertThat(test4).isEqualTo(3);


    }

    @Test
    public void saveSupportInfoCommentTest2(){
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("tester2")
                .email("12345")
                .age(7)
                .phoneNumber("1234567")
                .build();
        userRepository.save(user2);

        SupportInfo info1 = SupportInfo.builder().anonymousNumber(0).title("국가장학금 신청").build();
        supportInfoRepository.save(info1);
        SupportInfo info2 = SupportInfo.builder().anonymousNumber(0).title("봉사활동").build();
        supportInfoRepository.save(info2);


        CommentRequestDto.SupportInfoCommentDto dto1 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("굿")
                .build();

        CommentRequestDto.SupportInfoCommentDto dto2 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("좋아요")
                .build();
        CommentRequestDto.SupportInfoCommentDto dto3 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info2.getId())
                .content("유용해요!")
                .build();

        supportCommentService.saveComment(user1.getId(), dto1);
        supportCommentService.saveComment(user2.getId(), dto2);
         supportCommentService.saveComment(user1.getId(), dto3);

        em.flush();

        List<CommentResponseDto.SupportInfoCommentDto> returnDto = supportCommentService.findByInfo(info1.getId());
        List<CommentResponseDto.SupportInfoCommentDto> returnDto2 = supportCommentService.findByInfo(info2.getId());
        Assertions.assertThat(returnDto.size()).isEqualTo(2);
        Assertions.assertThat(returnDto.get(0).getSupportInfoId()).isEqualTo(info1.getId());
        Assertions.assertThat(returnDto.get(0).getUserId()).isEqualTo(user1.getId());
        Assertions.assertThat(returnDto.get(0).getContent()).isEqualTo("굿");
        Assertions.assertThat(returnDto.get(1).getSupportInfoId()).isEqualTo(info1.getId());
        Assertions.assertThat(returnDto.get(1).getUserId()).isEqualTo(user2.getId());
        Assertions.assertThat(returnDto.get(1).getContent()).isEqualTo("좋아요");

        Assertions.assertThat(returnDto2.size()).isEqualTo(1);
        Assertions.assertThat(returnDto2.get(0).getSupportInfoId()).isEqualTo(info2.getId());
        Assertions.assertThat(returnDto2.get(0).getUserId()).isEqualTo(user1.getId());
        Assertions.assertThat(returnDto2.get(0).getContent()).isEqualTo("유용해요!");

    }

    @Test
    void supportAnonymousCommentTest(){
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("tester2")
                .email("12345")
                .age(7)
                .phoneNumber("1234567")
                .build();
        User user3 = User.builder()
                .name("tester432")
                .email("123423445")
                .age(7)
                .phoneNumber("1423234567")
                .build();
        User user4 = User.builder()
                .name("test43er2")
                .email("1232445")
                .age(7)
                .phoneNumber("123454267")
                .build();
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        SupportInfo info1 = SupportInfo.builder().anonymousNumber(0).title("국가장학금 신청").build();
        supportInfoRepository.save(info1);
        SupportInfo info2 = SupportInfo.builder().anonymousNumber(0).title("봉사활동").build();
        supportInfoRepository.save(info2);


        CommentRequestDto.SupportInfoCommentDto dto1 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("굿")
                .build();

        CommentRequestDto.SupportInfoCommentDto dto2 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("좋아요")
                .build();
        CommentRequestDto.SupportInfoCommentDto dto3 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info2.getId())
                .content("유용해요!")
                .build();
        CommentRequestDto.SupportInfoCommentDto dto6 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("굿")
                .build();
        CommentRequestDto.SupportInfoCommentDto dto4 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("굿")
                .build();
        CommentRequestDto.SupportInfoCommentDto dto5 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("굿")
                .build();

        supportCommentService.saveComment(user1.getId(), dto1);
         supportCommentService.saveComment(user2.getId(), dto2);
         supportCommentService.saveComment(user1.getId(), dto3);
         supportCommentService.saveComment(user3.getId(), dto4);
         supportCommentService.saveComment(user4.getId(), dto5);
         supportCommentService.saveComment(user1.getId(), dto6);

        em.flush();

        List<CommentResponseDto.SupportInfoCommentDto> returnDto = supportCommentService.findByInfo(info1.getId());
        List<CommentResponseDto.SupportInfoCommentDto> returnDto2 = supportCommentService.findByInfo(info2.getId());

        Integer test1 = returnDto.get(0).getAnonymousNumber();
        Integer test2 = returnDto.get(1).getAnonymousNumber();
        Integer test3 = returnDto.get(2).getAnonymousNumber();
        Integer test4 = returnDto.get(3).getAnonymousNumber();
        Integer test5 = returnDto.get(4).getAnonymousNumber();

        Assertions.assertThat(test1).isEqualTo(1);
        Assertions.assertThat(test2).isEqualTo(2);
        Assertions.assertThat(test3).isEqualTo(3);
        Assertions.assertThat(test4).isEqualTo(4);
        Assertions.assertThat(test5).isEqualTo(1);
    }

    @Test
    void DiscountInfoCommentPagingTest() {
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("tester2")
                .email("12345")
                .age(7)
                .phoneNumber("1234567")
                .build();

        User user3 = User.builder()
                .name("tester3")
                .email("1234553")
                .age(9)
                .phoneNumber("1232134567")
                .build();
        userRepository.save(user2);
        userRepository.save(user3);

        DiscountInfo sale1 = DiscountInfo.builder().anonymousNumber(0).title("무신사 할인").build();
        discountInfoRepository.save(sale1);
        DiscountInfo sale2 = DiscountInfo.builder().anonymousNumber(0).title("핫트랙스 할인").build();
        discountInfoRepository.save(sale2);


        CommentRequestDto.DiscountInfoCommentDto dto1 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale1.getId())
                .content("굿")
                .build();

        CommentRequestDto.DiscountInfoCommentDto dto2 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale1.getId())
                .content("좋아요")
                .build();
        CommentRequestDto.DiscountInfoCommentDto dto3 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale2.getId())
                .content("유용해요!")
                .build();
        CommentRequestDto.DiscountInfoCommentDto dto4 = CommentRequestDto.DiscountInfoCommentDto.builder()
                .discountInfoId(sale1.getId())
                .content("구웃!")
                .build();

        discountCommentService.saveComment(user1.getId(), dto1);
        discountCommentService.saveComment(user2.getId(), dto2);
        discountCommentService.saveComment(user1.getId(), dto3);

        discountCommentService.saveComment(user1.getId(), dto4);
        discountCommentService.saveComment(user3.getId(), dto4);
        discountCommentService.saveComment(user2.getId(), dto4);
        //sale1 = 5
        em.flush();

        PageRequest pageRequest1 = PageRequest.of(0, 2);

        Page<CommentResponseDto.DiscountInfoCommentDto> result1 = discountCommentService.findByInfoWithPaging(sale1.getId(), pageRequest1);
        Assertions.assertThat(result1.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(result1.getTotalPages()).isEqualTo(3);
        Assertions.assertThat(result1.hasNext()).isTrue();
        Assertions.assertThat(result1.hasPrevious()).isFalse();
        List<CommentResponseDto.DiscountInfoCommentDto> list1 = result1.getContent();
        Assertions.assertThat(list1.get(0).getUserId()).isEqualTo(user1.getId());
        Assertions.assertThat(list1.get(0).getContent()).isEqualTo("굿");
        Assertions.assertThat(list1.get(0).getAnonymousNumber()).isEqualTo(1);

        PageRequest pageRequest2 = PageRequest.of(1, 3);

        Page<CommentResponseDto.DiscountInfoCommentDto> result2 = discountCommentService.findByInfoWithPaging(sale1.getId(), pageRequest2);
        Assertions.assertThat(result2.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(result2.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(result2.hasNext()).isFalse();
        Assertions.assertThat(result2.hasPrevious()).isTrue();
        List<CommentResponseDto.DiscountInfoCommentDto> list2 = result2.getContent();
        Assertions.assertThat(list2.get(0).getUserId()).isEqualTo(user3.getId());
        Assertions.assertThat(list2.get(0).getContent()).isEqualTo("구웃!");
        Assertions.assertThat(list2.get(0).getAnonymousNumber()).isEqualTo(3);


    }

    @Test
    void SupportInfoPagingTest() {
        User user1 = User.builder()
                .name("tester1")
                .email("1234")
                .age(5)
                .phoneNumber("123456")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("tester2")
                .email("12345")
                .age(7)
                .phoneNumber("1234567")
                .build();
        User user3 = User.builder()
                .name("tester432")
                .email("123423445")
                .age(7)
                .phoneNumber("1423234567")
                .build();
        User user4 = User.builder()
                .name("test43er2")
                .email("1232445")
                .age(7)
                .phoneNumber("123454267")
                .build();
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        SupportInfo info1 = SupportInfo.builder().anonymousNumber(0).title("국가장학금 신청").build();
        supportInfoRepository.save(info1);
        SupportInfo info2 = SupportInfo.builder().anonymousNumber(0).title("봉사활동").build();
        supportInfoRepository.save(info2);


        CommentRequestDto.SupportInfoCommentDto dto1 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("굿")
                .build();

        CommentRequestDto.SupportInfoCommentDto dto2 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("좋아요")
                .build();
        CommentRequestDto.SupportInfoCommentDto dto3 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info2.getId())
                .content("유용해요!")
                .build();
        CommentRequestDto.SupportInfoCommentDto dto6 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("굿")
                .build();
        CommentRequestDto.SupportInfoCommentDto dto4 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("굿")
                .build();
        CommentRequestDto.SupportInfoCommentDto dto5 = CommentRequestDto.SupportInfoCommentDto.builder()
                .supportInfoId(info1.getId())
                .content("굿")
                .build();

        supportCommentService.saveComment(user1.getId(), dto1);
        supportCommentService.saveComment(user2.getId(), dto2);
        supportCommentService.saveComment(user1.getId(), dto3); // 얘만 info2
        supportCommentService.saveComment(user3.getId(), dto4);
        supportCommentService.saveComment(user4.getId(), dto5);
        supportCommentService.saveComment(user1.getId(), dto6);
        supportCommentService.saveComment(user2.getId(), dto5);
        supportCommentService.saveComment(user3.getId(), dto5);
        em.flush();

        PageRequest pageRequest1 = PageRequest.of(0, 2);
        Page<CommentResponseDto.SupportInfoCommentDto> result1 = supportCommentService.findByInfoWithPaging(info1.getId(), pageRequest1);

        Assertions.assertThat(result1.getTotalElements()).isEqualTo(7);
        Assertions.assertThat(result1.getTotalPages()).isEqualTo(4);
        Assertions.assertThat(result1.hasNext()).isTrue();
        Assertions.assertThat(result1.hasPrevious()).isFalse();
        List<CommentResponseDto.SupportInfoCommentDto> list1 = result1.getContent();
        Assertions.assertThat(list1.get(0).getUserId()).isEqualTo(user1.getId());
        Assertions.assertThat(list1.get(0).getContent()).isEqualTo("굿");
        Assertions.assertThat(list1.get(0).getAnonymousNumber()).isEqualTo(1);

        PageRequest pageRequest2 = PageRequest.of(1, 5);
        Page<CommentResponseDto.SupportInfoCommentDto> result2 = supportCommentService.findByInfoWithPaging(info1.getId(), pageRequest2);

        Assertions.assertThat(result2.getTotalElements()).isEqualTo(7);
        Assertions.assertThat(result2.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(result2.hasNext()).isFalse();
        Assertions.assertThat(result2.hasPrevious()).isTrue();
        List<CommentResponseDto.SupportInfoCommentDto> list2 = result2.getContent();
        Assertions.assertThat(list2.get(0).getUserId()).isEqualTo(user2.getId());
        Assertions.assertThat(list2.get(0).getContent()).isEqualTo("굿");
        Assertions.assertThat(list2.get(0).getAnonymousNumber()).isEqualTo(2);
    }



}