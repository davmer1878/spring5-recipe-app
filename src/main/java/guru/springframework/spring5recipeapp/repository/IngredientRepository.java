package guru.springframework.spring5recipeapp.repository;

import guru.springframework.spring5recipeapp.model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, Long>
{
}
