package com.meir.coupons.entity;

import com.meir.coupons.dto.Customer;
import com.meir.coupons.dto.User;
import com.meir.coupons.enums.UserTypes;
import com.meir.coupons.exceptions.ApplicationException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name="customers")
public class CustomerEntity extends UserEntity {

    @Column(name = "address")
    private String address;

    @Column(name = "amount_of_children")
    private Integer amountOfChildren;

    public CustomerEntity(String address, Integer amountOfChildren) {
        this.address = address;
        this.amountOfChildren = amountOfChildren;
    }

    public CustomerEntity() {}

    public CustomerEntity(Customer customer) throws ApplicationException {
        super(customer);
        this.address = customer.getAddress();
        this.amountOfChildren = customer.getAmountOfChildren();
    }
}
