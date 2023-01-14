package com.meir.coupons.repository;

import com.meir.coupons.entity.CouponEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface ICouponRepository extends JpaRepository<CouponEntity, Long> {
    public CouponEntity findById(Integer id);

    public boolean existsById(Integer id);

    @Query("SELECT amount FROM CouponEntity c WHERE c.id= :id")
    public Integer findAmountById(@Param("id") Integer id);

    @Query("SELECT endDate FROM CouponEntity c WHERE c.id= :id")
    public Date findEndDateById(@Param("id") Integer id);

    @Query("SELECT startDate FROM CouponEntity c WHERE c.id= :id")
    public Date findStartDateById(@Param("id") Integer id);

    public List<CouponEntity> findByEndDateBefore(Date date);

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM CouponEntity c WHERE c.endDate < :date")
//    public void removeByEndDateBefore(@Param("date") Date date);


    List<CouponEntity> findByTitleContains(String title, Pageable pageable);



    List<CouponEntity> findByStartDate(Date startDate, Pageable pageable);

    List<CouponEntity> findByEndDate(Date endDate, Pageable pageable);

    List<CouponEntity> findById(Integer id, Pageable pageable);

    long count();

    @Query("SELECT COUNT(c) FROM CouponEntity c WHERE c.company.id= :companyId")
    long countByCompanyId(Integer companyId);


    @Query("SELECT c FROM CouponEntity c WHERE c.company.name LIKE %:name%")
    List<CouponEntity> findByCompanyNameContains(String name, Pageable pageable);

    @Query("SELECT c FROM CouponEntity c WHERE c.category.name LIKE %:name%")
    List<CouponEntity> findByCategoryNameContains(String name, Pageable pageable);





    List<CouponEntity> findByAmountLessThan(Integer amount, Pageable pageable);

    List<CouponEntity> findByPriceLessThan(Double price, Pageable pageable);
    List<CouponEntity> findByPriceGreaterThan(Double price, Pageable pageable);


    List<CouponEntity> findByDescriptionContains(String description, Pageable pageable);


//    @Query("SELECT c FROM CouponEntity c WHERE c.title LIKE %:title% order by :order")
//    List<CouponEntity> searchByTitleLike(@Param("title") String title, @Param("order") String order);

}
