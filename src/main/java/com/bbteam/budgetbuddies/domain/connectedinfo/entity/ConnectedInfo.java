package com.bbteam.budgetbuddies.domain.connectedinfo.entity;

import com.bbteam.budgetbuddies.common.BaseEntity;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@SQLRestriction("deleted = false")
public class ConnectedInfo extends BaseEntity {
    /**
     * 연결된 지원정보 또는 할인정보를 나타내는 엔티티 (교차 엔티티)
     * supportInfo, discountInfo 둘 중 하나 이상 반드시 연결되어 있어야 함.
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_info_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private SupportInfo supportInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_info_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private DiscountInfo discountInfo;

}
