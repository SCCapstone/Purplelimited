package edu.sc.email.falzaraj.myloginapplication;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;


public class MainActivity2 extends AppCompatActivity {

    private ActionBar actionBar;

    private ViewPager viewPager;

    private ArrayList<Model> recipes;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        viewPager = findViewById(R.id.viewPager);
        loadCards();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String title = recipes.get(position).getRecipe();
                actionBar.setTitle(title);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void loadCards() {
        recipes = new ArrayList<>();

        recipes.add(new Model(
                "Beef and Broccoli",
                "Tender strips of steak and crisp broccoli florets in a rich ginger and garlic sauce",
                R.drawable.beefnbroc));
        recipes.add(new Model(
                "Meatball Sub",
                "Tender juicy meatballs simmered in a flavorful tomato sauce are placed in a roll and topped with cheese",
                R.drawable.meatballsub));
        recipes.add(new Model(
                "Penne Vodka",
                "Made with heavy cream, crushed tomatoes, onions",
                R.drawable.pennevodka));
        recipes.add(new Model(
                "Stuffed Red Peppers",
                "Hollowed or halved peppers filled with any of a variety of fillings, often including meat, vegetables, cheese, rice, or sauce",
                R.drawable.stuffedrpeppers));

        adapter = new Adapter(this, recipes);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(100, 0,100,0);

    }
}