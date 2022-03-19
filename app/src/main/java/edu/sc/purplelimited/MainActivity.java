package edu.sc.purplelimited;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.sc.purplelimited.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static boolean loggedIn = true;
    private static int userId = 000;
    private Button Logout;

//    private Button save_recipe_button;
//    public static final String SHARED_PREFS = "sharedPrefs";
//    public static final String RECIPE = "recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding;
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Binding the XML views
        Logout = findViewById(R.id.logoutbutton);

        while(loggedIn = true) {                                  //changed to a while loop
            setContentView(binding.getRoot());
            findViewById(R.id.nav_view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_suggestions, R.id.navigation_saved_recipes, R.id.navigation_on_hand, R.id.navigation_search)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);

            Logout.setOnClickListener(new View.OnClickListener() {  //logout button forces while loop to end
                @Override
                public void onClick(View view) {
                    loggedIn = false;
                }
            });
        }
        //Takes user back to login page when loggedIn = false
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public static void setLoggedIn(Boolean bool) {
        loggedIn = bool;
    }

    public static void setUserId(int value) {
        userId = value;
    }

//    public void saveRecipe() {
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE); //(Context.MODE_PRIVATE)
//        SharedPreferences.Editor editor = sharedPreferences.edit();
////        editor.putString(RECIPE, textView.getText().toString());
//        editor.apply();
//    }
}
