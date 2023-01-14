package com.meir.coupons.dto;

import com.meir.coupons.entity.CustomerEntity;
import com.meir.coupons.entity.UserEntity;
import com.meir.coupons.enums.UserTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Customer extends User {
    private String address;
    private Integer amountOfChildren;



    public Customer(CustomerEntity customerEntity){
        super(customerEntity.getId(), customerEntity.getFirstName(), customerEntity.getLastName(), customerEntity.getUsername(), customerEntity.getPassword(), customerEntity.getUserType());
        this.address = customerEntity.getAddress();
        this.amountOfChildren = customerEntity.getAmountOfChildren();
    }

    public Customer(String firstName, String lastName, String username, String password, UserTypes userType, String address, Integer amountOfChildren) {
        super(firstName, lastName, username, password, userType);
        this.address = address;
        this.amountOfChildren = amountOfChildren;
    }
    public Customer(Integer id){
        super(id);
    }
}

