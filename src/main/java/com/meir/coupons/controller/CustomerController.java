package com.meir.coupons.controller;

import com.meir.coupons.dto.Customer;
import com.meir.coupons.logic.CustomerLogic;
import com.meir.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerLogic customerLogic;

    @Autowired
    public CustomerController(CustomerLogic customerLogic) {
        this.customerLogic = customerLogic;
    }

    @PostMapping
    public void createCustomer(@RequestBody Customer customer) throws Exception {
        customerLogic.addCustomer(customer);
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@RequestHeader String Authorization, @PathVariable("id") Integer requestedId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return customerLogic.getCustomerById(applicantId, requestedId);
    }
    @GetMapping("/")
    public List<Customer> getCustomers(@RequestHeader String Authorization) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return customerLogic.getCustomers(applicantId);
    }
    @PutMapping
    public void updateCustomer(@RequestHeader String Authorization, @RequestBody Customer customerForUpdate) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        customerLogic.updateCustomer(applicantId, customerForUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@RequestHeader String Authorization, @PathVariable("id") Integer requestedId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        customerLogic.deleteCustomerById(applicantId, requestedId);
    }

}
