package edu.sc.purplelimited.classes;


import java.util.ArrayList;

public class Recipe {
    private String name;
    private String description;
    private ArrayList<String> ingredients;

    public Recipe() {
        this.name = "Default";
        this.description = "Default";
        this.ingredients = new ArrayList<>();
    }
    public Recipe(String title, String description, ArrayList<String> ingredients) {
        this.name = title;
        if (description.equals("") || description.contains("href") || description.equals("null")) {
            this.description = "A recipe for homemade " + title + ".";
        } else {
            this.description = description;
        }
        this.ingredients = ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(ArrayList<String> ingredients) {
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
