package guru.springframework.spring5recipeapp.converter;

import guru.springframework.spring5recipeapp.command.CategoryCommand;
import guru.springframework.spring5recipeapp.model.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand>
{
    @Nullable
    @Override
    public synchronized CategoryCommand convert(Category source)
    {
        if (source == null)
        {
            return null;
        }

        final CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(source.getId());
        categoryCommand.setDescription(source.getDescription());
        return categoryCommand;
    }
}
