package com.meir.coupons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="users")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserTypes userType;

    @ManyToOne
    private CompanyEntity company;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<PurchaseEntity> purchases;



    public UserEntity() {
    }



    public UserEntity(User user) throws ApplicationException {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userType = user.getUserType();
    }

    public UserEntity(User user, CompanyEntity companyEntity) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userType = user.getUserType();
        this.company = companyEntity;
    }

    public UserEntity(Integer id, String firstName, String lastName, String username, String password, UserTypes userType, CompanyEntity company) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.company = company;
    }

    public UserEntity(String firstName, String lastName, String username, String password, UserTypes userType, CompanyEntity company) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.company = company;
    }
    public UserEntity(UserEntity userEntity){
        this.id = userEntity.getId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.userType = userEntity.getUserType();
        this.company = userEntity.getCompany();
    }
}
