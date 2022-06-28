package guru.springframework.spring5recipeapp.controller;

import guru.springframework.spring5recipeapp.model.Recipe;
import guru.springframework.spring5recipeapp.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest
{
    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @InjectMocks
    IndexController indexController;

    Set<Recipe> recipeSet;

    @BeforeEach
    void setUp()
    {
        recipeSet = new HashSet<>();
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipeSet.add(recipe1);
        Recipe recipe2 = new Recipe();
        recipe1.setId(2L);
        recipeSet.add(recipe2);
    }

    @Test
    void testMockMVC() throws Exception
    {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    void showIndex()
    {
        when(recipeService.getRecipes()).thenReturn(recipeSet);
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        assertEquals("index", indexController.showIndex(model));
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        Set<Recipe> setInController = argumentCaptor.getValue();
        assertEquals(2, setInController.size());
    }
}