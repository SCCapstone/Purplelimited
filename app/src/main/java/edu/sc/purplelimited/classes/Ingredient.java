package edu.sc.purplelimited.classes;

public class Ingredient {
  private String ingredientName;
  private String units;
  private int quantity;
  private String id;

  public Ingredient() {
    this.ingredientName = "default";
    this.units = "default";
    this.quantity = 0;
  }

  public Ingredient(String ingredientName, String units, int quantity, String id) {
    this.ingredientName = ingredientName;
    if (!units.endsWith("s")) {
      this.units = units;
    } else {
      this.units = units.substring(0, units.length()-1);
    }
    this.quantity = quantity;
    this.id = id;
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

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getId() { return id; }

  public void setId(String id) {
    this.id = id;
  }
  public String toString() {
    String retUnits = "";
    String retName = ingredientName;
    String retQuantity;


    if (units.equals("none")) {
      retUnits += "";
    } else {
      retUnits += units;
      if (quantity == 1) {
        retUnits += " ";
      } else {
        retUnits += "s ";
      }
    }
    if (quantity == 0) {
      return retName;
    } else {
      retQuantity = quantity + " ";
      return retQuantity + retUnits + retName;
    }
  }
}
