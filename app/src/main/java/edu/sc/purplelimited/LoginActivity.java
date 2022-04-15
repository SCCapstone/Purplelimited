package edu.sc.purplelimited;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView AttemptsInfo;
    private Button Login;
    private Button Register;
    private CheckBox RememberMe;
    private int cnt = 3; //base amount of login attempts

    static String userName = "admin";
    String userPassword = "admin"; //these are hard-coded for testing

    boolean isValid = false;

    public UserAccount userAccount;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    FirebaseDatabase database;
    DatabaseReference usersReference;

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

        userAccount = new UserAccount();

        sharedPreferences = getApplicationContext().getSharedPreferences("UserAccount", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        // Check to see if the shared preferences file exists
        if(sharedPreferences != null) {

            Map<String, ?> preferencesMap = sharedPreferences.getAll();

            if(preferencesMap.size() != 0) {
                userAccount.loadAccount(preferencesMap);
            }

            String savedUsername = sharedPreferences.getString("SavedUsername", "");
            String savedPassword = sharedPreferences.getString("SavedPassword", "");

            if(sharedPreferences.getBoolean("Checkbox", false)) {
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
                    database = FirebaseDatabase.getInstance();
                    DatabaseReference userFromFirebase = database.getReference("users").child(userName);
                    DatabaseReference passwordFromFirebase = userFromFirebase.child("password");
                    Task<DataSnapshot> checkPass = passwordFromFirebase.get();
                    checkPass.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            String fromFirebase = task.getResult().getValue(String.class);
                            boolean passIsNull = fromFirebase==null;
                            boolean matches;
                            if (passIsNull) {
                                matches = false;
                            } else {
                                matches = fromFirebase.equals(userPassword);
                            }

                            if (!matches) {
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

                                // Set registration flag
                                database = FirebaseDatabase.getInstance();
                                usersReference = database.getReference("users");
                                usersReference.child(userName).child("flag").setValue("true");

                                // Send user to the next activity
                                Intent intent = new Intent();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        }
                    });
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

    private boolean validate(String userName, String userPassword) {
        return userAccount.checkAccount(userName, userPassword); // moved validate from here to the UserAccount.java
    }

    public static String getCurrentUserName() {
        return userName;
    }
}