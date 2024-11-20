package com.bbteam.budgetbuddies.domain.searchkeyword.domain;

import com.bbteam.budgetbuddies.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchKeyword extends BaseEntity {

    private String keyword;

    public void changeKeyword(String newKeyword) {
        this.keyword = newKeyword;
    }

}
