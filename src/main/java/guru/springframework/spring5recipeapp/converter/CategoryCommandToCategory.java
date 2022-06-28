package guru.springframework.spring5recipeapp.converter;

import guru.springframework.spring5recipeapp.command.CategoryCommand;
import guru.springframework.spring5recipeapp.model.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category>
{
    @Nullable
    @Override
    public synchronized Category convert(CategoryCommand source)
    {
        if (source == null)
        {
            return null;
        }

        final Category category = new Category();
        category.setId(source.getId());
        category.setDescription(source.getDescription());
        return category;
    }
}
