package com.meir.coupons.logic;

import com.meir.coupons.dto.Company;
import com.meir.coupons.dto.User;
import com.meir.coupons.dto.UserLoginData;
import com.meir.coupons.entity.UserEntity;
import com.meir.coupons.enums.UserTypes;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.repository.ICompanyRepository;
import com.meir.coupons.repository.IUserRepository;
import com.meir.coupons.utils.JWTUtils;
import com.meir.coupons.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.meir.coupons.enums.ErrorType.*;

@Service
public class UserLogic {

    private IUserRepository userRepository;

    private ICompanyRepository companyRepository;

    private CompanyLogic companyLogic;

    @Autowired
    public UserLogic(IUserRepository userRepository, ICompanyRepository companyRepository, CompanyLogic companyLogic) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.companyLogic = companyLogic;
    }

    public String login(User user) throws NoSuchAlgorithmException, ApplicationException {
        user.setPassword(passwordEncoder(user.getPassword()));
        Optional<UserLoginData> optionalLoggedInUser = userRepository.login(user.getUsername(), user.getPassword());
        if(optionalLoggedInUser.isEmpty()){
            throw new ApplicationException(LOGIN_FAILED);
        }
        UserLoginData userLoginData = optionalLoggedInUser.get();
        return JWTUtils.createJWT(userLoginData);
    }

    public void addUser(User user) throws Exception {
        validationForAddUser(user);
        user.setPassword(passwordEncoder(user.getPassword()));
        UserEntity userEntity = new UserEntity(user);
        if (user.getCompany() != null) {
            userEntity.setCompany(companyRepository.findById(user.getCompany().getId()));
        }
        userRepository.save(userEntity);
    }
    public User getUserById(Integer applicantId, Integer requestedId) throws ApplicationException {
        validationForGetAndDeleteUserById(applicantId, requestedId);
        return new User(userRepository.findById(requestedId));
    }
    public User getUserByUsername(Integer applicantId, String username) throws ApplicationException{
        validationForGetUserByUsername(applicantId, username);
        return new User(userRepository.findByUsername(username));
    }
    public void updateUser(Integer applicantId,User userForUpdate) throws Exception {
        validationForUpdateUser(applicantId,userForUpdate);
        userForUpdate.setPassword(passwordEncoder(userForUpdate.getPassword()));
        userRepository.save(new UserEntity(userForUpdate));
    }
    public List<User> getUsers(Integer applicantId) throws ApplicationException {
        ValidationUtils.authorizationValidation(applicantId, UserTypes.admin);
        List<UserEntity> userEntities = (List<UserEntity>) userRepository.findAll();
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            User user = new User(userEntity);
            if (userEntity.getCompany() != null){
                user.setCompany(new Company(userEntity.getCompany()));
            }
            users.add(user);
        }
        return users;
    }
    public void removeUserById(Integer applicantId,Integer requestedId) throws Exception {
//        ///Already passed the validations of the "get" function
//        userRepository.deleteById(requestedId);
        userRepository.delete(new UserEntity(getUserById(applicantId, requestedId)));
    }
    public void removeUserByUsername(Integer applicantId,String username) throws Exception {
        ///Already passed the validations of the "get" function
        userRepository.delete(new UserEntity(getUserByUsername(applicantId, username)));
    }
    public User findByIdAndUsername(Integer applicantId, Integer requestedId, String username) throws ApplicationException {
        validationForGetUserByIdAndUsername(applicantId, requestedId, username);
        return new User(userRepository.findByIdAndUsername(requestedId, username));
    }
    public List<User> getUsersByCompanyId(Integer applicantId, Integer companyId) throws ApplicationException {
        ValidationUtils.authorizationValidation(applicantId, UserTypes.admin);
        return companyLogic.getCompanyById(applicantId, companyId).getUsers();
    }
