package edu.sc.purplelimited.classes;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert.*;

import java.util.ArrayList;

@RunWith(JUnit4.class)
public class RecipeTest {
  private Recipe recipe;
  private ArrayList<Ingredient> ingredients;
  private ArrayList<String> instructions;

  @Before
  public void setup() {
    ingredients = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      Ingredient toAdd = new Ingredient("Ingredient#"+i, "test", i, "id");
      ingredients.add(toAdd);
    }
    instructions = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      String toAdd = "Instruction#"+i;
      instructions.add(toAdd);
    }
    recipe = new Recipe("title", "description", ingredients,
            "a1", "testURL", instructions);
  }

  @Test
  public void getName() {
    String expected = "title";
    String actual = recipe.getName();
    assertEquals(expected, actual);
  }

  @Test
  public void getId() {
    String expected = "a1";
    String actual = recipe.getId();
    assertEquals(expected, actual);
  }

  @Test
  public void getDescription() {
    String expected = "description";
    String actual = recipe.getDescription();
  }


}
