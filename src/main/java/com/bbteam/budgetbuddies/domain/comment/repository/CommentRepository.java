package com.bbteam.budgetbuddies.domain.comment.repository;

import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.discountInfo.id = :discountInfoId" +
    " order by c.createdAt asc") // 익명번호 부여용
    List<Comment> findByDiscountInfo(@Param("discountInfoId")Long discountInfoId);

    @Query("select c from Comment c where c.discountInfo.id = :discountInfoId" +
            " order by c.createdAt asc")
    Page<Comment> findByDiscountInfoWithPaging(@Param("discountInfoId")Long discountInfoId,
                                               Pageable pageable);

    @Query("select c from Comment c where c.supportInfo.id = :supportInfoId" +
    " order by c.createdAt asc")
    List<Comment> findBySupportInfo(@Param("supportInfoId")Long supportInfoId);

    @Query("select c from Comment c where c.supportInfo.id = :supportInfoId" +
            " order by c.createdAt asc")
    Page<Comment> findBySupportInfoWithPaging(@Param("supportInfoId")Long supportInfoId,
                                               Pageable pageable);

    Optional<Comment> findTopByUserAndDiscountInfo(User user, DiscountInfo discountInfo);
    Optional<Comment> findTopByUserAndSupportInfo(User user, SupportInfo supportInfo);

}
