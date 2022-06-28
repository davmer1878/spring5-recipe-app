package guru.springframework.spring5recipeapp.service;

import guru.springframework.spring5recipeapp.command.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.converter.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.model.UnitOfMeasure;
import guru.springframework.spring5recipeapp.repository.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitOfMeasureServiceImplTest
{
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    public void setUp() throws Exception
    {
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void listAllUnitsOfMeasure() throws Exception {
        //given
        Set<UnitOfMeasure> unitsOfMeasure = new HashSet<>();
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        unitsOfMeasure.add(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId(2L);
        unitsOfMeasure.add(uom2);

        when(unitOfMeasureRepository.findAll()).thenReturn(unitsOfMeasure);

        //when
        Set<UnitOfMeasureCommand> commands = unitOfMeasureService.listAllUnitsOfMeasure();

        //then
        assertEquals(2, commands.size());
        verify(unitOfMeasureRepository, times(1)).findAll();
    }
}
