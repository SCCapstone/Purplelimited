package edu.sc.purplelimited.ui.on_hand_ingredients;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OnHandIngredientsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OnHandIngredientsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}