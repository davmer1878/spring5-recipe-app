package guru.springframework.spring5recipeapp.service;

import guru.springframework.spring5recipeapp.command.RecipeCommand;
import guru.springframework.spring5recipeapp.model.Recipe;

import java.util.Set;

public interface RecipeService
{
    Set<Recipe> getRecipes();
    Recipe findById(Long id);
    RecipeCommand findCommandById(Long id);
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    void deleteById(Long id);
}
