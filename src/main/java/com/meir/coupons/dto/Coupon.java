package com.meir.coupons.dto;

import com.meir.coupons.entity.CouponEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class Coupon {
    private Integer id;
    private Company company;
    private Category category;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Integer amount;
    private Double price;
    private String image;

    ///for add
    public Coupon(Integer id, Integer companyId, Integer categoryId, String title, String description, Date startDate, Date endDate, Integer amount, Double price, String image) {
        this.id = id;
        this.company = new Company(companyId);
        this.category = new Category(categoryId);
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    //for get
    public Coupon(CouponEntity couponEntity) {
        this.id = couponEntity.getId();
        this.company = new Company(couponEntity.getCompany());
        this.category = new Category(couponEntity.getCategory());
        this.title = couponEntity.getTitle();
        this.description = couponEntity.getDescription();
        this.startDate = couponEntity.getStartDate();
        this.endDate = couponEntity.getEndDate();
        this.amount = couponEntity.getAmount();
        this.price = couponEntity.getPrice();
        this.image = couponEntity.getImage();
    }

    public Coupon(Integer id){
        this.id = id;
    }
}

