package guru.springframework.spring5recipeapp.controller;

import guru.springframework.spring5recipeapp.repository.CategoryRepository;
import guru.springframework.spring5recipeapp.repository.UnitOfMeasureRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController
{
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IndexController(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository)
    {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @RequestMapping({"", "/", "index", "index.html"})
    public String showIndex()
    {
        categoryRepository.findByDescription("Mexican").ifPresentOrElse(
                category -> System.out.println("Category Id: " + category.getId()),
                () -> System.out.println("Category does not exist"));
        categoryRepository.findByDescription("Invalid").ifPresentOrElse(
                category -> System.out.println("Category Id: " + category.getId()),
                () -> System.out.println("Category does not exist"));
        unitOfMeasureRepository.findByDescription("Pinch").ifPresentOrElse(
                unitOfMeasure -> System.out.println("Unit Of Measure Id: " + unitOfMeasure.getId()),
                () -> System.out.println("Unit of Measure does not exist"));
        unitOfMeasureRepository.findByDescription("Punch").ifPresentOrElse(
                unitOfMeasure -> System.out.println("Unit Of Measure Id: " + unitOfMeasure.getId()),
                () -> System.out.println("Unit of Measure does not exist"));

        return "index";
    }
}
