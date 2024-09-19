package com.ecomm.jun;

import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.repository.ProductRepository;
import com.ecomm.jun.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JunApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private ProductService productService;

	private Product product;
	private Category category;

	@BeforeEach
	void setup() {
		product = new Product();
		product.setId(1L);
		product.setName("Test ürünü");
		product.setImagePath("");
		product.setPrice(15.0);

		category = new Category();
		category.setId(1L);
		category.setName("Erkek");

		Set<Category> categories = new HashSet<>();
		categories.add(category);

		product.setCategories(categories);
	}

	@Test
	@DisplayName("Repository Test")
	void testFindByName() {
		when(productRepository.findByName("Test ürünü")).thenReturn(Optional.of(product));
		when(productService.findByName("Test ürünü")).thenReturn(product);

		Product found = productService.findByName("Test ürünü");

		assert found.getId().equals(product.getId());
	}

	@Test
	@DisplayName("Service Test")
	void saveProductTest() {

		when(productService.save(any(ProductRequest.class))).thenReturn(product);
		Set<Long> categoryIds = new HashSet<>();
		categoryIds.add(1L);
		Product saved = productService.save(new ProductRequest("Test ürünü", "", 15.0, 0.0, categoryIds));
		assert saved.getName().equals(product.getName());
	}

	@Test
	@DisplayName("Controller Test")
	void findByIdTest() {

		when(productService.findById(1L)).thenReturn(product);

		mockMvc.perform(get("/shop/1"))
				.andExpect(status().isOk())
				.andExpect()

	}



}
