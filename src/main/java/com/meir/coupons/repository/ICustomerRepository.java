package com.meir.coupons.repository;

import com.meir.coupons.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustomerRepository extends JpaRepository<CustomerEntity, Long> {

    public CustomerEntity findById(Integer id);
    public boolean existsById(Integer id);
}
