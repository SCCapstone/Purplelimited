package edu.sc.purplelimited.ui.saved_recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.ui.saved_recipes.SavedRecipesViewModel;
import edu.sc.purplelimited.R;
import edu.sc.purplelimited.databinding.FragmentSavedRecipesBinding;
import edu.sc.purplelimited.ui.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class SavedRecipesFragment extends Fragment {
    private final List<String> savedRecipes = new ArrayList<>();
    private FragmentSavedRecipesBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference users;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SavedRecipesViewModel savedRecipesViewModel =
                new ViewModelProvider(this).get(SavedRecipesViewModel.class);
        binding = FragmentSavedRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textDashboard;
        savedRecipesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // TODO: fetch saved recipes from database
    private void populateRecipeList() {

    }
}