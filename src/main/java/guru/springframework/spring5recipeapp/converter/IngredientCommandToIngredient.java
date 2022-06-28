package guru.springframework.spring5recipeapp.converter;

import guru.springframework.spring5recipeapp.command.IngredientCommand;
import guru.springframework.spring5recipeapp.model.Ingredient;
import guru.springframework.spring5recipeapp.model.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient>
{
    private final UnitOfMeasureCommandToUnitOfMeasure uomConverter;

    public IngredientCommandToIngredient(UnitOfMeasureCommandToUnitOfMeasure uomConverter)
    {
        this.uomConverter = uomConverter;
    }

    @Nullable
    @Override
    public synchronized Ingredient convert(IngredientCommand source)
    {
        if (source == null)
        {
            return null;
        }

        final Ingredient ingredient = new Ingredient();
        ingredient.setId(source.getId());
        if (source.getRecipeId() != null)
        {
            Recipe recipe = new Recipe();
            recipe.setId(source.getRecipeId());
            ingredient.setRecipe(recipe);
            recipe.addIngredient(ingredient);
        }
        ingredient.setDescription(source.getDescription());
        ingredient.setAmount(source.getAmount());
        ingredient.setUnitOfMeasure(uomConverter.convert(source.getUnitOfMeasure()));
        return ingredient;
    }
}
