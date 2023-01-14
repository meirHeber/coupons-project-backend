package com.meir.coupons.logic;

import com.meir.coupons.dto.Company;
import com.meir.coupons.dto.Coupon;
import com.meir.coupons.entity.CategoryEntity;
import com.meir.coupons.entity.CompanyEntity;
import com.meir.coupons.entity.CouponEntity;
import com.meir.coupons.entity.UserEntity;
import com.meir.coupons.enums.UserTypes;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.repository.ICategoryRepository;
import com.meir.coupons.repository.ICompanyRepository;
import com.meir.coupons.repository.ICouponRepository;
import com.meir.coupons.repository.IUserRepository;
import com.meir.coupons.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.meir.coupons.enums.ErrorType.*;

@Service
public class CouponLogic {

    private ICouponRepository couponRepository;

    private UserLogic userLogic;
    private ICompanyRepository companyRepository;
    private ICategoryRepository categoryRepository;
    private IUserRepository userRepository;

    @Autowired
    public CouponLogic(ICouponRepository couponRepository,UserLogic userLogic, ICompanyRepository companyRepository, ICategoryRepository categoryRepository, IUserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.userLogic = userLogic;
        this.companyRepository = companyRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public void addCoupon(Integer applicantId, Coupon coupon) throws ApplicationException {
        UserEntity user = userRepository.findById(applicantId);
        if (user.getUserType() == UserTypes.company) {
            coupon.setCompany(new Company(user.getCompany().getId()));
        }
        validationForAddCoupon(applicantId, coupon);
        CompanyEntity companyEntity = companyRepository.findById(coupon.getCompany().getId());
        CategoryEntity categoryEntity = categoryRepository.findById(coupon.getCategory().getId());
        couponRepository.save(new CouponEntity(coupon, companyEntity, categoryEntity));
    }

    public Coupon getCouponById(Integer couponId) throws ApplicationException {
        validationForGetCoupon(couponId);
        return new Coupon(couponRepository.findById(couponId));
    }


    public List<Coupon> getCoupons() {
        List<CouponEntity> couponEntities = couponRepository.findAll();
        return convertCouponEntityListToCouponList(couponEntities);
    }

    public List<Coupon> getCouponsByCompanyUser(Integer applicantId) {
        List<Coupon> coupons = getCoupons();
        return filterForGetCouponsByCompanyUser(applicantId, coupons);
    }
    public long getCouponsSize(){
        return couponRepository.count();
    }
    public long getCouponsSize(Integer applicantId) throws ApplicationException {
        Integer companyId = userLogic.getUserById(applicantId, applicantId).getCompany().getId();
        return couponRepository.countByCompanyId(companyId);
    }

    public void updateCoupon(Integer applicantId, Coupon coupon) throws ApplicationException {
        UserEntity user = userRepository.findById(applicantId);
        if (user.getUserType() == UserTypes.company) {
            coupon.setCompany(new Company(user.getCompany().getId()));
        }
        validationForUpdateCoupon(applicantId, coupon);
        CompanyEntity companyEntity = companyRepository.findById(coupon.getCompany().getId());
        CategoryEntity categoryEntity = categoryRepository.findById(coupon.getCategory().getId());
        couponRepository.save(new CouponEntity(coupon, companyEntity, categoryEntity));
    }

    public void deleteById(Integer applicantId, Integer id) throws ApplicationException {
        validationForDeleteCoupon(applicantId, id);
        couponRepository.delete(couponRepository.findById(id));
    }

    public List<Coupon> getCouponsByPagesAndSorting(int pageNumber, int quantityPerPage, String sortParameter) {
        Page<CouponEntity> couponEntities = couponRepository.findAll(PageRequest.of(pageNumber, quantityPerPage, Sort.by(Sort.Direction.ASC, sortParameter)));
        List<Coupon> coupons = new ArrayList<>();
        for (CouponEntity couponEntity : couponEntities) {
            coupons.add(new Coupon(couponEntity));
        }
        return coupons;
    }

    //    public List<Coupon> test(int pageNumber, int quantityPerPage, String title) {
////        Page<CouponEntity> couponEntities = couponRepository.findAll(PageRequest.of(pageNumber, quantityPerPage, Sort.by(Sort.Direction.ASC, sortParameter)));
//        Pageable pageAndQuantity = PageRequest.of(pageNumber, quantityPerPage);
//        List<CouponEntity> couponEntities = couponRepository.findAllByTitle(title, pageAndQuantity);
////        String a = "s";
////        a.contains()
//
////        List<CouponEntity> couponEntities = couponRepository.findCouponsWithPaginationByTitle(title, pageNumber, quantityPerPage);
//        List<Coupon> coupons = new ArrayList<>();
//        for (CouponEntity couponEntity : couponEntities) {
//            coupons.add(new Coupon(couponEntity));
//        }
//        return coupons;
//    }
    public List<Coupon> paginationAndFilterBySortNotForCompanyUser(String containsBy, String containsValue, Integer pageNumber, Integer quantityPerPage, String sortBy) {
        return genericGetByPaginationAndFilterBySort(containsBy, containsValue, pageNumber, quantityPerPage, sortBy);
    }

    public List<Coupon> paginationAndFilterBySortOnlyForCompanyUser(Integer applicantId, String containsBy, String containsValue, Integer pageNumber, Integer quantityPerPage, String sortBy) {
        List<Coupon> allCoupons = genericGetByPaginationAndFilterBySort(containsBy, containsValue, pageNumber, quantityPerPage, sortBy);
        return filterForGetCouponsByCompanyUser(applicantId, allCoupons);
    }

    public List<Coupon> filterForGetCouponsByCompanyUser(Integer applicantId, List<Coupon> allCoupons) {
        UserEntity userEntity = userRepository.findById(applicantId);
        List<Coupon> companyCoupons = new ArrayList<>();
        for (Coupon coupon : allCoupons) {
            if (coupon.getCompany().getId() == userEntity.getCompany().getId()) {
                companyCoupons.add(coupon);
            }
        }
        return companyCoupons;
    }

    public List<Coupon> genericGetByPaginationAndFilterBySort(String containsBy, String containsValue, Integer pageNumber, Integer quantityPerPage, String sortBy) {
        Pageable page = PageRequest.of(pageNumber, quantityPerPage, Sort.by(sortBy));
        List<CouponEntity> couponEntities = null;
//        if (containsBy.equals("")) {
//            couponEntities = couponRepository.findByTitleContains(containsValue, page);
//        }
        if (containsBy.equals("title")) {
            couponEntities = couponRepository.findByTitleContains(containsValue, page);
        } else if (containsBy.equals("description")) {
            couponEntities = couponRepository.findByDescriptionContains(containsValue, page);
        } else if (containsValue == "") {
            couponEntities = couponRepository.findByTitleContains(containsValue, page);
        } else if (containsBy.equals("amount")) {
            couponEntities = couponRepository.findByAmountLessThan(Integer.parseInt(containsValue) + 1, page);
        } else if (containsBy.equals("price up to")) {
            couponEntities = couponRepository.findByPriceLessThan(Double.parseDouble(containsValue) + 1, page);
        } else if (containsBy.equals("price from")) {
            couponEntities = couponRepository.findByPriceGreaterThan(Double.parseDouble(containsValue) + 1, page);
        } else if (containsBy.equals("startDate")) {
            couponEntities = couponRepository.findByStartDate(Date.valueOf(containsValue), page);
        } else if (containsBy.equals("endDate")) {
            couponEntities = couponRepository.findByEndDate(Date.valueOf(containsValue), page);
        } else if (containsBy.equals("company")) {
            couponEntities = couponRepository.findByCompanyNameContains(containsValue, page);
        } else if (containsBy.equals("category")) {
            couponEntities = couponRepository.findByCategoryNameContains(containsValue, page);
        } else if (containsBy.equals("id")) {
            couponEntities = couponRepository.findById(Integer.parseInt(containsValue), page);
        } else {
            Page<CouponEntity> couponEntityPages = couponRepository.findAll(page);
            couponEntities = new ArrayList<>();
            for (CouponEntity couponEntityPage : couponEntityPages) {
                couponEntities.add(couponEntityPage);
            }
        }
        List<Coupon> coupons = new ArrayList<>();
        for (CouponEntity couponEntity : couponEntities) {
            coupons.add(new Coupon(couponEntity));
        }
        return coupons;
    }

    @PostConstruct
    public void deleteExpCoupons() {
        Date d = Date.valueOf(LocalDate.now());
        List<CouponEntity> couponEntities = couponRepository.findByEndDateBefore(d);
        for (CouponEntity couponEntity : couponEntities) {
            couponRepository.delete(couponEntity);
        }
    }

    private List<Coupon> convertCouponEntityListToCouponList(List<CouponEntity> couponEntities) {
        List<Coupon> coupons = new ArrayList<>();
        for (CouponEntity couponEntity : couponEntities) {
            coupons.add(new Coupon(couponEntity));
        }
        return coupons;
    }


    ///////////////validations------------------------
    public void validationForGetCoupon(Integer couponId) throws ApplicationException {
        if (couponId == null) {
            throw new ApplicationException(INVALID_ID);
        }
        if (!couponRepository.existsById(couponId)) {
            throw new ApplicationException(ID_DOES_NOT_EXIST);
        }
    }

    private void validationForDeleteCoupon(Integer applicantId, Integer couponId) throws ApplicationException {
        validationForGetCoupon(couponId);
        ValidationUtils.authorizationValidation(applicantId, UserTypes.company, UserTypes.admin);
        CouponEntity couponEntity = couponRepository.findById(couponId);
        UserEntity userEntity = userRepository.findById(applicantId);
        if (userEntity.getUserType() != UserTypes.admin) {
            if (userEntity.getUserType() != UserTypes.company) {
                throw new ApplicationException(NO_AUTHORIZED);
            }
            if (userEntity.getCompany().getId() != couponEntity.getCompany().getId()) {
                throw new ApplicationException(NO_AUTHORIZED);
            }
        }
    }

    private void validationForUpdateCoupon(Integer applicantId, Coupon coupon) throws ApplicationException {
        validationForDeleteCoupon(applicantId, coupon.getId());
        validationForAllParametersExceptId(coupon);
    }

    private void validationForAddCoupon(Integer applicantId, Coupon coupon) throws ApplicationException {
//        validationForDeleteCoupon(applicantId, coupon.getId());
        if (coupon.getId() != null) {
            throw new ApplicationException(IMPOSSIBLE_TO_PROVIDE_ID);
        }
        validationForAllParametersExceptId(coupon);
    }

    private void validationForAllParametersExceptId(Coupon coupon) throws ApplicationException {
        if (coupon.getAmount() == null || coupon.getAmount() <= 0) {
            throw new ApplicationException(INVALID_AMOUNT);
        }
        if (coupon.getStartDate() == null || coupon.getEndDate() == null) {
            throw new ApplicationException(INVALID_DATES);
        }
        if (coupon.getPrice() == null || coupon.getPrice() <= 0) {
            throw new ApplicationException(INVALID_PRICE);
        }
        if (coupon.getCompany().getId() == null || !companyRepository.existsById(coupon.getCompany().getId())) {
            throw new ApplicationException(INVALID_COMPANY_ID);
        }
        if (coupon.getCategory().getId() == null || !categoryRepository.existsById(coupon.getCategory().getId())) {
            throw new ApplicationException(INVALID_CATEGORY_ID);
        }
        if (coupon.getDescription() != null) {
            try {
                ValidationUtils.isTextLengthLegal(coupon.getDescription(), 100, 2);
            } catch (ApplicationException e) {
                throw new ApplicationException(INVALID_DESCRIPTION);
            }
        }
        try {
            ValidationUtils.isTextLengthLegal(coupon.getTitle(), 50, 2);
        } catch (ApplicationException e) {
            throw new ApplicationException(INVALID_COUPON_TITLE);
        }
    }

}
