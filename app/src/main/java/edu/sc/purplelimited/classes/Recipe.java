package edu.sc.purplelimited.classes;


import java.util.ArrayList;

public class Recipe {
    private String name;
    private String description;
    private ArrayList<String> ingredients;

    public Recipe(String title, String description, ArrayList<String> ingredients) {
        this.name = title;
        if (description.equals("") || description.contains("href") || description.equals("null")) {
            this.description = "A recipe for homemade " + title + ".";
        } else {
            this.description = description;
        }
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<String> getIngredients() {
        return ingredients;
    }
}
