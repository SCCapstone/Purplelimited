package edu.sc.purplelimited.classes;

public class Ingredient {
  private String ingredientName;
  private String units;
  private String quantity;

  public Ingredient() {
    this.ingredientName = "default";
    this.units = "default";
    this.quantity = "0";
  }

  //TODO: parse quantity from text. example: ¼ -> 0.25
  public Ingredient(String ingredientName, String units, String quantity) {
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

  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public String toString() {
    String retUnits = "";
    String retName = ingredientName;
    String retQuantity;

    //TODO de-spaghettify this monstrosity
    if (units.equals("none")) {
      retUnits += "";
    } else {
      retUnits += units;
      if (quantity.equals("1") || quantity.equals("1.0")) {
        retUnits += " ";
      } else {
        retUnits += "s ";
      }
    }
    if (quantity.equals("0") || quantity.equals("0.0")) {
      return retName + " to taste.";
    } else {
      retQuantity = quantity + " ";
      return retQuantity + retUnits + retName;
    }
  }
}
