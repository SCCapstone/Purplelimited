package edu.sc.purplelimited.ui.swipe_ui;

import java.util.ArrayList;

public class RecipeCard {
  String title, description;
  ArrayList<String> ingredients;

  public RecipeCard(String title, String description, ArrayList<String> ingredients) {
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

  public ArrayList<String> getIngredients() {
    return ingredients;
  }

}
