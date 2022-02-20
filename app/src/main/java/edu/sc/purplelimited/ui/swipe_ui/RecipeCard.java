package edu.sc.purplelimited.ui.swipe_ui;

import java.util.ArrayList;

import edu.sc.purplelimited.classes.Ingredient;

public class RecipeCard {
  String title, description;
  ArrayList<Ingredient> ingredients;

  public RecipeCard(String title, String description, ArrayList<Ingredient> ingredients) {
    this.title = title;
    this.description = description;
    this.ingredients = ingredients;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public ArrayList<Ingredient> getIngredients() {
    return ingredients;
  }

}
