package com.pfe.categoryservice.services;

import com.pfe.categoryservice.models.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);
    List<Category>  FindAllCategory();
    Category categoryById(Long id);
    Category categoryUpdate(Long id , Category category);

    //String deleteCategory(Long id);
    String deleteCategoryAndAttraction(Long id);

}
