package edu.sc.email.falzaraj.myloginapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    // template class
    public class DataBaseReader {
        // stream of data from data base
        private String fromDatabase = "";
        private JSONObject jsonObject;

        public void fetchDatabase() {
            try {
                jsonObject = new JSONObject(fromDatabase);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // template class
    public class DataBaseWriter {
        // current working array list
        private ArrayList arrayList = new ArrayList();
        private JSONObject toDataBase = new JSONObject();

        public void prepareJSONObject() {
            for (int i = 0; i < arrayList.size(); i++) {
                // write array list items to JSONObject
            }
        }
        public void writeChanges() {
            // write JSONObject to database
        }
    }

    SignInButton signInButton;
    private static final int SIGN_IN = 1;`

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