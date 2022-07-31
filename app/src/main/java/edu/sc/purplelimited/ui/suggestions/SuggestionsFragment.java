package edu.sc.purplelimited.ui.suggestions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
//import androidx.viewpager2.widget.ViewPager2;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.lang.Object;
import java.util.HashMap;
import java.util.Random;

import edu.sc.purplelimited.LoginActivity;
import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.ImageQueue;
import edu.sc.purplelimited.classes.Ingredient;
import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.databinding.FragmentSuggestionsBinding;
import edu.sc.purplelimited.ui.PopupListAdapter;
import edu.sc.purplelimited.ui.saved_recipes.SavedRecipesFragment;
import edu.sc.purplelimited.ui.search.SearchConstants;
import edu.sc.purplelimited.ui.search.SearchFragment;
import edu.sc.purplelimited.ui.search.SearchResultsAdapter;
import edu.sc.purplelimited.ui.swipe_ui.Adapter;
import edu.sc.purplelimited.ui.swipe_ui.Model;

public class SuggestionsFragment extends Fragment {

    private ViewPager viewPager;
    private ViewPager suggestionCards;
    private View root;
    private FragmentSuggestionsBinding binding;
    private ArrayList<Recipe> suggestedRecipesList = new ArrayList<>();
    private ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String RECIPE = "recipe";
    TextView text_home;
    private static DatabaseReference DBRef;
    private static FirebaseDatabase database;
    private ArrayList<String> onHandArray = new ArrayList<>();
    private AlertDialog suggestionPopup;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set up view and binding
        SuggestionsViewModel suggestionsViewModel = new ViewModelProvider(this).get(SuggestionsViewModel.class);
        binding = FragmentSuggestionsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        // call reference ids sugg_card_images
        viewPager = root.findViewById(R.id.view_pager_suggest);
        suggestionCards = root.findViewById(R.id.view_pager_suggest);
        text_home = root.findViewById(R.id.text_home);
        progressBar = root.findViewById(R.id.suggesting);
        progressBar.setVisibility(View.INVISIBLE);
        Button nextButton = root.findViewById(R.id.nextSuggest);

        // Next button for user
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int pagei = position + 1;
                suggestionCards.setCurrentItem(getItem(+1),true);
            }
        });
//    private int getItem(int i) {
//        return suggestionCards.getCurrentItem() + i;
//        }

        // Database reference onHandIngredients from Firebase
        String userName = LoginActivity.getCurrentUserName();
        database = FirebaseDatabase.getInstance();
        DBRef = database.getReference("users").child(userName).child("onHandIngredients");


        DBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String name = snapshot.child("ingredientName").getValue(String.class);
                onHandArray.add(name);
                progressBar.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.INVISIBLE);
                FetchRecipes fetchRecipes = new FetchRecipes();
                fetchRecipes.execute();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                /*empty*/
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                /*Empty*/
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                /*empty*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                /*empty*/
            }
        });
        // Keeps track overlap event
        suggestionsViewModel.getText().observe(getViewLifecycleOwner(), s -> {
        });


//        suggestionCards.setOnClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//
//            @Override
//            public void onClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Recipe recipe = suggestedRecipesList.get(i);
//                createPopup(recipe);
//            }
//        });
//

//        suggestionCards.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Recipe recipe = suggestedRecipesList.get(i)
//            }
//
//            @Override
//            public void onClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Recipe recipe = suggestedRecipesList.get(i);
//                createPopup(recipe);
//            }

