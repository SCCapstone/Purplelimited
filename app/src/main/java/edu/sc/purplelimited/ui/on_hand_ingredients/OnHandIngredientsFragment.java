package edu.sc.purplelimited.ui.on_hand_ingredients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.sc.purplelimited.databinding.FragmentOnHandBinding;
import edu.sc.purplelimited.ui.saved_recipes.SavedRecipesViewModel;

import java.util.ArrayList;
import java.util.List;

public class OnHandIngredientsFragment extends Fragment {
    private final List<String> savedRecipes = new ArrayList<>();
    private FragmentOnHandBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SavedRecipesViewModel savedRecipesViewModel =
                new ViewModelProvider(this).get(SavedRecipesViewModel.class);
        binding = FragmentOnHandBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    // TODO: Implement add/remove functionality and read/write from Database
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}