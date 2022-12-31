package pt.meetlisbon.backend.models.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.meetlisbon.backend.models.entities.Category;

import java.util.UUID;


@Repository
public interface CategoryRepository extends CrudRepository<Category, UUID> {
    Category findCategoryByCatNameEquals(String catName);

}
