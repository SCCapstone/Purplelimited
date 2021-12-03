package edu.sc.purplelimited;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity{

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
        setContentView(R.layout.activity_login);

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
    }

    private boolean validate(String userName, String userPassword)
    {
        // Get the object of UserAccount class
        UserAccount userAccount = new UserAccount();

        // Match user details
        //if(userName.equals(userAccount.Name) && userPassword.equals(userAccount.Password))
        if(userName.equals("admin") && userPassword.equals("admin")){
            return true;
        }
        return false;
    }
}