package com.bbteam.budgetbuddies.domain.category.entity;

import com.bbteam.budgetbuddies.common.BaseEntity;
import jakarta.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

}
