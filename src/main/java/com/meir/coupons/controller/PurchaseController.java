package com.meir.coupons.controller;

import com.meir.coupons.dto.Purchase;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.logic.PurchaseLogic;
import com.meir.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private PurchaseLogic purchaseLogic;

    @Autowired
    public PurchaseController(PurchaseLogic purchaseLogic) {
        this.purchaseLogic = purchaseLogic;
    }

    @PostMapping
    public void createPurchase(@RequestHeader String Authorization,@RequestBody Purchase purchase) throws ApplicationException {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        purchaseLogic.addPurchase(applicantId, purchase);
    }

    @GetMapping("/")
    public List<Purchase> getPurchases(@RequestHeader String Authorization) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return purchaseLogic.getPurchases(applicantId);
    }
    @GetMapping("/byUserId")
    public List<Purchase> getPurchasesByUserId(@RequestHeader String Authorization) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return purchaseLogic.getPurchaseByUserId(applicantId);
    }

    @GetMapping("/byCompanyId")
    public List<Purchase> getPurchaseByCompanyId(@RequestHeader String Authorization) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return purchaseLogic.getPurchaseByCompanyId(applicantId);
    }
    @GetMapping("/byCompanyId/{id}")
    public List<Purchase> getPurchaseByCompanyId(@RequestHeader String Authorization,@PathVariable("id") Integer requestedCompanyId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return purchaseLogic.getPurchaseByCompanyId(applicantId, requestedCompanyId);
    }


    @GetMapping("/byCouponId")
    public List<Purchase> getPurchaseByCouponId(@RequestHeader String Authorization,@RequestParam("couponId") Integer requestedCouponId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return purchaseLogic.getPurchaseByCouponId(applicantId, requestedCouponId);
    }
    @GetMapping("/{id}")
    public Purchase getPurchaseById(@RequestHeader String Authorization, @PathVariable("id") Integer requestedId) throws ApplicationException {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return purchaseLogic.getPurchaseById(applicantId, requestedId);
    }
}
