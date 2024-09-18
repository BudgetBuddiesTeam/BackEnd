package com.bbteam.budgetbuddies.domain.hashtag.entity;

import com.bbteam.budgetbuddies.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Hashtag extends BaseEntity {

    private String name; // 해시태그 이름

}
