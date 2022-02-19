package edu.sc.purplelimited.ui.swipe_ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.Ingredient;

public class CardViewAdapter extends PagerAdapter {

  private final Context context;
  private ArrayList<RecipeCard> cardContentArrayList;

  public CardViewAdapter(Context context, ArrayList<RecipeCard> cardContentArrayList) {
    this.context = context;
    this.cardContentArrayList = cardContentArrayList;
  }

  @Override
  public int getCount() {
    return cardContentArrayList.size();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view.equals(object);
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View view = LayoutInflater.from(context).inflate(R.layout.search_result_card,
                                                     container,
                                         false);
    TextView cardTitle = view.findViewById(R.id.cardTitle);
    TextView cardDescription = view.findViewById(R.id.cardDescription);
    ListView cardIngredients = view.findViewById(R.id.card_ingredients);
    RecipeCard recipeCard = cardContentArrayList.get(position);
    String title = recipeCard.getTitle();
    String description = recipeCard.getDescription();
    ArrayList<Ingredient> ings = recipeCard.getIngredients();
    ArrayList<String> ingredientsList = new ArrayList<>();
    for (int i = 0; i < ings.size();i++) {
      ingredientsList.add(ings.get(i).toString());
    }
    cardTitle.setText(title);
    cardDescription.setText(description);
    ArrayAdapter listAdapter = new ArrayAdapter<String>(context,
                                                        R.layout.search_result_ingredient,
                                                        ingredientsList);
    cardIngredients.setAdapter(listAdapter);
    container.addView(view, position);
    return view;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View)object);
  }
}
