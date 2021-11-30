package edu.sc.purplelimited.ui.suggestions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.ui.swipe_ui.RecipeCard;
import edu.sc.purplelimited.ui.swipe_ui.CardViewAdapter;
import edu.sc.purplelimited.R;
import edu.sc.purplelimited.databinding.FragmentSuggestionsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SuggestionsFragment extends Fragment {
    private SuggestionsViewModel sVM;
    private FragmentSuggestionsBinding binding;
    private View root;
    private ViewPager suggestionsCards;
    private ArrayList<Recipe> savedRecipesList;
    private FirebaseDatabase database;
    private DatabaseReference users;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sVM = new ViewModelProvider(this).get(SuggestionsViewModel.class);
        binding = FragmentSuggestionsBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        final TextView textView = binding.textHome;
        sVM.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    //TODO: Populate cards with info from database
    private void populateSuggestionCards() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}