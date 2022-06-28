package guru.springframework.spring5recipeapp.service;

import guru.springframework.spring5recipeapp.command.IngredientCommand;
import guru.springframework.spring5recipeapp.converter.IngredientCommandToIngredient;
import guru.springframework.spring5recipeapp.converter.IngredientToIngredientCommand;
import guru.springframework.spring5recipeapp.model.Ingredient;
import guru.springframework.spring5recipeapp.model.Recipe;
import guru.springframework.spring5recipeapp.repository.IngredientRepository;
import guru.springframework.spring5recipeapp.repository.RecipeRepository;
import guru.springframework.spring5recipeapp.repository.UnitOfMeasureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService
{
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientRepository ingredientRepository;

    private static final Logger log = LoggerFactory.getLogger(IngredientServiceImpl.class);

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository,
                                 IngredientRepository ingredientRepository)
    {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId)
    {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty())
        {
            //todo impl error handling
            log.error("Recipe not found with Id: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if (ingredientCommandOptional.isEmpty())
        {
            //todo impl error handling
            log.error("Ingredient not found with Id: " + ingredientId);
        }

        return ingredientCommandOptional.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand)
    {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

        if (recipeOptional.isEmpty())
        {
            //todo toss error if not found!
            log.error("Recipe not found for id: " + ingredientCommand.getRecipeId());
            return new IngredientCommand();
        }
        else
        {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent())
            {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
                        .findById(ingredientCommand.getUnitOfMeasure().getId())
                        .orElseThrow(() -> new RuntimeException("Unit Of Measure not found"))); //todo address this
            }
            else
            {
                //add new Ingredient
                recipe.addIngredient(ingredientCommandToIngredient.convert(ingredientCommand));
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            //check by description
            if (savedIngredientOptional.isEmpty())
            {
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(ingredientCommand.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredientCommand.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(ingredientCommand.getUnitOfMeasure().getId()))
                        .findFirst();
            }

            //to do check for fail
            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long recipeId, Long idToDelete)
    {
        log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isPresent())
        {
            Recipe recipe = recipeOptional.get();
            log.debug("found recipe");

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(idToDelete))
                    .findFirst();

            if (ingredientOptional.isPresent())
            {
                log.debug("found Ingredient");
                Ingredient ingredientToDelete = ingredientOptional.get();
                recipe.getIngredients().remove(ingredientOptional.get());
                ingredientRepository.delete(ingredientToDelete);
            }
        }
        else
        {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }
    }
}
