package guru.springframework.spring5recipeapp.service;

import guru.springframework.spring5recipeapp.command.RecipeCommand;
import guru.springframework.spring5recipeapp.converter.RecipeCommandToRecipe;
import guru.springframework.spring5recipeapp.converter.RecipeToRecipeCommand;
import guru.springframework.spring5recipeapp.model.Recipe;
import guru.springframework.spring5recipeapp.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService
{
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    private static final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand)
    {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes()
    {
        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().forEach(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Long id)
    {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
         if (recipeOptional.isEmpty())
         {
             throw new RuntimeException("Recipe not found");
         }

         return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(Long id)
    {
        return recipeToRecipeCommand.convert(findById(id));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand)
    {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved RecipeId:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public void deleteById(Long id)
    {
        recipeRepository.deleteById(id);
    }
}
