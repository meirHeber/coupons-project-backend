package com.meir.coupons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meir.coupons.dto.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name" ,nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<CouponEntity> coupons;


    public CategoryEntity() {
    }
    public CategoryEntity(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
