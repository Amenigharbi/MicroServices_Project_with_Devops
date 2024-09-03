package com.pfe.categoryservice.controllers;


import com.pfe.categoryservice.models.Attraction;
import com.pfe.categoryservice.models.Category;
import com.pfe.categoryservice.services.AttractionService;
import com.pfe.categoryservice.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController

@RequestMapping("/categories")
public class CategoryController {
    //injection de service dans le controller
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttractionService attractionService;

    @PostMapping("/save")
    public Category create(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping("/all")
    public List<Category> allcat() {
        return categoryService.FindAllCategory();

    }

    @GetMapping("/getone/{id}")
    public Category getonebyId(@PathVariable Long id) {
        return categoryService.categoryById(id);
    }

    @PutMapping("/update/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.categoryUpdate(id, category);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteCategory(@PathVariable Long id) {
        Category c = categoryService.categoryById(id);
        HashMap<String, String> message = new HashMap<>();
        if (c != null) {
            try {

                List<Attraction> attractions = c.getAttractions();
                for (Attraction attraction : attractions) {
                    attractionService.deleteAttractionAndCommentsAndRes(attraction.getId());
                }
                categoryService.deleteCategoryAndAttraction(id);
                message.put("etat", "category deleted");
            } catch (Exception e) {
                message.put("etat", "Error: " + e.getMessage());
            }
        } else {
            message.put("etat", "category not found");
        }
        return message;
    }

//    @DeleteMapping("/delete/{id}")
//
//    public HashMap<String,String> deletecategory(@PathVariable Long id){
//        Category c=categoryService.categoryById(id);
//        HashMap message =  new HashMap();
//        if(c != null) {
//            try {
//                categoryService.deleteCategory(id);
//                message.put("etat", "category deleted");
//                return message;
//            } catch (Exception e) {
//                message.put("etat", "Error" + e.getMessage());
//                return message;
//            }
//        }else {
//            message.put("etat", "category not found");
//            return message;
//        }
//    }
}









