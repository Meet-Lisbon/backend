package pt.meetlisbon.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pt.meetlisbon.backend.controllers.AuthController;
import pt.meetlisbon.backend.models.entities.Category;
import pt.meetlisbon.backend.models.repository.CategoryRepository;
import pt.meetlisbon.backend.models.requests.RequestCategory;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    public void partialUpdate(RequestCategory requestCategory, Category oldCategory) {
        LOG.info("Updating place, these should match: {} and {}", requestCategory.getCatName(), oldCategory.getCatName());
        oldCategory.setCatName((requestCategory.getCatName() == null) ? oldCategory.getCatName() : requestCategory.getCatName());
        oldCategory.setCatIcon((requestCategory.getCatIcon() == null) ? oldCategory.getCatIcon() : requestCategory.getCatIcon());
        oldCategory.setUpdatedAt(OffsetDateTime.now());

        categoryRepository.save(oldCategory);
    }

}