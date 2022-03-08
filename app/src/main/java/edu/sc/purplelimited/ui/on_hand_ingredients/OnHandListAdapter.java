package edu.sc.purplelimited.ui.on_hand_ingredients;

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
import edu.sc.purplelimited.classes.Ingredient;

public class OnHandListAdapter extends ArrayAdapter {
  ArrayList<Ingredient> onHandList;
  Context context;

  public OnHandListAdapter(@NonNull Context context, ArrayList<Ingredient> onHandList) {
    super(context, R.layout.on_hand_list_view, onHandList);
    this.context = context;
    this.onHandList = onHandList;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View row = null;
    if(convertView == null) {
      LayoutInflater inflater;
      inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.on_hand_list_view, null);
    } else {
      row = convertView;
    }
    // Text
    TextView ingredientString = row.findViewById(R.id.ingredient_name_in_listview);
    String ingredientText = onHandList.get(position).toString();
    if (ingredientText.length() > 28) {
      ingredientText = ingredientText.substring(0, 28) + "...";
    }
    ingredientString.setText(ingredientText);

    // Clickable
    ImageView increaseQuantity = row.findViewById(R.id.increase_quantity_list_view);
    ImageView decreaseQuantity = row.findViewById(R.id.decrease_quantity_list_view);
    ImageView removeItem = row.findViewById(R.id.remove);

    increaseQuantity.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String id = onHandList.get(position).getId();
        int quantity = onHandList.get(position).getQuantity();
        quantity++;
        OnHandIngredientsFragment.changeQuantity(quantity, id);
      }
    });
    decreaseQuantity.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String id = onHandList.get(position).getId();
        int quantity = onHandList.get(position).getQuantity();
        if (quantity > 0) {
          quantity--;
          OnHandIngredientsFragment.changeQuantity(quantity, id);
        } else {
          // remove item from list if quantity goes below 0
          OnHandIngredientsFragment.removeIngredient(id);
        }
      }
    });
    removeItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String id = onHandList.get(position).getId();
        OnHandIngredientsFragment.removeIngredient(id);
      }
    });
    return row;
  }
}
