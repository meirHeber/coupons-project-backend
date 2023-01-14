package com.meir.coupons.repository;

import com.meir.coupons.entity.CompanyEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompanyRepository extends CrudRepository<CompanyEntity, Long> {
    public CompanyEntity findById(Integer id);

    public boolean existsById(Integer id);

    public boolean existsByName(String name);

    public CompanyEntity findByName(String name);

    public CompanyEntity findByNameContains(String name);

    @Query("SELECT id FROM CompanyEntity c WHERE c.name= :name")
    public Integer findIdByName(@Param("name") String name);
}
