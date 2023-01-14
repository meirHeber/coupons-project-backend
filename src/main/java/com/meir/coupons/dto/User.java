package com.meir.coupons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meir.coupons.entity.UserEntity;
import com.meir.coupons.enums.UserTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private UserTypes userType;
    private Company company;

    ///Default constructor - must for Annotation @JsonProperty
    public User(){
    }

   ///for get
    public User(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.userType = userEntity.getUserType();
        if (userEntity.getCompany() != null) {
            this.company = new Company(userEntity.getCompany().getId());
        }
    }

    ///for update customer
    public User(Integer id, String firstName, String lastName, String username, String password, UserTypes userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    ///for add - not company!
    public User(String firstName, String lastName, String username, String password, UserTypes userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    ///for add - company!
    public User(String firstName, String lastName, String username, String password, UserTypes userType, Integer company) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.company = new Company(company);
    }
    public User(Integer id) {
        this.id = id;
    }
}
