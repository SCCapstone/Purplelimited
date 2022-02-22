package edu.sc.purplelimited.ui.saved_recipes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import edu.sc.purplelimited.R;
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
    if(convertView == null) {
      LayoutInflater inflater;
      inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.saved_recipes_lv, null);

      // Text
      TextView savedName = convertView.findViewById(R.id.saved_rec_name);
      savedName.setText(savedRecipesList.get(position).getName());
      TextView savedDescription = convertView.findViewById(R.id.saved_rec_description);
      savedDescription.setText(savedRecipesList.get(position).getDescription());

      // Clickable
      ImageView removeIcon = convertView.findViewById(R.id.saved_rec_remove);

      // Thumbnail
      ImageView thumbnail = convertView.findViewById(R.id.saved_rec_thumbnail);

      //TODO Confirmation Dialog Popup
      removeIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          String id = savedRecipesList.get(position).getId();
          SavedRecipesFragment.removeRecipe(id);
        }
      });
    }
    return convertView;
  }
}
