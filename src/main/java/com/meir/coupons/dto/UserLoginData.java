package com.meir.coupons.dto;

import com.meir.coupons.enums.UserTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserLoginData {
    private Integer id;
    private UserTypes userType;
    private String firstName;
    private Integer companyId;

    public UserLoginData(Integer userId, UserTypes userType, String firstName, Integer companyId) {
        this.id = userId;
        this.userType = userType;
        this.firstName = firstName;
        this.companyId = companyId;
    }
}
