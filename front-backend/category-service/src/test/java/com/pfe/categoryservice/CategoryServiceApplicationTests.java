package com.pfe.categoryservice;

import com.pfe.categoryservice.models.Attraction;
import com.pfe.categoryservice.models.Category;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceApplicationTests {

	@Test
	public void testCreateCategory() {
		// Créer une instance de Category
		Category category = new Category();

		// Initialiser les champs
		category.setId(1L);
		category.setName("Category Name");
		category.setDescription("Category Description");

		// Vérifier si les champs sont correctement initialisés
		assertNotNull(category);
		assertEquals(1L, category.getId().longValue());
		assertEquals("Category Name", category.getName());
		assertEquals("Category Description", category.getDescription());
	}
	@Test
	public void testAttractionsRelation() {
		// Créer une instance de Category
		Category category = new Category();
		category.setAttractions(new ArrayList<>());

		// Créer une instance d'attraction
		Attraction attraction = new Attraction();
		attraction.setName("Attraction Name");
		attraction.setDescription("Attraction Description");

		// Définir la catégorie de l'attraction
		attraction.setCategory(category);

		// Ajouter l'attraction à la liste des attractions de la catégorie
		category.getAttractions().add(attraction);

		// Vérifier si la relation est correctement établie
		assertNotNull(category.getAttractions());
		assertNotNull(attraction.getCategory());
		assertEquals(category, attraction.getCategory());
	}
	@Test
	public void testGetCategoryById() {
		// ID de la catégorie à récupérer
		Long categoryId = 1L;
		TestRestTemplate restTemplate = new TestRestTemplate();

		// Spécifier l'URL complète avec le protocole, l'hôte et le port
//	 String url = "http://localhost:8085/categories/getone/" + categoryId;
		String url = "http://localhost:8089/CATEGORY-SERVICE/categories/getone/"+ categoryId;
		// Envoyer une requête GET pour récupérer la catégorie par ID
		ResponseEntity<Category> response = restTemplate.getForEntity(url, Category.class);

		// Vérifier que la réponse est un succès (code 200)
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// Vérifier que la catégorie renvoyée correspond à celle attendue
		Category category = response.getBody();
		assertNotNull(category);
		assertEquals(categoryId, category.getId());
	}

	@Test
	public void testUpdateCategory() {
		// Créer une nouvelle catégorie à mettre à jour
		Category categoryToUpdate = new Category();
		categoryToUpdate.setName("CategoryToUpdate");
		categoryToUpdate.setDescription("Description de la catégorie à mettre à jour");

		// Envoyer une requête POST pour créer la catégorie à mettre à jour
		TestRestTemplate restTemplate = new TestRestTemplate();

		// Spécifier l'URL complète avec le protocole, l'hôte et le port
		String createUrl = "http://localhost:8085/categories/save";
		ResponseEntity<Category> createResponse = restTemplate.postForEntity(createUrl, categoryToUpdate, Category.class);
		Category createdCategory = createResponse.getBody();
		assertNotNull(createdCategory);

		// Mettre à jour les données de la catégorie
		createdCategory.setName("UpdatedCategoryName");
		createdCategory.setDescription("Description mise à jour de la catégorie");

		// Envoyer une requête PUT pour mettre à jour la catégorie
		String updateUrl = "http://localhost:8085/categories/update/" + createdCategory.getId();
		restTemplate.put(updateUrl, createdCategory);

		// Récupérer la catégorie mise à jour
		String getUrl = "http://localhost:8085/categories/getone/" + createdCategory.getId();
		ResponseEntity<Category> updatedResponse = restTemplate.getForEntity(getUrl, Category.class);
		Category updatedCategory = updatedResponse.getBody();

		// Vérifier que la catégorie a été mise à jour avec succès
		assertEquals(HttpStatus.OK, updatedResponse.getStatusCode());
		assertEquals("UpdatedCategoryName", updatedCategory.getName());
		assertEquals("Description mise à jour de la catégorie", updatedCategory.getDescription());
	}
//	@Test
//	public void testDeleteCategory() {
//		// Créer une nouvelle catégorie à supprimer
//		Category categoryToDelete = new Category();
//		categoryToDelete.setName("CategoryToDelete");
//		categoryToDelete.setDescription("Description de la catégorie à supprimer");
//		TestRestTemplate restTemplate = new TestRestTemplate();
//		// Envoyer une requête POST pour créer la catégorie à supprimer
//		ResponseEntity<Category> createResponse = restTemplate.postForEntity("http://localhost:8089/CATEGORY-SERVICE/categories/save", categoryToDelete, Category.class);
//		Category createdCategory = createResponse.getBody();
//		assertNotNull(createdCategory);
//
//		// Envoyer une requête DELETE pour supprimer la catégorie
//		restTemplate.delete("http://localhost:8089/CATEGORY-SERVICE/categories/delete/" + createdCategory.getId());
//
//		// Vérifier que la catégorie a été supprimée avec succès
//		ResponseEntity<Category> response = restTemplate.getForEntity("http://localhost:8089/CATEGORY-SERVICE/categories/getone/" + createdCategory.getId(), Category.class);
//		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//	}
	@Test
	public void testResponseTime() {
		long startTime = System.currentTimeMillis();

		// Envoyer la requête
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<Category> response = restTemplate.getForEntity("http://localhost:8089/CATEGORY-SERVICE/categories/getone/1", Category.class);

		long endTime = System.currentTimeMillis();
		long responseTime = endTime - startTime;

		// Afficher le temps de réponse
		System.out.println("Temps de réponse : " + responseTime + " ms");

		// Vérifier si le temps de réponse est inférieur à un certain seuil
		assertTrue(responseTime < 1000); // Exemple : Vérification si le temps de réponse est inférieur à 1 seconde
	}






}
