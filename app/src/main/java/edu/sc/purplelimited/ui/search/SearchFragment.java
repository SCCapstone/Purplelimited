package edu.sc.purplelimited.ui.search;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.Ingredient;
import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.databinding.FragmentSearchBinding;
import edu.sc.purplelimited.ui.swipe_ui.CardViewAdapter;
import edu.sc.purplelimited.ui.swipe_ui.RecipeCard;

public class SearchFragment extends Fragment {
  private FragmentSearchBinding binding;
  private ViewPager viewPager;
  private EditText searchBar;
  private ViewPager searchResultsCards;
  private ArrayList<Recipe> searchResultsArrayList = new ArrayList<>();
  private View root;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    binding = FragmentSearchBinding.inflate(inflater, container, false);
    root = binding.getRoot();
    viewPager = root.findViewById(R.id.view_pager_suggestions);
    searchBar = root.findViewById(R.id.search_recipe_text);
    Button searchButton = root.findViewById(R.id.search_button);
    searchResultsCards = root.findViewById(R.id.view_pager_suggestions);

    searchButton.setOnClickListener(view -> {
      FetchRecipes fetchRecipes = new FetchRecipes();
      fetchRecipes.execute();
    });
    searchViewModel.getText().observe(getViewLifecycleOwner(), s -> {
    });
    return root;
  }

  @SuppressLint("StaticFieldLeak")
  public class FetchRecipes extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... strings) {
      String keywords = searchBar.getText().toString();
      String URL = SearchConstants.searchPrefix+keywords+SearchConstants.searchPostfix;
      StringBuilder received = new StringBuilder();

      try {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
          url = new URL(URL);
          urlConnection = (HttpURLConnection) url.openConnection();
          urlConnection.setRequestProperty(SearchConstants.headerHostPrefix,
                  SearchConstants.headerHostValue);
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

    @Override
    protected void onPostExecute(String s) {
      //TODO: refine keyword comparison when performing search
      searchResultsArrayList.clear();
      try {
        JSONObject resultsJSON = new JSONObject(s);
        JSONArray recipesArrayJSON = resultsJSON.getJSONArray("results");
        for (int i = 0; i < recipesArrayJSON.length(); i++) {
          JSONObject currentRecipeObj = recipesArrayJSON.getJSONObject(i);
          String recipeName = currentRecipeObj.getString("name");
          String recipeDesc = currentRecipeObj.getString("description");
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
          }
          Recipe toAdd = new Recipe(recipeName, recipeDesc, recipeIngredients, "none");
          searchResultsArrayList.add(toAdd);
        }
        populateSearchResults();
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  private void populateSearchResults() {
    ArrayList<RecipeCard> recipeCards = new ArrayList<>();
    for(int i = 0; i < 5; i++) {
      Recipe recipe = searchResultsArrayList.get(i);
      String name = recipe.getName();
      String description = recipe.getDescription();
      ArrayList<Ingredient> ingredients = recipe.getIngredients();
      RecipeCard toAdd = new RecipeCard(name, description, ingredients);
      recipeCards.add(toAdd);
    }
    if(getContext()!=null) {
      CardViewAdapter cardViewAdapter = new CardViewAdapter(getContext(), recipeCards);
      searchResultsCards.setAdapter(cardViewAdapter);
      searchResultsCards.setPadding(50,0, 50, 0);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}