//        });




        return root;
    }

    // Async keyword search
    @SuppressLint("StaticFieldLeak")
    public class FetchRecipes extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String search = onHandArray.toString();
            StringBuilder keywords = new StringBuilder(search);
            for (int i = 0; i < keywords.length(); i++) {
                if (keywords.charAt(i)==',' || keywords.charAt(i)=='[' || keywords.charAt(i)==']') {
                    keywords = keywords.deleteCharAt(i);
                }
            }
            // look for leading spaces in keyword string
            int blankCount = 0;
            for (int i = 0;i<keywords.length();i++) {
                if (keywords.charAt(i) == ',') {
                    blankCount++;
                    System.out.println(blankCount);
                } else {
                    break;
                }
            }
            // remove leading spaces, if any
            keywords.substring(blankCount);
            String URL = SearchConstants.searchPrefix+keywords+SearchConstants.searchPostfix;
            StringBuilder received = new StringBuilder();
            System.out.println(URL);
            try {
                URL url; //OKhttp
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty(SearchConstants.headerHostPrefix,
                            SearchConstants.headerHostValue); // Search Fragment keys
                    urlConnection.setRequestProperty(SearchConstants.headerKeyPrefix,
                            SearchConstants.headerKeyValue);
                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    int data = inputStreamReader.read();
                    while (data != -1) {
                        received.append((char) data);
                        data = inputStreamReader.read();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return received.toString();
        }

        //JSON Parsing
        //Random functionality
        @Override
        protected void onPostExecute(String s) {
            suggestedRecipesList.clear();
//            Math.random() = new Random();
            try {
                JSONObject resultsJSON = new JSONObject(s);
                JSONArray recipesArrayJSON = resultsJSON.getJSONArray("results");
                final int numberOfSuggestions = recipesArrayJSON.length();
                Random random = new Random();
                int randomIndex = random.nextInt(recipesArrayJSON.length());
                for (int i = 0; i < (numberOfSuggestions); i++) {
//                    int randomIndex = rand.nextInt(recipesArrayJSON.length());
//                    Random random = new Random();
//                    int randomIndex = random.nextInt(recipesArrayJSON.length());
//                    int randomSuggestion = recipesArrayJSON.get(randomIndex+i);
//                    int randomSuggestion = randomIndex + i;
                    JSONObject currentRecipeObj = recipesArrayJSON.getJSONObject(i);
                    ArrayList<String> recipeInstructions = new ArrayList<String>();
                    try {
                        JSONArray instructionsArray = currentRecipeObj.getJSONArray("instructions");
                        for (int j = 0; j < instructionsArray.length();j++) {
                            JSONObject currentInstruction = instructionsArray.getJSONObject(j);
                            String instructionText = currentInstruction.getString("display_text");
                            recipeInstructions.add(instructionText);
                        }
                        String recipeName = currentRecipeObj.getString("name");
                        String recipeDesc = currentRecipeObj.getString("description");
                        String thumbnail = currentRecipeObj.getString("beauty_url");
                        if (thumbnail.equals("null")) {
                            thumbnail = currentRecipeObj.getString("thumbnail_url");
                        }
                        if(thumbnail.equals("null")) {
                            thumbnail = "none";
                        }
                        ArrayList<Ingredient> recipeIngredients = new ArrayList<>();
                        try {
                            JSONArray sectionsArray = currentRecipeObj.getJSONArray("sections");
                            if (sectionsArray != null) {
                                for (int j = 0; j < sectionsArray.length(); j++) {
                                    JSONObject section = sectionsArray.getJSONObject(j);
                                    JSONArray components = section.getJSONArray("components");
                                    for (int k = 0; k < components.length(); k++) {
                                        JSONObject component = components.getJSONObject(k);
                                        JSONObject componentIng = component.getJSONObject("ingredient");
                                        String ingName;
                                        String ingUnit;
                                        int ingQuantity;
                                        if (componentIng.getString("display_singular").equals("") ) {
                                            ingName = componentIng.getString("display_plural");
                                        } else {
                                            ingName = componentIng.getString("display_singular");
                                        }
                                        JSONArray compMeasureArray = component.getJSONArray("measurements");
                                        JSONObject componentMeasurement = compMeasureArray.getJSONObject(0);
                                        JSONObject componentUnit = componentMeasurement.getJSONObject("unit");
                                        if (componentUnit.getString("name").equals("") ) {
                                            ingUnit = "none";
                                        } else {
                                            ingUnit = componentUnit.getString("name");
                                        }
                                        try {
                                            ingQuantity = componentMeasurement.getInt("quantity");
                                        } catch (Exception e) {
                                            ingQuantity = 1;
                                        }
                                        Ingredient toAdd = new Ingredient(ingName, ingUnit, ingQuantity, "none");
                                        recipeIngredients.add(toAdd);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            continue;
                        } //add saved Recipe
                        Recipe toAdd = new Recipe(recipeName, recipeDesc, recipeIngredients, "none", thumbnail, recipeInstructions);
                        suggestedRecipesList.add(toAdd);
                    } catch (JSONException e){
                        e.printStackTrace();
                        //
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                populateSuggestionCards();
//                progressBar.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // function for nextButton
    private int getItem(int i) {
        return suggestionCards.getCurrentItem() + i;
    }

    private void createPopup(Recipe recipe){
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder((getContext()));
        final View view = getLayoutInflater().inflate(R.layout.suggest_recipe_popup, null);

        // Get the toStrings from each Ingredient in the Recipe
        ArrayList<String> ingToString = new ArrayList<>();
        ArrayList<Ingredient> ingredientsList = recipe.getIngredients();
        for (Ingredient ingredient : ingredientsList) {
            String toString = ingredient.toString();
            ingToString.add(toString);
        }

        // Image Preview
        ImageView preview = (ImageView) view.findViewById(R.id.suggest_popup);
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
        TextView recipeName = (TextView) view.findViewById(R.id.suggest_popup_title);
        recipeName.setText(recipe.getName());

        // Description TextView
        TextView recipeDescription = (TextView) view.findViewById(R.id.suggest_popup_description);
        recipeDescription.setText(recipe.getDescription());

        // Prepare for Expandable List View
        ArrayList<String> sectionsList = new ArrayList<>();
        sectionsList.add("Ingredients");
        sectionsList.add("Instructions");
        HashMap<String, ArrayList<String>> sectionsContent = new HashMap<>(); //shared prefs
        ArrayList<String> instructions = recipe.getInstructions();
        sectionsContent.put(sectionsList.get(0), ingToString);
        sectionsContent.put(sectionsList.get(1), instructions);

        // Ingredients and Instructions Expandable ListView
        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.suggest_expanded);
        PopupListAdapter popupListAdapter = new PopupListAdapter(getContext(), sectionsList, sectionsContent);
        expandableListView.setAdapter(popupListAdapter);

        // Create the popup
        popupBuilder.setView(view);
        suggestionPopup = popupBuilder.create();
        suggestionPopup.show();
    }






    private void populateSuggestionCards() {

        if(getContext()!=null) {
            SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(getContext(), suggestedRecipesList);
            suggestionCards.setAdapter(suggestionsAdapter);
            suggestionCards.setPadding(50,0, 50, 0);
        }
        if(suggestedRecipesList.size()==0){
            Toast.makeText(getContext(), "No suggestions available.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Swipe left to see more recipes.", Toast.LENGTH_SHORT).show();
        }
        viewPager.setVisibility(View.VISIBLE);
        //suggestionCards.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}