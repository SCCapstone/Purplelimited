package edu.sc.purplelimited.ui.search;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class SearchFragment extends Fragment {
  private FragmentSearchBinding binding;
  private ViewPager viewPager;
  private EditText searchBar;
  private ViewPager searchResultsCards;
  private ArrayList<Recipe> searchResultsArrayList = new ArrayList<>();
  private View root;
  private ProgressBar progressBar;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    binding = FragmentSearchBinding.inflate(inflater, container, false);
    root = binding.getRoot();
    viewPager = root.findViewById(R.id.view_pager_suggestions);
    searchBar = root.findViewById(R.id.search_recipe_text);
    Button searchButton = root.findViewById(R.id.search_button);
    progressBar = root.findViewById(R.id.search_progress);
    progressBar.setVisibility(View.INVISIBLE);
    searchResultsCards = root.findViewById(R.id.view_pager_suggestions);

    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String searchText = searchBar.getText().toString();
        boolean tooShort = searchText.length() < 3;
        boolean noCharacters = searchText.matches("[ ]*");
        boolean invalidChars = !searchText.matches("[A-Za-z0-9 ]*");

        if (tooShort || noCharacters) {
          String toastText = "Search terms must be at least 3 characters.";
          Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
          return;
        }
        if (invalidChars) {
          String toastText = "Search terms can only contain letters, numbers and spaces.";
          Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
          return;
        }
        progressBar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        FetchRecipes fetchRecipes = new FetchRecipes();
        fetchRecipes.execute();
      }
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
      // look for leading spaces in keyword string
      int blankCount = 0;
      for (int i = 0;i<keywords.length();i++) {
        if (keywords.charAt(i) == ' ') {
          blankCount++;
          System.out.println(blankCount);
        } else {
          break;
        }
      }
      // remove leading spaces, if any
      keywords = keywords.substring(blankCount);
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
      //TODO: minimize try/catch blocks
      searchResultsArrayList.clear();
      try {
        JSONObject resultsJSON = new JSONObject(s);
        JSONArray recipesArrayJSON = resultsJSON.getJSONArray("results");
        for (int i = 0; i < recipesArrayJSON.length(); i++) {
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
            }
            Recipe toAdd = new Recipe(recipeName, recipeDesc, recipeIngredients, "none", thumbnail, recipeInstructions);
            searchResultsArrayList.add(toAdd);
          } catch (JSONException e){
            e.printStackTrace();
          }
        }
        progressBar.setVisibility(View.INVISIBLE);
        populateSearchResults();
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  private void populateSearchResults() {
    if(getContext()!=null) {
      SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(getContext(), searchResultsArrayList);
      searchResultsCards.setAdapter(searchResultsAdapter);
      searchResultsCards.setPadding(50,0, 50, 0);
    }
    if(searchResultsArrayList.size()==0){
      Toast.makeText(getContext(), "No results found.", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(getContext(), "Swipe left to see more recipes.", Toast.LENGTH_SHORT).show();
    }
    viewPager.setVisibility(View.VISIBLE);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
























//public class SearchFragment extends Fragment {
//  private FragmentSearchBinding binding;
//  private ViewPager viewPager;
//  private EditText searchBar;
//  private ViewPager searchResultsCards;
//  private ArrayList<Recipe> searchResultsArrayList = new ArrayList<>();
//  private View root;
//  private ProgressBar progressBar;
//  private int minPage = 0;
//
//  public View onCreateView(@NonNull LayoutInflater inflater,
//                           ViewGroup container, Bundle savedInstanceState) {
//    SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
//    binding = FragmentSearchBinding.inflate(inflater, container, false);
//    root = binding.getRoot();
//    viewPager = root.findViewById(R.id.view_pager_suggestions);
//    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//      @Override
//      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//      }
//
//      @Override
//      public void onPageSelected(int position) {
//        if(position < minPage) {
//          viewPager.setCurrentItem(minPage);
//        } else {
//          minPage = position;
//        }
//      }
//
//      @Override
//      public void onPageScrollStateChanged(int state) {
//
//      }
//    });
//    searchBar = root.findViewById(R.id.search_recipe_text);
//    Button searchButton = root.findViewById(R.id.search_button);
//    progressBar = root.findViewById(R.id.search_progress);
//    progressBar.setVisibility(View.INVISIBLE);
//    searchResultsCards = root.findViewById(R.id.view_pager_suggestions);
//
//    searchButton.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        String searchText = searchBar.getText().toString();
//        boolean tooShort = searchText.length() < 3;
//        boolean noCharacters = searchText.matches("[ ]*");
//        boolean invalidChars = !searchText.matches("[A-Za-z0-9 ]*");
//
//        if (tooShort || noCharacters) {
//          String toastText = "Search terms must be at least 3 characters.";
//          Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
//          return;
//        }
//        if (invalidChars) {
//          String toastText = "Search terms can only contain letters, numbers and spaces.";
//          Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
//          return;
//        }
//        progressBar.setVisibility(View.VISIBLE);
//        viewPager.setVisibility(View.INVISIBLE);
//        FetchRecipes fetchRecipes = new FetchRecipes();
//        fetchRecipes.execute();
//      }
//    });
//    searchViewModel.getText().observe(getViewLifecycleOwner(), s -> {
//    });
//    return root;
//  }
//
//  @SuppressLint("StaticFieldLeak")
//  public class FetchRecipes extends AsyncTask<String, String, String> {
//
//    @Override
//    protected String doInBackground(String... strings) {
//
//      String keywords = searchBar.getText().toString();
//      // look for leading spaces in keyword string
//      int blankCount = 0;
//      for (int i = 0;i<keywords.length();i++) {
//        if (keywords.charAt(i) == ' ') {
//          blankCount++;
//          System.out.println(blankCount);
//        } else {
//          break;
//        }
//      }
//      // remove leading spaces, if any
//      keywords = keywords.substring(blankCount);
//      String URL = SearchConstants.searchPrefix+keywords+SearchConstants.searchPostfix;
//      StringBuilder received = new StringBuilder();
//
//      try {
//        URL url;
//        HttpURLConnection urlConnection = null;
//        try {
//          url = new URL(URL);
//          urlConnection = (HttpURLConnection) url.openConnection();
//          urlConnection.setRequestProperty(SearchConstants.headerHostPrefix,
//                  SearchConstants.headerHostValue);
//          urlConnection.setRequestProperty(SearchConstants.headerKeyPrefix,
//                  SearchConstants.headerKeyValue);
//          InputStream inputStream = urlConnection.getInputStream();
//          InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//          int data = inputStreamReader.read();
//          while (data != -1) {
//            received.append((char) data);
//            data = inputStreamReader.read();
//          }
//        } catch (IOException e) {
//          e.printStackTrace();
//        } finally {
//          if (urlConnection != null) {
//            urlConnection.disconnect();
//          }
//        }
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//      return received.toString();
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//      //TODO: minimize try/catch blocks
//      searchResultsArrayList.clear();
//      try {
//        JSONObject resultsJSON = new JSONObject(s);
//        JSONArray recipesArrayJSON = resultsJSON.getJSONArray("results");
//        for (int i = 0; i < recipesArrayJSON.length(); i++) {
//          JSONObject currentRecipeObj = recipesArrayJSON.getJSONObject(i);
//          ArrayList<String> recipeInstructions = new ArrayList<String>();
//          try {
//            JSONArray instructionsArray = currentRecipeObj.getJSONArray("instructions");
//            for (int j = 0; j < instructionsArray.length();j++) {
//              JSONObject currentInstruction = instructionsArray.getJSONObject(j);
//              String instructionText = currentInstruction.getString("display_text");
//              recipeInstructions.add(instructionText);
//            }
//            String recipeName = currentRecipeObj.getString("name");
//            String recipeDesc = currentRecipeObj.getString("description");
//            String thumbnail = currentRecipeObj.getString("beauty_url");
//            if (thumbnail.equals("null")) {
//              thumbnail = currentRecipeObj.getString("thumbnail_url");
//            }
//            if(thumbnail.equals("null")) {
//              thumbnail = "none";
//            }
//            ArrayList<Ingredient> recipeIngredients = new ArrayList<>();
//            try {
//              JSONArray sectionsArray = currentRecipeObj.getJSONArray("sections");
//              if (sectionsArray != null) {
//                for (int j = 0; j < sectionsArray.length(); j++) {
//                  JSONObject section = sectionsArray.getJSONObject(j);
//                  JSONArray components = section.getJSONArray("components");
//                  for (int k = 0; k < components.length(); k++) {
//                    JSONObject component = components.getJSONObject(k);
//                    JSONObject componentIng = component.getJSONObject("ingredient");
//                    String ingName;
//                    String ingUnit;
//                    int ingQuantity;
//                    if (componentIng.getString("display_singular").equals("") ) {
//                      ingName = componentIng.getString("display_plural");
//                    } else {
//                      ingName = componentIng.getString("display_singular");
//                    }
//                    JSONArray compMeasureArray = component.getJSONArray("measurements");
//                    JSONObject componentMeasurement = compMeasureArray.getJSONObject(0);
//                    JSONObject componentUnit = componentMeasurement.getJSONObject("unit");
//                    if (componentUnit.getString("name").equals("") ) {
//                      ingUnit = "none";
//                    } else {
//                      ingUnit = componentUnit.getString("name");
//                    }
//                    try {
//                      ingQuantity = componentMeasurement.getInt("quantity");
//                    } catch (Exception e) {
//                      ingQuantity = 1;
//                    }
//                    Ingredient toAdd = new Ingredient(ingName, ingUnit, ingQuantity, "none");
//                    recipeIngredients.add(toAdd);
//                  }
//                }
//              }
//            } catch (JSONException e) {
//              continue;
//            }
//            Recipe toAdd = new Recipe(recipeName, recipeDesc, recipeIngredients, "none", thumbnail, recipeInstructions);
//            searchResultsArrayList.add(toAdd);
//          } catch (JSONException e){
//            e.printStackTrace();
//          }
//        }
//        progressBar.setVisibility(View.INVISIBLE);
//        populateSearchResults();
//      } catch (JSONException e) {
//        e.printStackTrace();
//      }
//    }
//  }
//
//  private void populateSearchResults() {
//    if(getContext()!=null) {
//      SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(getContext(), searchResultsArrayList);
//      searchResultsCards.setAdapter(searchResultsAdapter);
//      searchResultsCards.setPadding(50,0, 50, 0);
//    }
//    if(searchResultsArrayList.size()==0){
//      Toast.makeText(getContext(), "No results found.", Toast.LENGTH_SHORT).show();
//    } else {
//      Toast.makeText(getContext(), "Swipe left to see more recipes.", Toast.LENGTH_SHORT).show();
//    }
//    viewPager.setVisibility(View.VISIBLE);
//  }
//
//  @Override
//  public void onDestroyView() {
//    super.onDestroyView();
//    binding = null;
//  }
//}