package guru.springframework.spring5recipeapp.enums;

public enum Difficulty
{
    EASY("Easy"),
    MODERATE("Moderate"),
    HARD("Hard");

    private final String displayValue;

    private Difficulty(String displayValue)
    {
        this.displayValue = displayValue;
    }

    public String getDisplayValue()
    {
        return displayValue;
    }
}
