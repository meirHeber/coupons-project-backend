package com.meir.coupons.logic;

import com.meir.coupons.dto.Company;
import com.meir.coupons.dto.User;
import com.meir.coupons.entity.CompanyEntity;
import com.meir.coupons.entity.UserEntity;
import com.meir.coupons.enums.UserTypes;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.repository.ICompanyRepository;
import com.meir.coupons.repository.IUserRepository;
import com.meir.coupons.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.meir.coupons.enums.ErrorType.*;

@Service
public class CompanyLogic {

    private ICompanyRepository companyRepository;
    private IUserRepository userRepository;

    @Autowired
    public CompanyLogic(ICompanyRepository companyRepository, IUserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public void addCompany(Integer applicantId, Company company) throws ApplicationException {
        validationForAddCompany(applicantId, company);
        companyRepository.save(new CompanyEntity(company));
    }

    public Company getCompanyById(Integer applicantId, Integer companyId) throws ApplicationException {
        validationForGetAndDeleteCompanyById(applicantId, companyId);
        return new Company(companyRepository.findById(companyId));
    }

    public List<Company> getCompanies() throws ApplicationException {
        List<CompanyEntity> companyEntityList = (List<CompanyEntity>) companyRepository.findAll();
        List<Company> companyList = new ArrayList<>();
        for (CompanyEntity companyEntity : companyEntityList) {
            Company company = new Company(companyEntity);
            companyList.add(company);
        }
        return companyList;
    }
    public Company getCompanyByName(Integer applicantId, String name) throws ApplicationException {
        validationForGetAndDeleteCompanyByName(applicantId, name);
        return new Company(companyRepository.findByName(name));
    }
    public List<User> getUsersByCompanyId(Integer applicantId, Integer companyId) throws ApplicationException {
        validationForGetAndDeleteCompanyById(applicantId, companyId);
        ValidationUtils.authorizationValidation(applicantId, UserTypes.admin, UserTypes.company);
        CompanyEntity companyEntity = new CompanyEntity(getCompanyById(applicantId, companyId));
        List<UserEntity> usersEntity = companyEntity.getUsers();
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : usersEntity) {
            User user = new User(userEntity);
            users.add(user);
        }
        return users;
    }
    public void updateCompany(Integer applicantId, Company company) throws Exception {
        validationForUpdateCompany(applicantId, company);
        companyRepository.save(new CompanyEntity(company));
    }
    public void deleteCompanyById(Integer applicantId, Integer companyId) throws ApplicationException {
        validationForGetAndDeleteCompanyById(applicantId, companyId);
        CompanyEntity companyEntity = companyRepository.findById(companyId);
        companyRepository.delete(companyEntity);
    }


    //////validations--------------------
    private void validationForGetAndDeleteCompanyByName(Integer applicantId, String name) throws ApplicationException {
        if (name == null) {
            throw new ApplicationException(INVALID_NAME);
        }
        isUserAllowedToApproachCompany(applicantId, companyRepository.findIdByName(name));
        if (!companyRepository.existsByName(name)) {
            throw new ApplicationException(NAME_DOES_NOT_EXIST);
        }
    }


    public void validationForGetAndDeleteCompanyById(Integer applicantId, Integer companyId) throws ApplicationException {
        if (companyId == null) {
            throw new ApplicationException(INVALID_ID);
        }
        isUserAllowedToApproachCompany(applicantId, companyId);
        if (!companyRepository.existsById(companyId)) {
            throw new ApplicationException(ID_DOES_NOT_EXIST);
        }
    }
    private void validationForAddCompany(Integer applicantId, Company company) throws ApplicationException {
        ValidationUtils.authorizationValidation(applicantId, UserTypes.admin);
        if (company.getId() != null) {
            throw new ApplicationException(IMPOSSIBLE_TO_PROVIDE_ID);
        }
        validationForAllParametersExceptId(company);
    }
    private void validationForUpdateCompany(Integer applicantId, Company company) throws ApplicationException {
        validationForGetAndDeleteCompanyById(applicantId, company.getId());
        if (companyRepository.existsByName(company.getName()) && companyRepository.findIdByName(company.getName()) != company.getId()) {
            throw new ApplicationException(NAME_ALREADY_EXISTS);
        }
        validationForAllParametersExceptId(company);
    }
    private void validationForAllParametersExceptId(Company company) throws ApplicationException {
        try {
            ValidationUtils.isTextLengthLegal(company.getName(), 10, 2);
        } catch (ApplicationException e) {
            throw new ApplicationException(INVALID_COMPANY_NAME);
        }
        try {
            ValidationUtils.isTextLengthLegal(company.getAddress(), 15, 5);
        } catch (ApplicationException e) {
            throw new ApplicationException(INVALID_ADDRESS);
        }
        try {
            ValidationUtils.isTextLengthLegal(company.getPhoneNumber(), 10, 10);
            Integer phoneNumber = Integer.parseInt(company.getPhoneNumber());
        } catch (ApplicationException e) {
            throw new ApplicationException(INVALID_PHONE_NUMBER);
        }
    }
    private void isUserAllowedToApproachCompany(Integer applicantId, Integer companyId) throws ApplicationException {
        if (userRepository.findById(applicantId).getUserType() != UserTypes.admin) {
            throw new ApplicationException(NO_AUTHORIZED);
        }
    }
}
