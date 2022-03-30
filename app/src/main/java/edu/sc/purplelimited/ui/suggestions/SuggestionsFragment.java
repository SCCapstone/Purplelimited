package edu.sc.purplelimited.ui.suggestions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
//import androidx.viewpager2.widget.ViewPager2;

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
import edu.sc.purplelimited.ui.saved_recipes.SavedRecipesFragment;
import edu.sc.purplelimited.ui.swipe_ui.Adapter;
import edu.sc.purplelimited.ui.swipe_ui.Model;

public class SuggestionsFragment extends Fragment {
    private FragmentSuggestionsBinding binding;
    private ViewPager suggestionsCards;
    private final ArrayList<Recipe> suggestedRecipesList = new ArrayList<>();

    private Button save_recipe_button;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String RECIPE = "recipe";
    TextView text_home;



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
        DatabaseReference suggestedRecipes;
        suggestedRecipes = database.getReference("users").child("1").child("suggestedRecipes");

        suggestedRecipes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String pcn) {
                String name = ds.child("name").getValue(String.class);
                String description = ds.child("description").getValue(String.class);
                ArrayList<Ingredient> ingredientsList= new ArrayList<>();
                DataSnapshot ingredients = ds.child("ingredients");
                for(DataSnapshot ing : ingredients.getChildren()) {
                    String ingName = ing.child("ingredientName").getValue(String.class);
                    String ingUnit = ing.child("units").getValue(String.class);
                    int ingQuantity = ing.child("quantity").getValue(int.class);
                    ingredientsList.add(new Ingredient(ingName, ingUnit, ingQuantity, "none"));
                }
                Recipe added = new Recipe(name, description, ingredientsList, "none", "none", new ArrayList<>());
                suggestedRecipesList.add(added);
                populateSuggestionCards();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot ds, @Nullable String pcn) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //TODO match this with onChildAdded logic
                Recipe removed = snapshot.getValue(Recipe.class);
                suggestedRecipesList.remove(removed);
                populateSuggestionCards();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot ds, @Nullable String pcn) {/*empty*/}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {/*empty*/}
        });


//        View v = inflater(R.layout.fragment_suggestions);
        save_recipe_button = (Button) root.findViewById(R.id.save_recipe_button); // on onCreate(Bundle savedInstanceState) in main instead?
        text_home = root.findViewById(R.id.text_home);
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String recipeName = sharedPreferences.getString(RECIPE, null);

        if (recipeName != null) {
//            Intent intent = new Intent(SuggestionsFragment.this, SavedRecipesFragment.class);
            Intent intent = new Intent(SuggestionsFragment.this.getContext(), SavedRecipesFragment.class);

//            startActivity(intent);
        }

        save_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(RECIPE, text_home.getText().toString());
                editor.apply();

//                Intent intent = new Intent(SuggestionsFragment.this, SavedRecipesFragment.class);
                Intent intent = new Intent(SuggestionsFragment.this.getContext(), SavedRecipesFragment.class);

//                startActivity(intent);
            }
        });       //??



//            @Override
//            public void onClick(View view) {
////                saveRecipe();
//            }
//        });

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

        recipeCards.add(new Model(
                "BBQ Bacon Burger",
                "",
                burger
        ));
        recipeCards.add(new Model(
                "Chicken Salad",
                "",
                chickenSalad
        ));
        recipeCards.add(new Model(
                "Shrimp Alfredo Pasta",
                "",
                pasta
        ));
        recipeCards.add(new Model(
                "Turkey and Black Bean Chili",
                "",
                chili
        ));


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



        if(getContext()!=null) {
            Adapter cardViewAdapter = new Adapter(getContext(), recipeCards);
            suggestionsCards.setAdapter(cardViewAdapter);
            suggestionsCards.setPadding(50,0, 50, 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//        public void saveRecipe() {
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE); //(Context.MODE_PRIVATE)
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(RECIPE, textView.getText().toString());
//        editor.apply();
//    }

//    public void onClick(View view) {
//        var savedRecipes: ArrayList<Recipe> = ArrayList();
//    }
}