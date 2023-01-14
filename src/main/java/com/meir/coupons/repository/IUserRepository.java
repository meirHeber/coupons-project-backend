package com.meir.coupons.repository;

import com.meir.coupons.dto.UserLoginData;
import com.meir.coupons.entity.UserEntity;
import com.meir.coupons.enums.UserTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findById(Integer id);
    public UserEntity findByUsername(String username);
    public boolean existsById(Integer id);
    public boolean existsByUsername(String username);
    @Query("SELECT u FROM UserEntity u WHERE u.id= :id and u.username= :username")
    public UserEntity findByIdAndUsername(@Param("id") Integer id , @Param("username") String name);

    @Query("SELECT id FROM UserEntity u WHERE u.username= :username")
    public Integer findIdByUsername(@Param("username") String name);

//    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.password = :password")
//    Optional<UserEntity> login(@Param("username") String username, @Param("password") String password);


    @Query(value = "SELECT new com.meir.coupons.dto.UserLoginData(u.id, u.userType, u.firstName, u.company.id) "
            + "FROM UserEntity u WHERE u.username = :username AND u.password = :password")
    Optional<UserLoginData> login(@Param("username") String username, @Param("password") String password);



    @Query("SELECT userType FROM UserEntity u WHERE u.id= :id")
    public UserTypes findUserTypeById(@Param("id")Integer id);


    @Modifying
    @Query("delete FROM UserEntity u WHERE u.id= :id")
    void deleteById(@Param("id")Integer id);
}
