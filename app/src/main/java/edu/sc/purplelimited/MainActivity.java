package edu.sc.purplelimited;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import edu.sc.purplelimited.R;
import edu.sc.purplelimited.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static boolean loggedIn = true;
    private static int userId = 000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        if(loggedIn) {
            setContentView(binding.getRoot());
            findViewById(R.id.nav_view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_suggestions, R.id.navigation_saved_recipes, R.id.navigation_on_hand, R.id.navigation_search)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        } else {
            //TODO: login Activity
        }
    }

    public static void setLoggedIn(Boolean bool) {
        loggedIn = bool;
    }

    public static void setUserId(int value) {
        userId = value;
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
