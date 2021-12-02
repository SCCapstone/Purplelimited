package edu.sc.purplelimited.ui.suggestions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.ui.swipe_ui.Adapter;
import edu.sc.purplelimited.ui.swipe_ui.Model;
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
        suggestionsCards = root.findViewById(R.id.view_pager_suggestions);
        savedRecipesList = new ArrayList<Recipe>();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        savedRecipesList = new ArrayList<Recipe>();

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO: add image references to database for each recipe
                //TODO: replace lookingFor string with int userId
                String lookingFor = "bob";
                savedRecipesList.clear();
                for(DataSnapshot user : dataSnapshot.getChildren()) {
                    String name = (String) user.child("name").getValue();
                    if (name.equalsIgnoreCase(lookingFor)) {
                        DataSnapshot savedRecipes = user.child("suggestedRecipes");
                        for (DataSnapshot recipe : savedRecipes.getChildren()) {
                            String recipeName = (String) recipe.child("name").getValue();
                            String recipeDescription = (String) recipe.child("description").getValue();
                            DataSnapshot ingredientsList = recipe.child("ingredients");
                            ArrayList<String> ingredients = new ArrayList<String>();
                            for (DataSnapshot ingredientSnapShot : ingredientsList.getChildren()) {
                                String ingredient = (String) ingredientSnapShot.getValue();
                                ingredients.add(ingredient);
                            }
                            savedRecipesList.add(new Recipe(recipeName, recipeDescription, ingredients));
                        }
                    }
                }
                populateSuggestionCards();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {      }
        });


        final TextView textView = binding.textHome;
        sVM.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void populateSuggestionCards() {
        ArrayList<Model> recipeCards = new ArrayList<>();
        for(Recipe recipe : savedRecipesList) {
            recipeCards.add(new Model(recipe.getName(), recipe.getDescription(), R.drawable.place_holder_recipe_image));
        }
        Adapter cardViewAdapter = new Adapter(getContext(), recipeCards);
        suggestionsCards.setAdapter(cardViewAdapter);
        suggestionsCards.setPadding(50,0, 50, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}