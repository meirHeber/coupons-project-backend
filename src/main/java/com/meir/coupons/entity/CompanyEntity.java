package com.meir.coupons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meir.coupons.dto.Company;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "companies")
public class CompanyEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE)
    private List<UserEntity> users;

    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE)
    private List<CouponEntity> coupons;


    public CompanyEntity() {
    }


    public CompanyEntity(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.phoneNumber = company.getPhoneNumber();
        this.address = company.getAddress();
    }

    public CompanyEntity(String name, String phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public CompanyEntity(Integer id, String name, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }


}

