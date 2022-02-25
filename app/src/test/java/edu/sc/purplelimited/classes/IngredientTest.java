package edu.sc.purplelimited.classes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class IngredientTest {
    private Ingredient ingredient = new Ingredient("ground beef", "lb", 1, "001");

    String expected = "1 lb ground beef";
    String actual = ingredient.toString();

    @Test
    public void ingredient_toString_test() {
        assertEquals(expected, actual);
    }
}