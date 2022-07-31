package edu.sc.purplelimited.ui.suggestions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SuggestionsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SuggestionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}