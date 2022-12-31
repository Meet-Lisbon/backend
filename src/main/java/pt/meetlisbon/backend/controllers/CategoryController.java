package pt.meetlisbon.backend.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pt.meetlisbon.backend.exceptions.AlreadyExistsException;
import pt.meetlisbon.backend.exceptions.NotFoundException;
import pt.meetlisbon.backend.models.entities.Category;
import pt.meetlisbon.backend.models.repository.CategoryRepository;
import pt.meetlisbon.backend.models.requests.RequestCategory;
import pt.meetlisbon.backend.services.CategoryService;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {
    final CategoryRepository categoryRepository;
    final CategoryService categoryService;
    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    @GetMapping("/all")
    public Iterable<Category> getAll() {
        return categoryRepository.findAll();
    }

    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        categoryRepository.deleteAll();
    }

    @GetMapping
    public Category findByName(@RequestParam String categoryName) {
        LOG.info("Searching for category: {}", categoryName);
        Category category = categoryRepository.findCategoryByCatNameEquals(categoryName);
        if(category == null) throw new NotFoundException("Category");
        return category;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void deleteCategoryByName(@RequestParam String categoryName) {
        LOG.info("Searching for category: {}", categoryName);
        Category category = categoryRepository.findCategoryByCatNameEquals(categoryName);
        if(category == null) throw new NotFoundException("Category");
        categoryRepository.delete(category);
    }

    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody RequestCategory requestCategory) {
        if(categoryRepository.findCategoryByCatNameEquals(requestCategory.getCatName()) != null)
            throw new AlreadyExistsException("Category");
        String iconUrl;
        if(requestCategory.getCatIcon() == null) iconUrl = "https://i.imgur.com/n9cCMNS.png";
        else iconUrl = requestCategory.getCatIcon();

        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setCatName(requestCategory.getCatName());
        category.setCatIcon(iconUrl);
        category.setCreatedAt(OffsetDateTime.now());
        category.setUpdatedAt(OffsetDateTime.now());
        categoryRepository.save(category);
    }

    @PatchMapping("/update/{categoryName}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@PathVariable String categoryName, @RequestBody RequestCategory requestCategory) {
        Category category = categoryRepository.findCategoryByCatNameEquals(categoryName);
        if (category == null) {
            throw new NotFoundException("Category");
        }
        categoryService.partialUpdate(requestCategory, category);

    }
}