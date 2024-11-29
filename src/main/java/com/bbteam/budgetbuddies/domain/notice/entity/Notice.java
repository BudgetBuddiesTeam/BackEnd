package com.bbteam.budgetbuddies.domain.notice.entity;

import com.bbteam.budgetbuddies.common.BaseEntity;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeRequestDto;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Notice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String body;

    public void update(NoticeRequestDto dto) {
        title = dto.getTitle();
        body = dto.getBody();
    }

}
