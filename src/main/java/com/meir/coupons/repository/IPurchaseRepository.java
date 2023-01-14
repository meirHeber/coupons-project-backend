package com.meir.coupons.repository;

import com.meir.coupons.dto.Purchase;
import com.meir.coupons.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
    public PurchaseEntity findById(Integer id);

    public boolean existsById(Integer id);

    @Query("SELECT p FROM PurchaseEntity p WHERE p.customer= :customerEntity")
    public List<PurchaseEntity> findByUserId(@Param("customerEntity") CustomerEntity customerEntity);

    @Query("SELECT p FROM PurchaseEntity p WHERE p.coupon.company= :companyEntity")
    public List<PurchaseEntity> findByCompanyId(@Param("companyEntity") CompanyEntity companyEntity);
    @Query("SELECT p FROM PurchaseEntity p WHERE p.coupon= :couponEntity")
    public List<PurchaseEntity> findByCouponId(@Param("couponEntity") CouponEntity couponEntity);
}
