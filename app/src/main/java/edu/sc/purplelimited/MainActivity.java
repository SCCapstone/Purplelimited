package edu.sc.purplelimited;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
        }
        else {
            //Takes user back to login page when loggedIn = false
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        } else {
            return false;
        }
    }

    public static void setLoggedIn(Boolean bool) {
        loggedIn = bool;
    }

    public static void setUserId(int value) {
        userId = value;
    }
}
