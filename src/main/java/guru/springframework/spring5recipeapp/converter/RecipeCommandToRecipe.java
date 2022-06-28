package guru.springframework.spring5recipeapp.converter;

import guru.springframework.spring5recipeapp.command.RecipeCommand;
import guru.springframework.spring5recipeapp.model.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe>
{
    private final CategoryCommandToCategory categoryConverter;
    private final IngredientCommandToIngredient ingredientConverter;
    private final NotesCommandToNotes notesConverter;

    public RecipeCommandToRecipe(CategoryCommandToCategory categoryConverter, IngredientCommandToIngredient ingredientConverter,
                                 NotesCommandToNotes notesConverter)
    {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Nullable
    @Override
    public synchronized Recipe convert(RecipeCommand source)
    {
        if (source == null)
        {
            return null;
        }

        final Recipe recipe = new Recipe();
        recipe.setId(source.getId());
        recipe.setDescription(source.getDescription());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setCookTime(source.getCookTime());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setUrl(source.getUrl());
        recipe.setDirections(source.getDirections());
        recipe.setDifficulty(source.getDifficulty());
        recipe.setNotes(notesConverter.convert(source.getNotes()));

        if (!source.getCategories().isEmpty())
        {
            source.getCategories()
                    .forEach(category -> recipe.getCategories().add(categoryConverter.convert(category)));
        }

        if (!source.getIngredients().isEmpty())
        {
            source.getIngredients()
                    .forEach(ingredient -> recipe.getIngredients().add(ingredientConverter.convert(ingredient)));
        }

        return recipe;
    }
}
