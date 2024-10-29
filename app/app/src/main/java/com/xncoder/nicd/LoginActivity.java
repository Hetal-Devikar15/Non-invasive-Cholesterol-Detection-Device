package com.xncoder.nicd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private Popup popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView signupText = findViewById(R.id.signupText);
        signupText.setOnClickListener(view -> {
            Intent signupIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(signupIntent);
        });

        TextView fgPasswordText = findViewById(R.id.fg_password);
        fgPasswordText.setOnClickListener(view -> {
            Intent fgPasswordIntent = new Intent(LoginActivity.this, ForgetActivity.class);
            startActivity(fgPasswordIntent);
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        EditText emailText = findViewById(R.id.login_email);
        EditText passwordText = findViewById(R.id.login_password);
        Button loginBtn = findViewById(R.id.loginButton);
        Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        CredentialDB credentials = new CredentialDB(this);

        loginBtn.setOnClickListener(view -> {
            String email, password, popupMsg="Please enter your ";
            boolean popupShow = false;
            email = String.valueOf(emailText.getText());
            password = String.valueOf(passwordText.getText());
            if(TextUtils.isEmpty(email)) {
                popupMsg += "Email ";
                popupShow = true;
            }
            if(TextUtils.isEmpty(password)) {
                popupMsg += "Password";
                popupShow = true;
            }
            if(popupShow) {
                popupWindow = new Popup(getApplicationContext(), false, popupMsg);
                popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
            } else {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                if(user != null && user.isEmailVerified()) {

                                    Toast.makeText(this, "Saved : " + credentials.addUser(email, password) , Toast.LENGTH_SHORT).show();
                                    popupWindow = new Popup(getApplicationContext(), true, "Login Successfully");
                                    popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                                    popupWindow.onBtn.setOnClickListener(view1 -> startActivity(homeIntent));
                                } else {
                                    Toast.makeText(this, "Dropped", Toast.LENGTH_SHORT).show();
                                    popupWindow = new Popup(getApplicationContext(), false, "Your email is not verified");
                                    popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                                    credentials.clearDatabase();
                                }
                            } else {
                                Toast.makeText(this, "Dropped", Toast.LENGTH_SHORT).show();
                                popupWindow = new Popup(getApplicationContext(), false, "Error: " + Objects.requireNonNull(task.getException()).getMessage());
                                popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                                credentials.clearDatabase();
                            }
                        });
            }
        });
    }
}