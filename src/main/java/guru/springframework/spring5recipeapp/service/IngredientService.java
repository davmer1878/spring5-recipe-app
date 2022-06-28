package guru.springframework.spring5recipeapp.service;

import guru.springframework.spring5recipeapp.command.IngredientCommand;

public interface IngredientService
{
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
    IngredientCommand saveIngredientCommand(IngredientCommand command);
    void deleteById(Long recipeId, Long idToDelete);
}
