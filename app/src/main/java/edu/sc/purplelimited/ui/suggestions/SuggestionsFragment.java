package edu.sc.purplelimited.ui.suggestions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.Ingredient;
import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.databinding.FragmentSuggestionsBinding;
import edu.sc.purplelimited.ui.swipe_ui.Adapter;
import edu.sc.purplelimited.ui.swipe_ui.Model;

public class SuggestionsFragment extends Fragment {
    private FragmentSuggestionsBinding binding;
    private ViewPager suggestionsCards;
    private final ArrayList<Recipe> suggestedRecipesList = new ArrayList<>();

    //TODO implement save functionality
    //TODO implement dismiss functionality
    //TODO pull suggestions from API
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SuggestionsViewModel sVM = new ViewModelProvider(this).get(SuggestionsViewModel.class);
        binding = FragmentSuggestionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        suggestionsCards = root.findViewById(R.id.view_pager_suggestions);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TODO replace hardcoded reference with userId
        DatabaseReference suggestedRecipes = database.getReference("users").child("1").child("suggestedRecipes");

        suggestedRecipes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name = snapshot.child("name").getValue(String.class);
                String description = snapshot.child("description").getValue(String.class);
                ArrayList<Ingredient> ingredientsList= new ArrayList<>();
                DataSnapshot ingredients = snapshot.child("ingredients");
                for(DataSnapshot ing : ingredients.getChildren()) {
                    String ingName = ing.child("ingredientName").getValue(String.class);
                    String ingUnit = ing.child("units").getValue(String.class);
                    String ingQuantity = ing.child("quantity").getValue(Long.class).toString();
                    ingredientsList.add(new Ingredient(ingName, ingUnit, ingQuantity));
                }
                Recipe added = new Recipe(name, description, ingredientsList);
                suggestedRecipesList.add(added);
                populateSuggestionCards();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //TODO match this with onChildAdded logic

                Recipe removed = snapshot.getValue(Recipe.class);
                suggestedRecipesList.remove(removed);
                populateSuggestionCards();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        final TextView textView = binding.textHome;
        sVM.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    // TODO replace hardcoded images
    private void populateSuggestionCards() {
        ArrayList<Model> recipeCards = new ArrayList<>();
        int burger = R.drawable.burger;
        int chickenSalad = R.drawable.chicken_salad;
        int pasta = R.drawable.alfredo_shrimp_pasta;
        int chili = R.drawable.chili;
        int wings = R.drawable.wings;
        int current;
        for(Recipe recipe : suggestedRecipesList) {
            switch(recipe.getName()) {
                case "BBQ Bacon Burger":
                    current = burger;
                    break;
                case "Chicken Salad":
                    current = chickenSalad;
                    break;
                case "Shrimp Alfredo Pasta":
                    current = pasta;
                    break;
                case "Turkey and Black Bean Chili":
                    current = chili;
                    break;
                case "Korean BBQ Wings":
                    current = wings;
                    break;
                default:
                    current = burger;
                    break;
            }
            recipeCards.add(new Model(recipe.getName(), recipe.getDescription(), current));
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