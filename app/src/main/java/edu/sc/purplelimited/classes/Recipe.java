package edu.sc.purplelimited.classes;


import java.util.ArrayList;

public class Recipe {
    private String id;
    private String name;
    private String description;
    private ArrayList<Ingredient> ingredients;
    private String thumbnailURL;

    public Recipe() {
        this.name = "Default";
        this.description = "Default";
        this.ingredients = new ArrayList<>();
        this.id = "empty";
        this.thumbnailURL = "none";
    }
    public Recipe(String title, String description, ArrayList<Ingredient> ingredients, String id, String thumbnailURL) {
        this.name = title;
        if (description.equals("") || description.contains("href") || description.equals("null")) {
            this.description = "A recipe for homemade " + title + ".";
        } else {
            this.description = description;
        }
        this.ingredients = ingredients;
        this.id = id;
        this.thumbnailURL = thumbnailURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
