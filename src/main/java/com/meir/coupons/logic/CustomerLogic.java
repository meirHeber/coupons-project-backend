package com.meir.coupons.logic;

import com.meir.coupons.dto.Customer;
import com.meir.coupons.entity.CustomerEntity;
import com.meir.coupons.enums.UserTypes;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.repository.ICustomerRepository;
import com.meir.coupons.repository.IUserRepository;
import com.meir.coupons.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.meir.coupons.enums.ErrorType.*;

@Service
public class CustomerLogic {

    private ICustomerRepository customerRepository;
    private IUserRepository userRepository;
    private UserLogic userLogic;


    @Autowired
    public CustomerLogic(ICustomerRepository customerRepository, IUserRepository userRepository, UserLogic userLogic) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.userLogic = userLogic;
    }

    public void addCustomer(Customer customer) throws Exception {
        validationForAddCustomer(customer);
        customer.setPassword(userLogic.passwordEncoder(customer.getPassword()));
        customerRepository.save(new CustomerEntity(customer));
    }

    public List<Customer> getCustomers(Integer applicantId) throws ApplicationException {
        ValidationUtils.authorizationValidation(applicantId, UserTypes.admin);
        List<CustomerEntity> customerEntities = customerRepository.findAll();
        List<Customer> customers = new ArrayList<>();
        for (CustomerEntity customerEntity : customerEntities) {
            customers.add(new Customer(customerEntity));
        }
        return customers;
    }

    public Customer getCustomerById(Integer applicantId, Integer id) throws Exception {
        validationForGetAndDeleteById(applicantId, id);
        return new Customer(customerRepository.findById(id));
    }

    public void updateCustomer(Integer applicantId, Customer customer) throws Exception {
        validationForUpdateCustomer(applicantId, customer);
        customer.setPassword(userLogic.passwordEncoder(customer.getPassword()));
        customerRepository.save(new CustomerEntity(customer));
    }

    public void deleteCustomerById(Integer applicantId, Integer id) throws Exception {
        validationForGetAndDeleteById(applicantId, id);
        CustomerEntity customerEntity = customerRepository.findById(id);
        customerRepository.delete(customerEntity);
    }

    //////////////////validations-----------------------
    private void validationForGetAndDeleteById(Integer applicantId, Integer id) throws ApplicationException {
        if (id == null) {
            throw new ApplicationException(INVALID_ID);
        }
        if (applicantId != id && userRepository.findById(applicantId).getUserType() != UserTypes.admin) {
            throw new ApplicationException(NO_AUTHORIZED);
        }
        if (id < 0) {
            throw new ApplicationException(NEGATIVE_NUMBER_ENTERED);
        }
        if (!customerRepository.existsById(id)) {
            throw new ApplicationException(ID_DOES_NOT_EXIST);
        }
    }

    private void validationForAddCustomer(Customer customer) throws ApplicationException {
        userLogic.validationForAddUser(customer);
        validationForAllParametersExceptId(customer);
    }

    private void validationForUpdateCustomer(Integer applicantId, Customer customer) throws ApplicationException {
        validationForGetAndDeleteById(applicantId, customer.getId());
        validationForAllParametersExceptId(customer);
    }

    private void validationForAllParametersExceptId(Customer customer) throws ApplicationException {
        try {
            ValidationUtils.isTextLengthLegal(customer.getAddress(), 25, 5);
        } catch (ApplicationException e) {
            throw new ApplicationException(INVALID_ADDRESS);
        }
        if (customer.getAmountOfChildren() > 80) {
            throw new ApplicationException(INVALID_AMOUNT_OF_CHILDREN);
        }
        if (customer.getAmountOfChildren() < 0) {
            throw new ApplicationException(NEGATIVE_NUMBER_ENTERED);
        }
    }
}
