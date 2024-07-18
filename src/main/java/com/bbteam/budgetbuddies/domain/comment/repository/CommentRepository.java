package com.bbteam.budgetbuddies.domain.comment.repository;

import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.discountInfo.id = :discountInfoId" +
    " order by c.createdAt asc") // delete_at 을 어떻게 식별해야할지???
    List<Comment> findByDiscountInfo(@Param("discountInfoId")Long discountInfoId);

    @Query("select c from Comment c where c.supportInfo.id = :supportInfoId" +
    " order by c.createdAt asc")
    List<Comment> findBySupportInfo(@Param("supportInfoId")Long supportInfoId);
}
