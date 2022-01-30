package edu.sc.purplelimited;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity{

    private EditText Name;
    private EditText Password;
    private TextView AttemptsInfo;
    private Button Login;
    private Button Register;
    private CheckBox RememberMe;
    private int cnt = 3; //base amount of login attempts

    String userName = "admin";
    String userPassword = "admin"; //these are hard-coded for testing

    boolean isValid = false;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Binding the XML views
        Name = findViewById(R.id.userName);
        Password = findViewById(R.id.userPassword);
        AttemptsInfo = findViewById(R.id.noAttempts);
        Login = findViewById(R.id.loginbutton);
        Register = findViewById(R.id.registerbutton);
        RememberMe = findViewById(R.id.RememberMe);

        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        if(sharedPreferences != null){

            String savedUsername = sharedPreferences.getString("Username", "");
            String savedPassword = sharedPreferences.getString("Password", "");

            RegistrationActivity.userAccount = new UserAccount(savedUsername, savedPassword);

            if(sharedPreferences.getBoolean("Checkbox", false)){
                Name.setText(savedUsername);
                Password.setText(savedPassword);
                RememberMe.setChecked(true);
            }
        }

            // When login button is clicked
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // user inputs
                userName = Name.getText().toString();
                userPassword = Password.getText().toString();

                if(userName.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter a username and password!", Toast.LENGTH_LONG).show();
                }
                else {

                    // Validate the user inputs
                    isValid = validate(userName, userPassword);
                    if (!isValid) {
                        cnt--;
                        AttemptsInfo.setText("Attempts Remaining: " + String.valueOf(cnt));

                        // Disable login button when counter == 0
                        if (cnt == 0) {
                            Login.setEnabled(false);
                            Toast.makeText(LoginActivity.this, "You have used all your attempts", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Incorrect credentials, please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        // Send user to the next activity
                        Intent intent = new Intent();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                }
            }

        });

        // When register button is clicked
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        // If the 'Remember Me' checkbox is checked..
        RememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferencesEditor.putBoolean("RememberMeCheckbox", RememberMe.isChecked());

                sharedPreferencesEditor.apply();
            }
        });
    }

    private boolean validate(String userName, String userPassword)
    {
        // Match user details
        if(RegistrationActivity.userAccount != null) {
            if(userName.equals(RegistrationActivity.userAccount.getuserName()) && userPassword.equals(RegistrationActivity.userAccount.getuserPassword())) {
                return true;
            }
        }

        return false;
    }
}