package com.meir.coupons.dto;


import com.meir.coupons.entity.PurchaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class Purchase {
    private Integer id;

    private Coupon coupon;

    private Customer customer;

    private Date timeStamp;

    private Integer amount;

    private Double totalPrice;


    ///for get
    public Purchase(PurchaseEntity purchaseEntity) {
        this.id = purchaseEntity.getId();
        this.coupon = new Coupon(purchaseEntity.getCoupon());
        this.customer = new Customer(purchaseEntity.getCustomer());
        this.timeStamp = purchaseEntity.getTimeStamp();
        this.amount = purchaseEntity.getAmount();
        this.totalPrice = purchaseEntity.getTotalPrice();
    }


    ///for add

    public Purchase(Integer coupon, Integer amount) {
        this.coupon = new Coupon(coupon);
        this.amount = amount;
    }
}