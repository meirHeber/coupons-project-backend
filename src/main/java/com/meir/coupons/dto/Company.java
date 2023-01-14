package com.meir.coupons.dto;

import com.meir.coupons.entity.CompanyEntity;
import com.meir.coupons.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Company {
    private Integer id;
    private String name;
    private String phoneNumber;
    private String address;
    private List<User> users;

    public Company(){
    }

    public Company(Integer id){
        this.id = id;
    }
    public Company(Integer id, String name, String phoneNumber, String address, List<User> users) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.users = users;
    }

    public Company(String name, String phoneNumber, String address, List<User> users) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.users = users;
    }

    public Company(CompanyEntity companyEntity){
        this.id = companyEntity.getId();
        this.name = companyEntity.getName();
        this.phoneNumber = companyEntity.getPhoneNumber();
        this.address = companyEntity.getAddress();
        List<User> dtoUsers = new ArrayList<>();
        for (int i = 0; i < companyEntity.getUsers().size(); i++) {
            dtoUsers.add(new User(companyEntity.getUsers().get(i)));
        }
        this.users = dtoUsers;
    }


}
