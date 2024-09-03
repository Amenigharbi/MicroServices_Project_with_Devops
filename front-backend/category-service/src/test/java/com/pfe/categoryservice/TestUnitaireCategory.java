package com.pfe.categoryservice;


import com.pfe.categoryservice.models.Category;
import com.pfe.categoryservice.repository.CategoryRepository;
import com.pfe.categoryservice.services.implementation.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TestUnitaireCategory {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
    }
    @Test
    public void testGetCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Category foundCategory = categoryService.categoryById(1L);
        assertNotNull(foundCategory);
        assertEquals("Test Category", foundCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }
    @Test
    public void testDeleteCategoryNotFound() {
        // Préparation des mocks pour simuler une catégorie non trouvée
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Appel de la méthode à tester qui doit lancer une exception
        assertThrows(RuntimeException.class, () -> {
            categoryService.deleteCategoryAndAttraction(1L);
        });
    }

        @Test
    public void testGetAllCategories() {
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        List<Category> allCategories = categoryService.FindAllCategory();
        assertNotNull(allCategories);
        assertEquals(1, allCategories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        category.setName("Updated Category");
        Category updatedCategory = categoryService.categoryUpdate(1L, category);
        assertNotNull(updatedCategory);
        assertEquals("Updated Category", updatedCategory.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testDeleteCategory() {
        // Préparation des mocks et des objets
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(1L);

        // Appel de la méthode à tester
        String result = categoryService.deleteCategoryAndAttraction(1L);

        // Vérification du résultat
        assertEquals("deleted", result);
        verify(categoryRepository, times(1)).deleteById(1L);
    }



}
