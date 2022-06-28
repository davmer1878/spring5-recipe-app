package guru.springframework.spring5recipeapp.controller;

import guru.springframework.spring5recipeapp.command.RecipeCommand;
import guru.springframework.spring5recipeapp.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RecipeController
{
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService)
    {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}/show")
    public String showById(@PathVariable Long id, Model model)
    {
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe/show";
    }

    @GetMapping("/recipe/new")
    public String newRecipe(Model model)
    {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model)
    {
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "recipe/recipeform";
    }

    @GetMapping("/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable Long id)
    {
        recipeService.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command)
    {
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }
}
