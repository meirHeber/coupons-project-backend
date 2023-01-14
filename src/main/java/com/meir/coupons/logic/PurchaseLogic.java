package com.meir.coupons.logic;

import com.meir.coupons.dto.Coupon;
import com.meir.coupons.dto.Customer;
import com.meir.coupons.dto.Purchase;
import com.meir.coupons.dto.User;
import com.meir.coupons.entity.*;
import com.meir.coupons.enums.UserTypes;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.repository.*;
import com.meir.coupons.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.meir.coupons.enums.ErrorType.*;

@Service
public class PurchaseLogic {
    private IPurchaseRepository purchaseRepository;
    private ICouponRepository couponRepository;
    private ICustomerRepository customerRepository;
    private ICompanyRepository companyRepository;
    private IUserRepository userRepository;
    private CouponLogic couponLogic;
    private UserLogic userLogic;
    private CompanyLogic companyLogic;

    @Autowired
    public PurchaseLogic(IPurchaseRepository purchaseRepository, ICouponRepository couponRepository, ICustomerRepository customerRepository, ICompanyRepository companyRepository, IUserRepository userRepository, CouponLogic couponLogic, UserLogic userLogic, CompanyLogic companyLogic) {
        this.purchaseRepository = purchaseRepository;
        this.couponRepository = couponRepository;
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.couponLogic = couponLogic;
        this.userLogic = userLogic;
        this.companyLogic = companyLogic;
    }

    @Transactional
    public void addPurchase(Integer applicantId, Purchase purchase) throws ApplicationException {
        purchase.setCustomer(new Customer(applicantId));
        validationForAddPurchase(applicantId, purchase);
        CouponEntity couponEntity = couponRepository.findById(purchase.getCoupon().getId());
        purchase.setTotalPrice(couponRepository.findById(purchase.getCoupon().getId()).getPrice() * purchase.getAmount());
        CustomerEntity customerEntity = new CustomerEntity(purchase.getCustomer());
        couponEntity.setAmount(couponEntity.getAmount() - purchase.getAmount());
        couponRepository.save(couponEntity);
        purchaseRepository.save(new PurchaseEntity(purchase, couponEntity, customerEntity));
    }
    public Purchase getPurchaseById(Integer applicantId,Integer id) throws ApplicationException {
        validationForGetPurchase(applicantId, id);
        return new Purchase(purchaseRepository.findById(id));
    }
    public List<Purchase> getPurchaseByUserId(Integer applicantId) throws Exception {
        validationForGetPurchaseUserById(applicantId);
        CustomerEntity customerEntity = customerRepository.findById(applicantId);
        List<PurchaseEntity> purchaseEntities = purchaseRepository.findByUserId(customerEntity);
        return convertPurchaseEntityListToPurchaseList(purchaseEntities);
    }

    private void validationForGetPurchaseUserById(Integer applicantId) throws ApplicationException {
        if (!userRepository.existsById(applicantId)) {
            throw new ApplicationException(LOGIN_FAILED);
        }
        if (userRepository.findById(applicantId).getUserType() != UserTypes.customer) {
            throw new ApplicationException(NO_AUTHORIZED);
        }
    }

    public List<Purchase> getPurchaseByCompanyId(Integer applicantId) throws ApplicationException {
        User user = userLogic.getUserById(applicantId, applicantId);
        return getPurchaseByCompanyId(applicantId,user.getCompany().getId());
    }
    public List<Purchase> getPurchaseByCompanyId(Integer applicantId, int companyId) throws ApplicationException {
        validationsForGetPurchasesByCompany(applicantId, companyId);
        CompanyEntity companyEntity = companyRepository.findById(companyId);
        List<PurchaseEntity> purchaseEntities = purchaseRepository.findByCompanyId(companyEntity);
        return convertPurchaseEntityListToPurchaseList(purchaseEntities);
    }

    public List<Purchase> getPurchaseByCouponId(Integer applicantId, int couponId) throws Exception {
       Coupon coupon = couponLogic.getCouponById(couponId);
       companyLogic.validationForGetAndDeleteCompanyById(applicantId, coupon.getCompany().getId());
       CouponEntity couponEntity = couponRepository.findById(couponId);
        List<PurchaseEntity> purchaseEntities = purchaseRepository.findByCouponId(couponEntity);
        return convertPurchaseEntityListToPurchaseList(purchaseEntities);
    }
    public List<Purchase> getPurchases(Integer applicantId) throws Exception {
        ValidationUtils.authorizationValidation(applicantId, UserTypes.admin);
        List<PurchaseEntity> purchaseEntities = purchaseRepository.findAll();
        return convertPurchaseEntityListToPurchaseList(purchaseEntities);
    }
    private List<Purchase> convertPurchaseEntityListToPurchaseList(List<PurchaseEntity> purchaseEntities) {
        List<Purchase> purchases = new ArrayList<>();
        for (PurchaseEntity purchaseEntity : purchaseEntities) {
            purchases.add(new Purchase(purchaseEntity));
        }
        return purchases;
    }

    /////////////validations-------------------------------------------
    private void validationForGetPurchase(Integer applicantId, Integer id) throws ApplicationException {
        if (id == null) {
            throw new ApplicationException(INVALID_ID);
        }
        if (userRepository.findById(applicantId).getUserType() == UserTypes.customer && purchaseRepository.findById(id).getCustomer().getId() != applicantId){
          throw new ApplicationException(NO_AUTHORIZED);
        }
        if (!userRepository.existsById(id)) {
            throw new ApplicationException(ID_DOES_NOT_EXIST);
        }
    }
    private void validationForAddPurchase(Integer applicantId, Purchase purchase) throws ApplicationException {
        if (purchase.getId() != null) {
            throw new ApplicationException(IMPOSSIBLE_TO_PROVIDE_ID);
        }
        if (applicantId != purchase.getCustomer().getId()){
            throw new ApplicationException(NO_AUTHORIZED);
        }
        validationForAllParametersExceptId(purchase);
    }
    private void validationForAllParametersExceptId(Purchase purchase) throws ApplicationException {
        if (purchase.getAmount() <= 0 || purchase.getAmount() == null) {
            throw new ApplicationException(INVALID_AMOUNT);
        }
        couponLogic.validationForGetCoupon(purchase.getCoupon().getId());
//        userLogic.validationForGetAndDeleteUserById(purchase.getUserId());
        if (userRepository.findUserTypeById(purchase.getCustomer().getId()) != UserTypes.customer){
            throw new ApplicationException(USER_TYPE_WHO_CANNOT_MAKE_A_PURCHASE);
        }

        if (couponRepository.findAmountById(purchase.getCoupon().getId()) < purchase.getAmount()) {
            throw new ApplicationException(NOT_ENOUGH_COUPONS_LEFT);
        }
        if (couponRepository.findEndDateById(purchase.getCoupon().getId()).before(Date.valueOf(LocalDate.now()))) {
            throw new ApplicationException(COUPON_EXPIRED);
        }
        if (couponRepository.findStartDateById(purchase.getCoupon().getId()).after(Date.valueOf(LocalDate.now()))) {
            throw new ApplicationException(COUPON_NOT_VALID_YET);
        }
    }
    private void validationsForGetPurchasesByCompany(Integer applicantId, int companyId) throws ApplicationException {
        User user = userLogic.getUserById(applicantId, applicantId);
        if(user.getUserType() != UserTypes.admin && (user.getUserType() != UserTypes.company || user.getCompany().getId() != companyId)){
            throw new ApplicationException(NO_AUTHORIZED);
        }
    }
}
