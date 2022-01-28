package edu.sc.purplelimited.ui.saved_recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.databinding.FragmentSavedRecipesBinding;

import java.util.ArrayList;

public class SavedRecipesFragment extends Fragment {
    private final ArrayList<Recipe> savedRecipesArrayList = new ArrayList<>();
    private FragmentSavedRecipesBinding binding;
    private ListView savedRecipesListView;

    //TODO implement add/remove functionality
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SavedRecipesViewModel savedRecipesViewModel =
                new ViewModelProvider(this).get(SavedRecipesViewModel.class);
        binding = FragmentSavedRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        savedRecipesListView = root.findViewById(R.id.saved_recipe_list_view);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TODO replace hardcoded reference with userId
        DatabaseReference savedRecipes = database.getReference("users").child("1").child("savedRecipes");

        savedRecipes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Recipe added = snapshot.getValue(Recipe.class);
                savedRecipesArrayList.add(added);
                populateRecipeList();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Recipe removed = snapshot.getValue(Recipe.class);
                savedRecipesArrayList.remove(removed);
                populateRecipeList();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        final TextView textView = binding.textDashboard;
        savedRecipesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //TODO create custom adapter/listview for recipes
    private void populateRecipeList() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, savedRecipesArrayList);
        savedRecipesListView.setAdapter(arrayAdapter);
    }
}