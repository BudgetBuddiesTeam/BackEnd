package com.bbteam.budgetbuddies.domain.comment.repository;

import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.repository.DiscountInfoRepository;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.repository.SupportInfoRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    SupportInfoRepository supportInfoRepository;

    @Autowired
    DiscountInfoRepository discountInfoRepository;

    @Autowired
    EntityManager em;


    @Test
    public void saveTest(){
        Comment comment1 = Comment.builder().content("test1").build();
        Comment comment2 = Comment.builder().content("test2").build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        em.flush();

        Comment found1 = commentRepository.findById(comment1.getId()).get();
        Comment found2 = commentRepository.findById(comment2.getId()).get();


        Assertions.assertThat(comment1).isEqualTo(found1);
        Assertions.assertThat(comment2).isEqualTo(found2);
    }

    @Test
    public void findByDiscountInfoTest(){
        DiscountInfo sale1 = DiscountInfo.builder().title("무신사 할인").build();
        DiscountInfo sale2 = DiscountInfo.builder().title("옥션 할인").build();

        discountInfoRepository.save(sale1);
        discountInfoRepository.save(sale2);
        Comment comment1 = Comment.builder().content("test1")
                .discountInfo(sale1)
                .build();
        Comment comment2 = Comment.builder().content("test2")
                .discountInfo(sale2)
                .build();
        Comment comment3 = Comment.builder().content("test3")
                .discountInfo(sale1)
                .build();
        Comment comment4 = Comment.builder().content("test4")
                .discountInfo(sale1)
                .build();
        Comment comment5 = Comment.builder().content("test5")
                .discountInfo(sale2)
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);


        em.flush();

        List<Comment> found1 = commentRepository.findByDiscountInfo(sale1.getId());
        List<Comment> found2 = commentRepository.findByDiscountInfo(sale2.getId());

        Assertions.assertThat(found1.size()).isEqualTo(3);
        Assertions.assertThat(found2.size()).isEqualTo(2);


        Assertions.assertThat(found1.get(0)).isEqualTo(comment1);
        Assertions.assertThat(found1.get(1)).isEqualTo(comment3);
        Assertions.assertThat(found1.get(2)).isEqualTo(comment4);
        Assertions.assertThat(found2.get(0)).isEqualTo(comment2);
        Assertions.assertThat(found2.get(1)).isEqualTo(comment5);


    }

    @Test
    public void findBySupportInfoTest(){
//        DiscountInfo sale1 = DiscountInfo.builder().title("무신사 할인").build();
//        DiscountInfo sale2 = DiscountInfo.builder().title("옥션 할인").build();

        SupportInfo support1 = SupportInfo.builder().title("국가장학금").build();
        SupportInfo support2 = SupportInfo.builder().title("비전장학금").build();
        SupportInfo support3 = SupportInfo.builder().title("좋은장학금").build();
        SupportInfo support4 = SupportInfo.builder().title("서울봉사").build();
        SupportInfo support5 = SupportInfo.builder().title("청년대출").build();

        supportInfoRepository.save(support1);
        supportInfoRepository.save(support2);
        supportInfoRepository.save(support3);
        supportInfoRepository.save(support4);
        supportInfoRepository.save(support5);

        Comment comment1 = Comment.builder().content("test1")
                .supportInfo(support1)
                .build();
        Comment comment2 = Comment.builder().content("test2")
                .supportInfo(support2)
                .build();
        Comment comment3 = Comment.builder().content("test3")
                .supportInfo(support3)
                .build();
        Comment comment4 = Comment.builder().content("test4")
                .supportInfo(support4)
                .build();
        Comment comment5 = Comment.builder().content("test5")
                .supportInfo(support5)
                .build();
        Comment comment6 = Comment.builder().content("test6")
                .supportInfo(support1)
                .build();
        Comment comment7 = Comment.builder().content("test7")
                .supportInfo(support1)
                .build();
        Comment comment8 = Comment.builder().content("test8")
                .supportInfo(support3)
                .build();
        Comment comment9 = Comment.builder().content("test9")
                .supportInfo(support4)
                .build();
        Comment comment10 = Comment.builder().content("test10")
                .supportInfo(support4)
                .build();


        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);
        commentRepository.save(comment6);
        commentRepository.save(comment7);
        commentRepository.save(comment8);
        commentRepository.save(comment9);
        commentRepository.save(comment10);


        em.flush();

        List<Comment> found1 = commentRepository.findBySupportInfo(support1.getId());
        List<Comment> found2 = commentRepository.findBySupportInfo(support2.getId());
        List<Comment> found3 = commentRepository.findBySupportInfo(support3.getId());
        List<Comment> found4 = commentRepository.findBySupportInfo(support4.getId());
        List<Comment> found5 = commentRepository.findBySupportInfo(support5.getId());

        Assertions.assertThat(found1.size()).isEqualTo(3);
        Assertions.assertThat(found2.size()).isEqualTo(1);
        Assertions.assertThat(found3.size()).isEqualTo(2);
        Assertions.assertThat(found4.size()).isEqualTo(3);
        Assertions.assertThat(found5.size()).isEqualTo(1);

    }

    @Test
    public void deleteTest(){
        SupportInfo support1 = SupportInfo.builder().title("국가장학금").build();
        SupportInfo support2 = SupportInfo.builder().title("비전장학금").build();
        SupportInfo support3 = SupportInfo.builder().title("좋은장학금").build();
        SupportInfo support4 = SupportInfo.builder().title("서울봉사").build();
        SupportInfo support5 = SupportInfo.builder().title("청년대출").build();

        supportInfoRepository.save(support1);
        supportInfoRepository.save(support2);
        supportInfoRepository.save(support3);
        supportInfoRepository.save(support4);
        supportInfoRepository.save(support5);

        Comment comment1 = Comment.builder().content("test1")
                .supportInfo(support1)
                .build();
        Comment comment2 = Comment.builder().content("test2")
                .supportInfo(support2)
                .build();
        Comment comment3 = Comment.builder().content("test3")
                .supportInfo(support3)
                .build();
        Comment comment4 = Comment.builder().content("test4")
                .supportInfo(support4)
                .build();
        Comment comment5 = Comment.builder().content("test5")
                .supportInfo(support5)
                .build();
        Comment comment6 = Comment.builder().content("test6")
                .supportInfo(support1)
                .build();
        Comment comment7 = Comment.builder().content("test7")
                .supportInfo(support1)
                .build();
        Comment comment8 = Comment.builder().content("test8")
                .supportInfo(support3)
                .build();
        Comment comment9 = Comment.builder().content("test9")
                .supportInfo(support4)
                .build();
        Comment comment10 = Comment.builder().content("test10")
                .supportInfo(support4)
                .build();


        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);
        commentRepository.save(comment6);
        commentRepository.save(comment7);
        commentRepository.save(comment8);
        commentRepository.save(comment9);
        commentRepository.save(comment10);

        commentRepository.delete(comment1); // comment1이 삭제되면 support1의 개수는 2개가 되어야한다.

        em.flush();

        List<Comment> found1 = commentRepository.findBySupportInfo(support1.getId());
        List<Comment> found2 = commentRepository.findBySupportInfo(support2.getId());
        List<Comment> found3 = commentRepository.findBySupportInfo(support3.getId());
        List<Comment> found4 = commentRepository.findBySupportInfo(support4.getId());
        List<Comment> found5 = commentRepository.findBySupportInfo(support5.getId());

        Assertions.assertThat(found1.size()).isEqualTo(2); // 해당 로직 검증
        Assertions.assertThat(found2.size()).isEqualTo(1);
        Assertions.assertThat(found3.size()).isEqualTo(2);
        Assertions.assertThat(found4.size()).isEqualTo(3);
        Assertions.assertThat(found5.size()).isEqualTo(1);
    }


}