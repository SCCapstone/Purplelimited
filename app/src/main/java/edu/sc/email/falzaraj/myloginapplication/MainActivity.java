package edu.sc.email.falzaraj.myloginapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity{

    private EditText Name;
    private EditText Password;
    private TextView AttemptsInfo;
    private Button Login;
    private int cnt = 3; //base amount of login attempts

    String userName = "admin";
    String userPassword = "admin"; //these are the default hard-coded strings for a user account for the interim

   //class for user account information
    class UserAccount
    {
        TextView Name = findViewById(R.id.etName);
        TextView Password = findViewById(R.id.etPassword);
    }

    boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding the XML views
        Name = findViewById(R.id.etName);
        Password = findViewById(R.id.etPassword);
        AttemptsInfo = findViewById(R.id.noAttempts);
        Login = findViewById(R.id.loginbutton);

        // When login button is clicked
            Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // user inputs
                userName = Name.getText().toString();
                userPassword = Password.getText().toString();

                if(userName.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a username and password!", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MainActivity.this, "You have used all your attempts", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Incorrect credentials, please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        // Send user to the next activity *DO NOT USE THIS YET, this if for transitioning once the files are merged
                        // startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                    }

                }
            }
        });
    }

    private boolean validate(String userName, String userPassword)
    {
        // Get the object of UserAccount class
        UserAccount userAccount = new UserAccount();

        // Match user details
        if(userName.equals(userAccount.Name) && userPassword.equals(userAccount.Password)) {
            return true;
        }
        return false;
    }
}