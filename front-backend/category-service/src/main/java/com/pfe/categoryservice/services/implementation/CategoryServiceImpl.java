package com.pfe.categoryservice.services.implementation;

import com.pfe.categoryservice.models.Category;
import com.pfe.categoryservice.repository.AttractionRepository;
import com.pfe.categoryservice.repository.CategoryRepository;
import com.pfe.categoryservice.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
// C'est une annotation de Spring Framework qui indique que cette classe est un bean de service.
// Les beans de service sont généralement utilisés pour implémenter la logique métier de l'application.
// Cette annotation permet à Spring de détecter automatiquement cette classe
// lors de la configuration et de la gestion des dépendances
@Transactional
public class CategoryServiceImpl implements CategoryService {

    //injection de dependance par constructeur
    final private CategoryRepository categoryRepository; //injection de repository dans l'interface
    final private AttractionRepository attractionRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               AttractionRepository attractionRepository) {
        this.categoryRepository = categoryRepository;
        this.attractionRepository = attractionRepository;

    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> FindAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category categoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("id not found"));
        // return attractionRepository.findById(id).orElse(null);
    }

    @Override
    public Category categoryUpdate(Long id, Category category) {
        //Category category1=categoryById(id);
        //if(category1 != null) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            category.setId(id);
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("error");
        }
    }

//    @Override
//    public String deleteCategory(Long id) {
//        Optional<Category> category = categoryRepository.findById(id);
//        if(category.isPresent()){
//        categoryRepository.deleteById(id);
//        return "deleted";
//
//    } else {
//            throw new RuntimeException("category not found");
//        }
//}
//}


    @Override
    public String deleteCategoryAndAttraction(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            // Suppression des attractions associées
            attractionRepository.deleteByCategoryId(id);
            // Suppression de la catégorie
            categoryRepository.deleteById(id);
            return "deleted";
        } else {
            throw new RuntimeException("category not found");
        }
    }
}