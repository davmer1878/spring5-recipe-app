package guru.springframework.spring5recipeapp.controller;

import guru.springframework.spring5recipeapp.command.IngredientCommand;
import guru.springframework.spring5recipeapp.command.RecipeCommand;
import guru.springframework.spring5recipeapp.command.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.service.IngredientService;
import guru.springframework.spring5recipeapp.service.RecipeService;
import guru.springframework.spring5recipeapp.service.UnitOfMeasureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IngredientController
{
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    private static final Logger log = LoggerFactory.getLogger(IngredientController.class);

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService)
    {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable Long recipeId, Model model)
    {
        log.debug("Getting ingredient list for Recipe Id: " + recipeId);

        model.addAttribute("recipe", recipeService.findCommandById(recipeId));
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable Long recipeId, @PathVariable Long id, Model model)
    {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newRecipeIngredient(@PathVariable Long recipeId, Model model)
    {
        //make sure we have a good id value
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));
        //todo raise exception if null

        //need to return back parent id for hidden form property
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        model.addAttribute("ingredient", ingredientCommand);

        //init uom
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());

        model.addAttribute("unitOfMeasureList", unitOfMeasureService.listAllUnitsOfMeasure());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable Long recipeId, @PathVariable Long id, Model model)
    {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
        model.addAttribute("unitOfMeasureList", unitOfMeasureService.listAllUnitsOfMeasure());

        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command)
    {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("saved recipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteRecipeIngredient(@PathVariable Long recipeId, @PathVariable Long id)
    {
        ingredientService.deleteById(recipeId, id);
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
