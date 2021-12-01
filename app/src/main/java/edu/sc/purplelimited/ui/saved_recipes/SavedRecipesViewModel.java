package edu.sc.purplelimited.ui.saved_recipes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SavedRecipesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SavedRecipesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}