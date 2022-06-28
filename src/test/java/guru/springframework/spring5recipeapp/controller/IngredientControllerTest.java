package guru.springframework.spring5recipeapp.controller;

import guru.springframework.spring5recipeapp.command.IngredientCommand;
import guru.springframework.spring5recipeapp.command.RecipeCommand;
import guru.springframework.spring5recipeapp.service.IngredientService;
import guru.springframework.spring5recipeapp.service.RecipeService;
import guru.springframework.spring5recipeapp.service.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredientControllerTest
{
    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    @InjectMocks
    IngredientController ingredientController;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception
    {
        ingredientController = new IngredientController(recipeService, ingredientService, unitOfMeasureService);

        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    public void testListIngredients() throws Exception
    {
        RecipeCommand recipeCommand = new RecipeCommand();

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/ingredients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    public void testShowIngredient() throws Exception
    {
        IngredientCommand ingredientCommand = new IngredientCommand();

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/ingredient/2/show"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/show"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient"));

        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyLong(), anyLong());
    }

    @Test
    public void testNewIngredientForm() throws Exception
    {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        //when
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        when(unitOfMeasureService.listAllUnitsOfMeasure()).thenReturn(new HashSet<>());

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/ingredient/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/ingredientform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("unitOfMeasureList"));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    public void testUpdateIngredientForm() throws Exception
    {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();

        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.listAllUnitsOfMeasure()).thenReturn(new HashSet<>());

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/ingredient/2/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/ingredientform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("unitOfMeasureList"));
    }

    @Test
    public void testSaveOrUpdate() throws Exception
    {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId(3L);
        command.setRecipeId(2L);

        //when
        when(ingredientService.saveIngredientCommand(any())).thenReturn(command);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/recipe/2/ingredient")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "some string")
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/2/ingredient/3/show"));
    }

    @Test
    public void testDeleteIngredient() throws Exception
    {
    //then
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/2/ingredient/3/delete"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/2/ingredients"));

        verify(ingredientService, times(1)).deleteById(anyLong(), anyLong());
    }
}
