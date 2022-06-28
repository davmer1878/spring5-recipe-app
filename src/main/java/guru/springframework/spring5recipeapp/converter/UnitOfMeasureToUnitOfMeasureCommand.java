package guru.springframework.spring5recipeapp.converter;

import guru.springframework.spring5recipeapp.command.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.model.UnitOfMeasure;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureToUnitOfMeasureCommand implements Converter<UnitOfMeasure, UnitOfMeasureCommand>
{
    @Nullable
    @Override
    public synchronized UnitOfMeasureCommand convert(UnitOfMeasure source)
    {
        if (source == null)
        {
            return null;
        }

        final UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(source.getId());
        unitOfMeasureCommand.setDescription(source.getDescription());
        return unitOfMeasureCommand;
    }
}
