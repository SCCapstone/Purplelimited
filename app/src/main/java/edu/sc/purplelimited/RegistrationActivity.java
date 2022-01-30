package edu.sc.purplelimited;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    // Define the UI elements
    private EditText RegistrationUsername;
    private EditText RegistrationPassword;
    private EditText RegistrationPassword2;
    private Button Register;

    public static UserAccount userAccount;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Binding the XML elements
        RegistrationUsername = findViewById(R.id.RegistrationName);
        RegistrationPassword = findViewById(R.id.RegistrationPassword);
        RegistrationPassword2 = findViewById(R.id.RegistrationPassword2);
        Register = findViewById(R.id.Register);

        // Temporarily saving user account information on the users phone in MODE_PRIVATE to only allow the app to access the information
        //TODO implement Firebase's drop-in authentication solution || implement hashmap to allow for multiple user accounts to be saved locally
        sharedPreferences = getApplicationContext().getSharedPreferences("UserAccountEX", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        // Clicking the Register Button
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Obtain the inputs from the fields
                String userName = RegistrationUsername.getText().toString();
                String userPassword = RegistrationPassword.getText().toString();
                String userPassword2 = RegistrationPassword2.getText().toString();

                // Check if the fields are valid
                if(validate(userName, userPassword, userPassword2))
                {
                    // Add the credentials into our database
                    userAccount = new UserAccount(userName, userPassword);

                    // Storing credentials and adds them to a file
                    sharedPreferencesEditor.putString("Username", userName);
                    sharedPreferencesEditor.putString("Password", userPassword);
                    sharedPreferencesEditor.apply();

                    // Go to Login Activity
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }

    boolean validate(String name, String password, String password2)
    {
        // Check if the name is empty and password field is 8 characters long
        if(name.isEmpty() || (password.length() < 8))
        {
            Toast.makeText(this, "Please re-enter your name and ensure password is more than 8 characters long!", Toast.LENGTH_SHORT).show();
            return false;

        }
        // Check if the passwords match (to ensure the user did not mistype it when creating the account)
        if(!password.equals(password2)) {
            Toast.makeText(this, "Please ensure your passwords match!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

