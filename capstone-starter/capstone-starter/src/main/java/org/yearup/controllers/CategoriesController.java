package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController {

    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // GET /categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    // GET /categories/{id}
    @GetMapping("{id}")
    public Category getById(@PathVariable int id) {
        Category category = categoryDao.getById(id);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return category;
    }

    // GET /categories/{categoryId}/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        return productDao.listByCategoryId(categoryId);
    }

    // POST /categories - Only ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category) {
        return categoryDao.create(category);
    }

    // PUT /categories/{id} - Only ADMIN
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        categoryDao.update(id, category);
    }

    // DELETE /categories/{id} - Only ADMIN
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id) {
        categoryDao.delete(id);
    }
}
