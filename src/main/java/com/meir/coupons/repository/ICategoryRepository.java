package com.meir.coupons.repository;

import com.meir.coupons.entity.CategoryEntity;
import com.meir.coupons.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
    public CategoryEntity findById(Integer id);
    public boolean existsById(Integer id);

    public boolean existsByName(String name);

    public CategoryEntity findByName(String name);
    public CategoryEntity findByNameContains(String name);


    @Query("SELECT id FROM CategoryEntity c WHERE c.name= :name")
    public Integer findIdByName(@Param("name") String name);

}
