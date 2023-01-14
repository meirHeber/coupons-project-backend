package com.meir.coupons.entity;

import com.meir.coupons.dto.Purchase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "purchases")
public class PurchaseEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(optional = false)
    private CouponEntity coupon;

    @ManyToOne(optional = false)
    private CustomerEntity customer;

    @Column(name = "time_stamp")
    @CreationTimestamp
    private Date timeStamp;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "total_price")
    private Double totalPrice;

    public PurchaseEntity() {
    }
    public PurchaseEntity(Purchase purchase, CouponEntity couponEntity, CustomerEntity customerEntity) {
        this.id = purchase.getId();
        this.coupon = couponEntity;
        this.customer = customerEntity;
        this.timeStamp = purchase.getTimeStamp();
        this.amount = purchase.getAmount();
        this.totalPrice = purchase.getTotalPrice();

    }

}
