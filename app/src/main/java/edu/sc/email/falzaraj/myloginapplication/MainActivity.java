package edu.sc.email.falzaraj.myloginapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    SignInButton signInButton;
    private static final int SIGN_IN = 1;

    @Override //doing a googleAPISign in with deprecated classes
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username1 = findViewById(R.id.username1);
        TextView password = findViewById(R.id.password);

        MaterialButton loginbutton = (MaterialButton)  findViewById(R.id.loginbutton);

        //admin & admin example

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username1.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    //correct login
                    Toast.makeText(MainActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }else
                    //incorrect login
                    Toast.makeText(MainActivity.this, "LOGIN UNSUCCESSFUL", Toast.LENGTH_SHORT).show();
            }
        });
    }
}