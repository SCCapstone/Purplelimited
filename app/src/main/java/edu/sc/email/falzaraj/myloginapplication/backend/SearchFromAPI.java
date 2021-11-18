package edu.sc.email.falzaraj.myloginapplication.backend;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import edu.sc.email.falzaraj.myloginapplication.recipe_classes.Recipe;

public class SearchFromAPI extends AsyncTask<String, String, String> {
    public static final String searchPrefix =
            "https://tasty.p.rapidapi.com/recipes/list?from=20&size=20&q=";
    public static final String searchPostfix = "%20";
    public static final String headerHostPrefix = "x-rapidapi-host";
    public static final String headerHostValue = "tasty.p.rapidapi.com";
    public static final String headerKeyPrefix= "x-rapidapi-key";
    public static final String headerKeyValue =
            "1a9d0aedc9msh7f67f6e5fb475b6p15aeb6jsn4542dbc93f22";

    private String searchTerm = "chicken";
    private ArrayList<Recipe> recipeList;

    protected String doInBackground(String... strings) {
        String URL = searchPrefix+searchTerm+searchPostfix;
        StringBuilder received = new StringBuilder();

        try {
            java.net.URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty(headerHostPrefix,
                        headerHostValue);
                urlConnection.setRequestProperty(headerKeyPrefix,
                        headerKeyValue);
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

    protected void onPostExecute(String s) {
        recipeList.clear();
        try {
            JSONObject resultsJSON = new JSONObject(s);
            JSONArray recipesArrayJSON = resultsJSON.getJSONArray("results");
            for (int i = 0; i < recipesArrayJSON.length(); i++) {
                JSONObject recipeJSON = recipesArrayJSON.getJSONObject(i);
                String recipeName = recipeJSON.getString("name");
                String description = recipeJSON.getString("description");
                ArrayList<String> ingredientsArrayList = new ArrayList<String>();
                try {
                    JSONArray sectionArray = recipeJSON.getJSONArray("sections");
                    if (sectionArray != null) {
                        for (int j = 0; j < sectionArray.length(); j++) {
                            JSONObject section = sectionArray.getJSONObject(j);
                            JSONArray componentsArray = section.getJSONArray("components");
                            for (int k = 0; k < componentsArray.length(); k++) {
                                JSONObject component = componentsArray.getJSONObject(k);
                                String ingredient = component.getString("raw_text");
                                ingredientsArrayList.add(ingredient);
                            }
                        }
                        recipeList.add(new Recipe(recipeName, description, ingredientsArrayList));
                    }
                } catch (JSONException e){
                    continue;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
