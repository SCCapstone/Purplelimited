package edu.sc.purplelimited.classes;

public class Ingredient {
  private String ingredientName;
  private Unit units;
  private double quantity;

  public Ingredient() {
    this.ingredientName = "default";
    this.units = Unit.TABLESPOON;
    this.quantity = 0.0;
  }

  public Ingredient(String ingredientName, Unit units, double quantity) {
    this.ingredientName = ingredientName;
    this.units = units;
    this.quantity = quantity;
  }

  public String getIngredientName() {
    return ingredientName;
  }

  public void setIngredientName(String ingredientName) {
    this.ingredientName = ingredientName;
  }

  public Unit getUnits() {
    return units;
  }

  public void setUnits(Unit units) {
    this.units = units;
  }

  public double getQuantity() {
    return quantity;
  }

  public void setQuantity(double quantity) {
    this.quantity = quantity;
  }
}
