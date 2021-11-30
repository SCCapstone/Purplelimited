package edu.sc.purplelimited.ui.search;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SearchViewModel extends ViewModel {

  private MutableLiveData<String> mText;

  public SearchViewModel() {
    mText = new MutableLiveData<>();
    mText.setValue("Search for new recipes here.");
  }

  public LiveData<String> getText() {
    return mText;
  }


}