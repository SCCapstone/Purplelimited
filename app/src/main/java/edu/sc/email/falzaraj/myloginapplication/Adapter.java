package edu.sc.email.falzaraj.myloginapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;


public class Adapter extends PagerAdapter {

    private Context context;
    private ArrayList<Model> recipes;

    public Adapter(Context context, ArrayList<Model> recipes) {
        this.context = context;
        this.recipes = recipes;
    }


    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card, container, false);

        ImageView cardV = view.findViewById(R.id.cardV);
        TextView recipeV = view.findViewById(R.id.recipeV);
        TextView descriptionV = view.findViewById(R.id.descriptionV);


        Model model = recipes.get(position);
        final String recipe = model.getRecipe();
        final String description = model.getDescription();
        int image = model.getImage();

        cardV.setImageResource(image);
        recipeV.setText(recipe);
        descriptionV.setText(description);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, recipe + "\n" + description, Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(view, position);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);    }
}