//    public User login(User user) throws NoSuchAlgorithmException, ApplicationException {
//        user.setPassword(passwordEncoder(user.getPassword()));
//        Optional<UserEntity> optionalLoggedInUser = userRepository.login(user.getUsername(), user.getPassword());
//        if(optionalLoggedInUser.isEmpty()){
//            throw new ApplicationException(LOGIN_FAILED);
//        }
//        UserEntity userEntity = optionalLoggedInUser.get();
//        String token = JWTUtils.createJWT(userEntity);
//        userEntity.setToken(token);
//        return new User(userEntity);
//    }
    public String passwordEncoder(String password) throws NoSuchAlgorithmException {
        String saltedPassword = "john" + password + "brice";
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(saltedPassword.getBytes());
        byte [] digest = messageDigest.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    ///////////////////////validations-----------------------------
    public void validationForAddUser(User user) throws ApplicationException {
        if (user.getId() != null){
            throw new ApplicationException(IMPOSSIBLE_TO_PROVIDE_ID);
        }
        if (userRepository.existsByUsername(user.getUsername())){
            throw new ApplicationException(USERNAME_ALREADY_EXIST);
        }
        validationForAllParametersExceptId(user);
    }
    public void validationForGetAndDeleteUserById(Integer applicantId, Integer requestedId) throws ApplicationException {
        if (requestedId == null) {
            throw new ApplicationException(INVALID_ID);
        }
        if (applicantId != requestedId && userRepository.findById(applicantId).getUserType() != UserTypes.admin){
            throw new ApplicationException(NO_AUTHORIZED);
        }
        if (!userRepository.existsById(requestedId)) {
            throw new ApplicationException(ID_DOES_NOT_EXIST);
        }
    }
    private void validationForGetUserByUsername(Integer applicantId, String username) throws ApplicationException {
        if (username == null){
            throw new ApplicationException(INVALID_USER_NAME);
        }
        if (applicantId != userRepository.findIdByUsername(username)){
            throw new ApplicationException(NO_AUTHORIZED);
        }
        if (!userRepository.existsByUsername(username)){
            throw new ApplicationException(USERNAME_DOES_NOT_EXIST);
        }
    }
    private void validationForGetUserByIdAndUsername(Integer applicantId, Integer requestedId, String username) throws ApplicationException {
        validationForGetAndDeleteUserById(applicantId, requestedId);
        validationForGetUserByUsername(applicantId, username);
    }
    public void validationForUpdateUser(Integer applicantId,User userForUpdate) throws ApplicationException {
        validationForGetAndDeleteUserById(applicantId, userForUpdate.getId());
        if (userRepository.existsByUsername(userForUpdate.getUsername()) && userRepository.findIdByUsername(userForUpdate.getUsername()) != userForUpdate.getId()){
            throw new ApplicationException(USERNAME_ALREADY_EXIST);
        }
        validationForAllParametersExceptId(userForUpdate);
    }
    private void validationForAllParametersExceptId(User user) throws ApplicationException {
        ///Checking the length of username
        try {
            ValidationUtils.isTextLengthLegal(user.getUsername(), 10, 3);
        }
        catch (ApplicationException e){
            throw new ApplicationException(INVALID_USER_NAME);
        }
        ///Checking the length of firstName and lastName
        try {
            ValidationUtils.isTextLengthLegal(user.getFirstName(), 10, 2);
            ValidationUtils.isTextLengthLegal(user.getLastName(), 10, 2);
        }
        catch (ApplicationException e){
            throw new ApplicationException(INVALID_NAME);
        }
        ///Checking the length of password
        try {
            ValidationUtils.isTextLengthLegal(user.getPassword(), 10, 7);
        }
        catch (ApplicationException e){
            throw new ApplicationException(INVALID_PASSWORD);
        }
        if (user.getUserType() == UserTypes.customer && user.getCompany() != null) {
            throw new ApplicationException(INVALID_USER_TYPE);
        }
        if (user.getUserType() == UserTypes.admin && user.getCompany() != null) {
            throw new ApplicationException(INVALID_USER_TYPE);
        }
        if (user.getUserType() == UserTypes.company && user.getCompany() == null) {
            throw new ApplicationException(INVALID_USER_TYPE);
        }
        if (user.getUserType() == UserTypes.company && !companyRepository.existsById(user.getCompany().getId())){
            throw new ApplicationException(INVALID_COMPANY_ID);
        }
    }
}



