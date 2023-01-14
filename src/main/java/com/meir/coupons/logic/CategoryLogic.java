package com.meir.coupons.logic;


import com.meir.coupons.dto.Category;
import com.meir.coupons.entity.CategoryEntity;
import com.meir.coupons.enums.ErrorType;
import com.meir.coupons.enums.UserTypes;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.repository.ICategoryRepository;
import com.meir.coupons.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.meir.coupons.enums.ErrorType.*;

@Service
public class CategoryLogic {

    private ICategoryRepository categoryRepository;

    @Autowired
    public CategoryLogic(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void addCategory(Integer applicantId, Category category) throws Exception {
        validationForAddCategory(applicantId, category);
        categoryRepository.save(new CategoryEntity(category));
    }
    public Category getCategoryById(Integer categoryId) throws ApplicationException {
        validationForGetCategoryById(categoryId);
        return new Category(categoryRepository.findById(categoryId));
    }
    public Category getCategoryByName(String name) throws ApplicationException {
        validationForGetCategoryByName(name);
        return new Category(categoryRepository.findByName(name));
    }
    public List<Category> getCategories() {
        List<CategoryEntity> categoryEntityList = (List<CategoryEntity>) categoryRepository.findAll();
        List<Category> categoryList = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryEntityList) {
            Category category = new Category(categoryEntity);
            categoryList.add(category);
        }
        return categoryList;
    }
    public void updateCategory(Integer applicantId, Category category) throws Exception {
        validationForUpdateCategory(applicantId, category);
        categoryRepository.save(new CategoryEntity(category));
    }
    public void deleteCategory(Integer id) throws ApplicationException {
        ///Already passed the validations of the "get" function
        categoryRepository.delete(new CategoryEntity(this.getCategoryById(id)));
    }

    //////////////validations-----------
    private void validationForGetCategoryById(Integer id) throws ApplicationException {
        if (id == null) {
            throw new ApplicationException(ErrorType.INVALID_ID);
        }
        if (!categoryRepository.existsById(id)) {
            throw new ApplicationException(ErrorType.ID_DOES_NOT_EXIST);
        }
    }

    private void validationForGetCategoryByName(String name) throws ApplicationException {
        if (name == null) {
            throw new ApplicationException(INVALID_NAME);
        }
        if (!categoryRepository.existsByName(name)) {
            throw new ApplicationException(ErrorType.NAME_DOES_NOT_EXIST);
        }
    }

    private void validationForAddCategory(Integer applicantId, Category category) throws ApplicationException {
        ValidationUtils.authorizationValidation(applicantId, UserTypes.admin);
        if (category.getId() != null) {
            throw new ApplicationException(ErrorType.IMPOSSIBLE_TO_PROVIDE_ID);
        }
        validationForAllParametersExceptId(category);
    }
    private void validationForUpdateCategory(Integer applicantId,Category category) throws ApplicationException {
        ValidationUtils.authorizationValidation(applicantId, UserTypes.admin);
        validationForGetCategoryById(category.getId());
        if (categoryRepository.existsByName(category.getName()) && categoryRepository.findIdByName(category.getName()) != category.getId()) {
            throw new ApplicationException(NAME_ALREADY_EXISTS);
        }
        validationForAllParametersExceptId(category);
    }
    private void validationForAllParametersExceptId(Category category) throws ApplicationException {
        try {
            ValidationUtils.isTextLengthLegal(category.getName(), 10, 2);
        } catch (ApplicationException e) {
            throw new ApplicationException(INVALID_NAME);
        }
    }
}
