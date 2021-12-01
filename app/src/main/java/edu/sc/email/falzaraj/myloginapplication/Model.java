package edu.sc.email.falzaraj.myloginapplication;

public class Model {
    String recipe, description;
    int image;

    public Model(String recipe, String description, int image) {
        this.recipe = recipe;
        this.description = description;
        this.image = image;
    }


    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
