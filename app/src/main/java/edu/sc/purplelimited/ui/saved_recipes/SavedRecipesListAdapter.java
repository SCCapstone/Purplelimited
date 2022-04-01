package edu.sc.purplelimited.ui.saved_recipes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.ImageQueue;
import edu.sc.purplelimited.classes.Recipe;

public class SavedRecipesListAdapter extends ArrayAdapter {
  ArrayList<Recipe> savedRecipesList;
  Context context;

  public SavedRecipesListAdapter(@NonNull Context context, ArrayList<Recipe> savedRecipesList) {
    super(context, R.layout.saved_recipes_lv, savedRecipesList);
    this.context = context;
    this.savedRecipesList = savedRecipesList;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View row = null;
    if(convertView == null) {
      LayoutInflater inflater;
      inflater = ((Activity) context).getLayoutInflater();
      row = inflater.inflate(R.layout.saved_recipes_lv, parent, false);
    } else {
      row = convertView;
    }

    // Text
    TextView savedName = row.findViewById(R.id.saved_rec_name);
    String nameString = savedRecipesList.get(position).getName();
    if (nameString.length() > 33) {
      nameString = nameString.substring(0, 33) + "...";
    }
    savedName.setText(nameString);
    TextView savedDescription = row.findViewById(R.id.saved_rec_description);
    savedDescription.setText(savedRecipesList.get(position).getDescription());

    // Clickable
    ImageView removeIcon = row.findViewById(R.id.saved_rec_remove);

    // Thumbnail
    ImageView thumbnail = row.findViewById(R.id.saved_rec_thumbnail);
    String url = savedRecipesList.get(position).getThumbnailURL();
    ImageRequest imageRequest = new ImageRequest(url,
            new Response.Listener<Bitmap>() {
              @Override
              public void onResponse(Bitmap response) {
                thumbnail.setImageBitmap(response);
              }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {/*empty*/}
    });
    ImageQueue.getInstance(getContext()).addToQueue(imageRequest);

    //TODO Confirmation Dialog Popup
    removeIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String id = savedRecipesList.get(position).getId();
        SavedRecipesFragment.removeRecipe(id);
      }
    });

    return row;
  }
}
