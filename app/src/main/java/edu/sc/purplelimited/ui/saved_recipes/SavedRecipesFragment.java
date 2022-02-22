package edu.sc.purplelimited.ui.saved_recipes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import edu.sc.purplelimited.classes.Ingredient;
import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.databinding.FragmentSavedRecipesBinding;

import java.util.ArrayList;

public class SavedRecipesFragment extends Fragment {
    private final ArrayList<Recipe> savedArrayList = new ArrayList<>();
    private FragmentSavedRecipesBinding binding;
    private ListView savedRecipesListView;
    private static FirebaseDatabase database;
    private static DatabaseReference savedRecipes;
    private AlertDialog recipeViewPopup;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SavedRecipesViewModel savedVM;
        savedVM = new ViewModelProvider(this).get(SavedRecipesViewModel.class);
        binding = FragmentSavedRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        database = FirebaseDatabase.getInstance();
        savedRecipesListView = root.findViewById(R.id.saved_recipe_list_view);
        // TODO replace hardcoded reference with userId
        savedRecipes = database.getReference("users").child("1").child("savedRecipes");

        savedRecipes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String pcn) {
                String name = ds.child("name").getValue(String.class);
                String description = ds.child("description").getValue(String.class);
                String id = ds.child("id").getValue(String.class);
                ArrayList<Ingredient> ingredientsList= new ArrayList<>();
                DataSnapshot ingredients = ds.child("ingredients");
                for(DataSnapshot ing : ingredients.getChildren()) {
                    String ingName = ing.child("ingredientName").getValue(String.class);
                    String ingUnit = ing.child("units").getValue(String.class);
                    int ingQuantity = ing.child("quantity").getValue(int.class);
                    ingredientsList.add(new Ingredient(ingName, ingUnit, ingQuantity, "none"));
                }
                Recipe added = new Recipe(name, description, ingredientsList, id);
                savedArrayList.add(added);
                populateRecipeList();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot ds, @Nullable String pcn) {/*empty*/}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot ds) {
                int index = 0;
                String id = ds.getKey();
                for(int i = 0; i < savedArrayList.size(); i++) {
                    Recipe recipe = savedArrayList.get(i);
                    if (recipe.getId().equals(id)) {
                        index = i;
                    }
                }
                savedArrayList.remove(index);
                populateRecipeList();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot ds, @Nullable String pcn) {/*empty*/}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {/*empty*/}
        });
        savedRecipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe recipe = savedArrayList.get(i);
                createPopup(recipe);
            }
        });
        final TextView textView = binding.textDashboard;
        savedVM.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void populateRecipeList() {
        if(getContext()!=null) {
            SavedRecipesListAdapter adapter;
            adapter = new SavedRecipesListAdapter(getContext(), savedArrayList);
            savedRecipesListView.setAdapter(adapter);
        }
    }

    public static void removeRecipe(String id) {
        savedRecipes.child(id).removeValue();
    }

    private void createPopup(Recipe recipe){
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder((getContext()));
        final View view = getLayoutInflater().inflate(R.layout.saved_recipe_popup, null);

        ArrayList<String> ingToString = new ArrayList<>();
        ArrayList<Ingredient> ingredientsList = recipe.getIngredients();
        for (Ingredient ingredient : ingredientsList) {
            String toString = ingredient.toString();
            System.out.println("Ingredient: " + toString);
            ingToString.add(toString);
        }
        // Name and description
        TextView recipeName = (TextView) view.findViewById(R.id.popup_title);
        recipeName.setText(recipe.getName());
        TextView recipeDescription = (TextView) view.findViewById(R.id.popup_description);

        // Ingredients
        recipeDescription.setText(recipe.getDescription());
        ListView ingredients = (ListView) view.findViewById(R.id.popup_ingredients);

        ArrayAdapter adapter;
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, ingToString);
        ingredients.setAdapter(adapter);
        popupBuilder.setView(view);
        recipeViewPopup = popupBuilder.create();
        recipeViewPopup.show();
    }
}