package com.xncoder.nicd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private Popup popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView signinText = findViewById(R.id.signinText);
        signinText.setOnClickListener(view -> finish());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Button register = findViewById(R.id.registerButton);
        EditText emailText = findViewById(R.id.reg_email);
        EditText passwordText = findViewById(R.id.reg_password);
        EditText cpasswordText = findViewById(R.id.reg_confirmpassword);
        register.setOnClickListener(view -> {
            String email, password, cpassword, popup_msg = "Please enter your ";
            boolean popshow = false;
            email = String.valueOf(emailText.getText());
            password = String.valueOf(passwordText.getText());
            cpassword = String.valueOf(cpasswordText.getText());
            if(TextUtils.isEmpty(email)) {
                popup_msg += "Email ";
                popshow = true;
            }
            if(TextUtils.isEmpty(password)) {
                popup_msg += "Password ";
                popshow = true;
            }
            if(TextUtils.isEmpty(cpassword)) {
                popup_msg += "Confirm password";
                popshow = true;
            }
            if(popshow) {
                popupWindow = new Popup(getApplicationContext(), false, popup_msg);
                popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
            } else if(!password.equals(cpassword)) {
                popupWindow = new Popup(getApplicationContext(), false, "Both passwords are not matched");
                popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent verifyIntent = new Intent(RegisterActivity.this, VerifyActivity.class);
                                FirebaseUser user = auth.getCurrentUser();
                                verifyIntent.putExtra("Email", email);
                                verifyIntent.putExtra("User", user);
                                startActivity(verifyIntent);
                            } else {
                                popupWindow = new Popup(getApplicationContext(), false, "Error: " + Objects.requireNonNull(task.getException()).getMessage());
                                popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                            }
                        });
            }
        });
    }
}