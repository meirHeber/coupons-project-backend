package com.meir.coupons.controller;

import com.meir.coupons.dto.Category;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.logic.CategoryLogic;
import com.meir.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryLogic categoryLogic;

    @Autowired
    public CategoryController(CategoryLogic categoryLogic) {
        this.categoryLogic = categoryLogic;
    }

    @PostMapping
    public void createCategory(@RequestHeader String Authorization, @RequestBody Category category) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        categoryLogic.addCategory(applicantId, category);
    }



    @GetMapping("/byName")
    public Category getCategoryByName(@RequestParam("name") String name) throws ApplicationException {
        return categoryLogic.getCategoryByName(name);
    }
    @GetMapping("/")
    public List<Category> getCategories() {
      return categoryLogic.getCategories();
    }
    @PutMapping
    public void updateCategory(@RequestHeader String Authorization, @RequestBody Category category) throws Exception {
        Integer applicantId = JWTUtils.getIdByToken(Authorization);
        categoryLogic.updateCategory(applicantId, category);
    }
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable("id") Integer id) throws ApplicationException {
        return categoryLogic.getCategoryById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Integer id) throws ApplicationException {
        categoryLogic.deleteCategory(id);
    }
}
