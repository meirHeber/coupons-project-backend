package com.meir.coupons.controller;

import com.meir.coupons.dto.Company;
import com.meir.coupons.dto.User;
import com.meir.coupons.entity.CompanyEntity;
import com.meir.coupons.entity.UserEntity;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.logic.UserLogic;
import com.meir.coupons.repository.ICompanyRepository;
import com.meir.coupons.repository.IUserRepository;
import com.meir.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserLogic userLogic;

    @Autowired
    public UserController(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) throws NoSuchAlgorithmException, ApplicationException {
        return userLogic.login(user);
    }

    @PostMapping
    public void createUser(@RequestBody User user) throws Exception {
        userLogic.addUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@RequestHeader String Authorization, @PathVariable("id") Integer requestedId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return userLogic.getUserById(applicantId, requestedId);
    }

    @GetMapping("/")
    public List<User> getUsers(@RequestHeader String Authorization) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return userLogic.getUsers(applicantId);
    }


    @GetMapping("/byUsername")
    public User getUserByUsername(@RequestHeader String Authorization, @RequestParam("username") String username) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return userLogic.getUserByUsername(applicantId, username);
    }

    @GetMapping("/byIdAndUsername")
    public User getUserByIdAndUsername(@RequestHeader String Authorization, @RequestParam("id") Integer requestedId, @RequestParam("username") String username) throws ApplicationException {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return userLogic.findByIdAndUsername(applicantId, requestedId, username);
    }
    @PutMapping
    public void updateUser(@RequestHeader String Authorization, @RequestBody User requestedUser) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        userLogic.updateUser(applicantId, requestedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader String Authorization, @PathVariable("id") Integer requestedId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        userLogic.removeUserById(applicantId, requestedId);
    }

    @DeleteMapping("/deleteByUsername")
    public void deleteUserByUsername(@RequestHeader String Authorization, @RequestParam("username") String username) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        userLogic.removeUserByUsername(applicantId, username);
    }

    @GetMapping("/byCompanyId")
    public List<User> getUsersByCompanyId(@RequestHeader String Authorization, @RequestParam("companyId") Integer companyId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return userLogic.getUsersByCompanyId(applicantId, companyId);
    }
}
