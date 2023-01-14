package com.meir.coupons.controller;

import com.meir.coupons.dto.Category;
import com.meir.coupons.dto.Company;
import com.meir.coupons.dto.User;
import com.meir.coupons.logic.CategoryLogic;
import com.meir.coupons.logic.CompanyLogic;
import com.meir.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private CompanyLogic companyLogic;

    @Autowired
    public CompanyController(CompanyLogic companyLogic) {
        this.companyLogic = companyLogic;
    }

    @PostMapping
    public void createCategory(@RequestHeader String Authorization, @RequestBody Company company) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        companyLogic.addCompany(applicantId, company);
    }


    @GetMapping("/byName")
    public Company getCompanyByName(@RequestHeader String Authorization, @RequestParam("name") String name) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return companyLogic.getCompanyByName(applicantId, name);
    }
    @GetMapping("/usersByCompanyId")
    public List<User> getUsersByCompanyId(@RequestHeader String Authorization, @RequestParam("id") Integer requestedId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return companyLogic.getUsersByCompanyId(applicantId, requestedId);
    }
    @GetMapping("/")
    public List<Company> getCompanies() throws Exception {
        return companyLogic.getCompanies();
    }
    @GetMapping("/{id}")
    public Company getCompanyById(@RequestHeader String Authorization, @PathVariable("id") Integer requestedId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        return companyLogic.getCompanyById(applicantId, requestedId);
    }


    @PutMapping
    public void updateCompany(@RequestHeader String Authorization, @RequestBody Company companyForUpdate) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        companyLogic.updateCompany(applicantId, companyForUpdate);
    }
    @DeleteMapping("/{id}")
    public void deleteCompany(@RequestHeader String Authorization, @PathVariable("id") Integer requestedId) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        companyLogic.deleteCompanyById(applicantId, requestedId);
    }
}
