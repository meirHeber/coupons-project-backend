package com.meir.coupons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meir.coupons.dto.Company;
import com.meir.coupons.dto.Coupon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "coupons")
public class CouponEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(optional = false)
    private CompanyEntity company;

    @ManyToOne(optional = false)
    private CategoryEntity category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @Column(name = "endDate", nullable = false)
    private Date endDate;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "image")
    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.REMOVE)
    private List<PurchaseEntity> purchases;

    public CouponEntity() {
    }

    public CouponEntity(Coupon coupon, CompanyEntity companyEntity, CategoryEntity categoryEntity) {
        this.id = coupon.getId();
        this.company = companyEntity;
        this.category = categoryEntity;
        this.title = coupon.getTitle();
        this.description = coupon.getDescription();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.amount = coupon.getAmount();
        this.price = coupon.getPrice();
        this.image = coupon.getImage();
    }

    public CouponEntity(Coupon coupon){
        this.id = coupon.getId();

        if(coupon.getCompany() != null){
            this.company = new CompanyEntity(coupon.getCompany());
            this.category = new CategoryEntity(coupon.getCategory());
            this.title = coupon.getTitle();
            this.description = coupon.getDescription();
            this.startDate = coupon.getStartDate();
            this.endDate = coupon.getEndDate();
            this.price = coupon.getPrice();
            this.image = coupon.getImage();
            this.amount = coupon.getAmount();

        }

    }

}
