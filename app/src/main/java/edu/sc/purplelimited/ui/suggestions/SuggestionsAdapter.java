package edu.sc.purplelimited.ui.suggestions;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.ImageQueue;
import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.ui.saved_recipes.SavedRecipesFragment;

public class SuggestionsAdapter extends PagerAdapter {

    private final Context context;
    private final ArrayList<Recipe> suggestions;

    public SuggestionsAdapter(Context context, ArrayList<Recipe> suggestions) {
        this.context = context;
//        randomizeSuggestions();
        this.suggestions = suggestions;

    }

    public void randomizeSuggestions() {
        Random rand = new Random();
        for (int i = 0; i < suggestions.size(); i++) {
            int randomIndex = rand.nextInt(suggestions.size());
            Recipe randomSugg = suggestions.get(randomIndex);
        }
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.sugg_card_images, container, false);

        // Instantiate the Views
        ImageView cardImage = view.findViewById(R.id.suggCard);
        TextView cardTitle = view.findViewById(R.id.suggCardTitle);
        TextView cardDescription = view.findViewById(R.id.suggCardDescription);
        Button saveButton = view.findViewById(R.id.saveSuggest);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavedRecipesFragment.addRecipe(suggestions.get(position), context);
            }
        });

        // Get current recipe information
        Recipe recipe = suggestions.get(position);
        String title = recipe.getName();
        if(title.length() > 56) {
            title = title.substring(0,56) + "...";
        }
        String description = recipe.getDescription();
        String url = recipe.getThumbnailURL();

        // Image request for the thumbnail
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        cardImage.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {/*empty*/}
        });
        ImageQueue.getInstance(context).addToQueue(imageRequest);

        cardTitle.setText(title);
        cardDescription.setText(description);
        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
