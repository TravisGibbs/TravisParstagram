package com.example.travisparstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.travisparstagram.DataTypes._User;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String Tag = "main";
    private EditText userTextEdit;
    private EditText passTextEdit;
    private Button loginButton;
    private Button registerButton;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _User user = new _User();
        if(user.getCurrentUser()!=null){
            Log.i(Tag,"logged in");
            goToMainActivity();
        }

        userTextEdit = findViewById(R.id.usernameText);
        passTextEdit = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        linearLayout = findViewById(R.id.LiLayout);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(Tag, "login tapped");
                String userText = String.valueOf(userTextEdit.getText());
                String passText = String.valueOf(passTextEdit.getText());
                loginUser(userText, passText);
            }
        });
    }



    private void loginUser(String userText, String passText){
            ParseUser.logInInBackground(userText, passText, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.i(Tag,"user logged in");
                    goToMainActivity();
                } else {
                    Log.e(Tag,"user login failed",e);
                    Snackbar.make(linearLayout, "login failed", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void goToRegister(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}