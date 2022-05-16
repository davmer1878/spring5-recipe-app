package guru.springframework.spring5recipeapp.service;

import guru.springframework.spring5recipeapp.model.Recipe;

import java.util.Set;

public interface RecipeService
{
    Set<Recipe> getRecipes();
}
