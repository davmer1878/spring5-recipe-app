package guru.springframework.spring5recipeapp.converter;

import guru.springframework.spring5recipeapp.command.NotesCommand;
import guru.springframework.spring5recipeapp.model.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotesToNotesCommandTest
{
    public static final Long ID_VALUE = Long.valueOf(1L);
    public static final String RECIPE_NOTES = "Notes";
    NotesToNotesCommand converter;

    @BeforeEach
    public void setUp() throws Exception
    {
        converter = new NotesToNotesCommand();
    }

    @Test
    public void convert() throws Exception
    {
        //given
        Notes notes = new Notes();
        notes.setId(ID_VALUE);
        notes.setRecipeNotes(RECIPE_NOTES);

        //when
        NotesCommand notesCommand = converter.convert(notes);

        //then
        assertEquals(ID_VALUE, notesCommand.getId());
        assertEquals(RECIPE_NOTES, notesCommand.getRecipeNotes());
    }

    @Test
    public void testNull() throws Exception
    {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception
    {
        assertNotNull(converter.convert(new Notes()));
    }
}