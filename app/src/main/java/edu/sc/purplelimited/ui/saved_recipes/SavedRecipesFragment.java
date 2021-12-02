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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.ui.saved_recipes.SavedRecipesViewModel;
import edu.sc.purplelimited.R;
import edu.sc.purplelimited.databinding.FragmentSavedRecipesBinding;
import edu.sc.purplelimited.ui.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class SavedRecipesFragment extends Fragment {
    private ArrayList<String> savedRecipes;
    private FragmentSavedRecipesBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference users;
    private ListView savedRecipesListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SavedRecipesViewModel savedRecipesViewModel =
                new ViewModelProvider(this).get(SavedRecipesViewModel.class);
        binding = FragmentSavedRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        savedRecipesListView = root.findViewById(R.id.saved_recipe_list_view);
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        savedRecipes = new ArrayList<String>();

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO: add image references to database for each recipe
                //TODO: replace lookingFor string with int userId
                String lookingFor = "bob";
                savedRecipes.clear();
                for(DataSnapshot user : dataSnapshot.getChildren()) {
                    String name = (String) user.child("name").getValue();
                    if (name.equalsIgnoreCase(lookingFor)) {
                        DataSnapshot savedRecipesDatabase = user.child("savedRecipes");
                        for (DataSnapshot recipe : savedRecipesDatabase.getChildren()) {
                            String recipeName = (String) recipe.child("name").getValue();
                            savedRecipes.add(recipeName);
                        }
                    }
                }
                populateRecipeList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {      }
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

    private void populateRecipeList() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, savedRecipes);
        savedRecipesListView.setAdapter(arrayAdapter);
    }
}