package edu.sc.purplelimited.ui.saved_recipes;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.sc.purplelimited.LoginActivity;
import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.ImageQueue;
import edu.sc.purplelimited.classes.Ingredient;
import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.databinding.FragmentSavedRecipesBinding;
import edu.sc.purplelimited.ui.PopupListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

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
        String userName = LoginActivity.getCurrentUserName();
        savedRecipes = database.getReference("users").child(userName).child("savedRecipes");

        savedRecipes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String pcn) {
                String name = ds.child("name").getValue(String.class);
                String description = ds.child("description").getValue(String.class);
                String id = ds.child("id").getValue(String.class);
                String thumbnail = ds.child("thumbnailURL").getValue(String.class);
                ArrayList<Ingredient> ingredientsList= new ArrayList<>();
                DataSnapshot ingredients = ds.child("ingredients");
                for(DataSnapshot ing : ingredients.getChildren()) {
                    String ingName = ing.child("ingredientName").getValue(String.class);
                    String ingUnit = ing.child("units").getValue(String.class);
                    int ingQuantity = ing.child("quantity").getValue(int.class);
                    ingredientsList.add(new Ingredient(ingName, ingUnit, ingQuantity, "none"));
                }
                ArrayList<String> instructionsList = new ArrayList<>();
                DataSnapshot instructions = ds.child("instructions");
                for(DataSnapshot instruct : instructions.getChildren()) {
                    String instruction = instruct.getValue(String.class);
                    instructionsList.add(instruction);
                }
                Recipe added = new Recipe(name, description, ingredientsList, id, thumbnail, instructionsList);
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void populateRecipeList() {
        if(getContext()!=null) {
            // save the scroll position before changing the view
            int prevPosition = savedRecipesListView.getFirstVisiblePosition();
            View v = savedRecipesListView.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();

            // populate the listview and reset scroll position
            SavedRecipesListAdapter adapter;
            adapter = new SavedRecipesListAdapter(getContext(), savedArrayList);
            savedRecipesListView.setAdapter(adapter);
            savedRecipesListView.setSelectionFromTop(prevPosition, top);
        }
    }

    public static void removeRecipe(String id) {
        savedRecipes.child(id).removeValue();
    }

    public static void addRecipe(Recipe toAdd, Context context) {
        String userName = LoginActivity.getCurrentUserName();

        // Needed in case user performs a search before
        // visiting the Saved Recipes page
        if (savedRecipes == null) {
            // TODO move this to an instantiateDatabase method
            database = FirebaseDatabase.getInstance();
            savedRecipes = database.getReference("users").child(userName).child("savedRecipes");
        }
        Task<DataSnapshot> current = savedRecipes.get();
        current.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // Check that the recipe isn't a duplicate
                boolean alreadySaved = false;
                for (DataSnapshot currentRecipe : current.getResult().getChildren()) {
                    String nameFromList = currentRecipe.getValue(Recipe.class).getName();
                    String compareTo = toAdd.getName();
                    if (nameFromList.equals(compareTo)) {
                        alreadySaved = true;
                    }
                }
                if (alreadySaved) {
                    // Recipe already saved, so notify user and don't add to Saved Recipes
                    Toast.makeText(context, "This recipe is already saved.", Toast.LENGTH_SHORT).show();
                } else {
                    // Recipe not saved, so save it and notify user of success
                    String id = savedRecipes.push().getKey();
                    toAdd.setId(id);
                    savedRecipes.child(id).setValue(toAdd);
                    Toast.makeText(context, "Saved to your list.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // TODO: move to popup builder class to remove duplicate code
    private void createPopup(Recipe recipe){
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder((getContext()));
        final View view = getLayoutInflater().inflate(R.layout.saved_recipe_popup, null);

        // Get the toStrings from each Ingredient in the Recipe
        ArrayList<String> ingToString = new ArrayList<>();
        ArrayList<Ingredient> ingredientsList = recipe.getIngredients();
        for (Ingredient ingredient : ingredientsList) {
            String toString = ingredient.toString();
            ingToString.add(toString);
        }

        // Image Preview
        ImageView preview = (ImageView) view.findViewById(R.id.popup_image);
        String url = recipe.getThumbnailURL();
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        preview.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {/*empty*/}
        });
        ImageQueue.getInstance(getContext()).addToQueue(imageRequest);

        // Recipe Name TextView
        TextView recipeName = (TextView) view.findViewById(R.id.popup_title);
        recipeName.setText(recipe.getName());

        // Description TextView
        TextView recipeDescription = (TextView) view.findViewById(R.id.popup_description);
        recipeDescription.setText(recipe.getDescription());

        // Prepare for Expandable List View
        ArrayList<String> sectionsList = new ArrayList<>();
        sectionsList.add("Ingredients");
        sectionsList.add("Instructions");
        HashMap<String, ArrayList<String>> sectionsContent = new HashMap<>();
        ArrayList<String> instructions = recipe.getInstructions();
        sectionsContent.put(sectionsList.get(0), ingToString);
        sectionsContent.put(sectionsList.get(1), instructions);

        // Ingredients and Instructions Expandable ListView
        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.expanded_list_for_popup);
        PopupListAdapter popupListAdapter = new PopupListAdapter(getContext(), sectionsList, sectionsContent);
        expandableListView.setAdapter(popupListAdapter);

        // Create the popup
        popupBuilder.setView(view);
        recipeViewPopup = popupBuilder.create();
        recipeViewPopup.show();
    }
}