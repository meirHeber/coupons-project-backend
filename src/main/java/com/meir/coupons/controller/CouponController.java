package com.meir.coupons.controller;

import com.meir.coupons.dto.Coupon;
import com.meir.coupons.entity.CouponEntity;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.logic.CouponLogic;
import com.meir.coupons.repository.ICouponRepository;
import com.meir.coupons.utils.JWTUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private CouponLogic couponLogic;

    @Autowired
    public CouponController(CouponLogic couponLogic) {
        this.couponLogic = couponLogic;
    }

    @PostMapping
    public void createCoupon(@RequestHeader String Authorization, @RequestBody Coupon coupon) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        couponLogic.addCoupon(applicantId, coupon);
    }

    @GetMapping("/{id}")
    public Coupon getCouponById(@PathVariable("id") Integer requestedId) throws Exception {
        return couponLogic.getCouponById(requestedId);
    }


    @GetMapping("/")
    public List<Coupon> getCoupons() throws Exception {
        return couponLogic.getCoupons();
    }
    @GetMapping("/size")
    public long getCouponsSize() throws Exception {
        return couponLogic.getCouponsSize();
    }
    @GetMapping("/size/byCompany")
    public long getCouponsSize(@RequestHeader String Authorization) throws ApplicationException {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return couponLogic.getCouponsSize(applicantId);
    }

    @GetMapping("/allByCompanyUser")
    public List<Coupon> getCouponsByCompanyUser(@RequestHeader String Authorization){
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return couponLogic.getCouponsByCompanyUser(applicantId);
    }

    @GetMapping("/byPagesAndSorting/{pageNumber}/{quantityPerPage}")
    public List<Coupon> getCouponsByPagesAndSorting(@PathVariable(name = "pageNumber") int pageNumber,
                                                    @PathVariable(name = "quantityPerPage") int quantityPerPage,
                                                    @RequestParam(defaultValue = "id", name = "sortParameter") String sortParameter) throws Exception {
        return couponLogic.getCouponsByPagesAndSorting(pageNumber, quantityPerPage, sortParameter);
    }

    @GetMapping("/paginationAndFilterBySortForCompany")
    public List<Coupon> paginationAndFilterBySortForCompany(@RequestHeader String Authorization,
                                                  @RequestParam(name = "searchBy") String containsBy,
                                                  @RequestParam(name = "searchValue") String containsValue,
                                                  @RequestParam(name = "pageNumber") Integer pageNumber,
                                                  @RequestParam(name = "quantityPerPage") Integer quantityPerPage,
                                                  @RequestParam("sortBy") String sortBy) {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return couponLogic.paginationAndFilterBySortOnlyForCompanyUser(applicantId, containsBy, containsValue, pageNumber, quantityPerPage, sortBy);
    }

    @GetMapping("/paginationAndFilterBySort")
    public List<Coupon> paginationAndFilterBySort(@RequestParam(name = "searchBy") String containsBy,
                                                  @RequestParam(name = "searchValue") String containsValue,
                                                  @RequestParam(name = "pageNumber") Integer pageNumber,
                                                  @RequestParam(name = "quantityPerPage") Integer quantityPerPage,
                                                  @RequestParam("sortBy") String sortBy) {
        return couponLogic.paginationAndFilterBySortNotForCompanyUser(containsBy, containsValue, pageNumber, quantityPerPage, sortBy);
    }

    @PutMapping
    public void updateCoupon(@RequestHeader String Authorization, @RequestBody Coupon couponForUpdate) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        couponLogic.updateCoupon(applicantId, couponForUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteCoupon(@RequestHeader String Authorization, @PathVariable("id") Integer requestedId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        couponLogic.deleteById(applicantId, requestedId);
    }
}
