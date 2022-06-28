package guru.springframework.spring5recipeapp.controller;

import guru.springframework.spring5recipeapp.command.RecipeCommand;
import guru.springframework.spring5recipeapp.model.Recipe;
import guru.springframework.spring5recipeapp.service.RecipeService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest
{
    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController recipeController;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception
    {
        recipeController = new RecipeController(recipeService);

        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void testGetRecipe() throws Exception
    {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when (recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/show"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/show"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
    }

    @Test
    public void testGetNewRecipeForm() throws Exception
    {
        RecipeCommand recipeCommand = new RecipeCommand();
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
    }

    @Test
    public void testPostNewRecipeForm() throws Exception
    {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipe").contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/2/show"));
    }

    @Test
    public void testGetUpdateView() throws Exception
    {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        when (recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
    }

    @Test
    public void testDeleteAction() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/delete"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyLong());
    }
}