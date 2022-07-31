package edu.sc.purplelimited.ui.suggestions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SuggestionsViewModel extends ViewModel {

//    String recipe, description;
//    int imageI;
        private MutableLiveData<String> mText;


//    public SuggestionsViewModel(MutableLiveData<String> mText) {
//        this.mText = mText;
//    }

    public SuggestionsViewModel() {
//        this.recipe = recipe;
//        this.description = description;
//        this.imageI = imageI;
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

//    public String getRecipe() {
//        return recipe;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public int getImage() {
//        return imageI;
//    }
//
//
//    public void setRecipe(String recipe) {
//        this.recipe = recipe;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setImageI(int imageI) {
//        this.imageI = imageI;
//    }

    public LiveData<String> getText() {
        return mText;
    }

//    private MutableLiveData<String> mText;
//
//    public SuggestionsViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("");
//    }
//
//    public LiveData<String> getText() {
//        return mText;
//    }
}