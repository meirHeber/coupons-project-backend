package com.meir.coupons.dto;

import com.meir.coupons.entity.CategoryEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Category {
    private Integer id;
    private String name;

    public Category(){
    }
    public Category(Integer id){
        this.id = id;
    }
    public Category(CategoryEntity categoryEntity){
        this.id = categoryEntity.getId();
        this.name = categoryEntity.getName();
    }


    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
    }
}
